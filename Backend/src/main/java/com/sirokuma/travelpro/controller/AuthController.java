package com.sirokuma.travelpro.controller;

import com.sirokuma.travelpro.Util.GoogleOAuth2Client;
import com.sirokuma.travelpro.Util.JwtProvider;
import com.sirokuma.travelpro.Util.KakaoOAuth2Client;
import com.sirokuma.travelpro.Util.NaverOAuth2Client;
import com.sirokuma.travelpro.dto.SocialLoginDTO;
import com.sirokuma.travelpro.entity.Role;
import com.sirokuma.travelpro.entity.SocialLogin;
import com.sirokuma.travelpro.entity.User;
import com.sirokuma.travelpro.response.CustomResponse;
import com.sirokuma.travelpro.response.CustomResponseCode;
import com.sirokuma.travelpro.service.SocialLoginService;
import com.sirokuma.travelpro.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SocialLoginService socialLoginService;
    private final GoogleOAuth2Client googleOAuth2Client;
    private final KakaoOAuth2Client kakaoOAuth2Client;
    private final NaverOAuth2Client naverOAuth2Client;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    public AuthController(
            SocialLoginService socialLoginService,
            GoogleOAuth2Client googleOAuth2Client,
            KakaoOAuth2Client kakaoOAuth2Client,
            NaverOAuth2Client naverOAuth2Client,
            UserService userService,
            JwtProvider jwtProvider) {
        this.socialLoginService = socialLoginService;
        this.googleOAuth2Client = googleOAuth2Client;
        this.kakaoOAuth2Client = kakaoOAuth2Client;
        this.naverOAuth2Client = naverOAuth2Client;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/oauth2/callback")
    public ResponseEntity<CustomResponse> handleOAuth2Callback(@RequestBody SocialLoginDTO request) {
        String code = request.getCode();
        String provider = request.getProvider();
        String email;

        try {
            switch (provider) {
                case "google":
                    email = googleOAuth2Client.getEmail(code);
                    break;
                case "kakao":
                    email = kakaoOAuth2Client.getEmail(code);
                    break;
                case "naver":
                    email = naverOAuth2Client.getEmail(code);
                    break;
                default:
                    return ResponseEntity.badRequest().body(new CustomResponse(CustomResponseCode.ERROR, "Unsupported provider"));
            }

            Optional<User> userOptional = userService.getUserByEmail(email);
            User user;
            if (userOptional.isEmpty()) {
                // Create a new user if one does not exist
                user = new User();
                user.setEmail(email);
                String uniqueName = "temp_" + LocalDate.now() + "_" + UUID.randomUUID();
                user.setName(uniqueName); // Generate a unique name
                user.setRole(Role.UNREGISTERED);
                user.setActive(true); // Set active to true if necessary
                user = userService.save(user);

                // Create social login entry
                SocialLogin socialLogin = new SocialLogin();
                socialLogin.setUser(user);
                socialLogin.setProvider(provider);
                socialLogin.setProviderId(email); // or other unique provider ID
                socialLoginService.save(socialLogin);
            } else {
                user = userOptional.get();
            }

            String jwt = jwtProvider.generateToken(user);

            // If user is unregistered
            if (user.getRole() == Role.UNREGISTERED) {
                return ResponseEntity.status(500).body(new CustomResponse(CustomResponseCode.SIGNUP_REQUIRED, jwt));
            }

            return ResponseEntity.ok(new CustomResponse(CustomResponseCode.SUCCESS, jwt));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new CustomResponse(CustomResponseCode.ERROR, "Authentication error"));
        }
    }

    @GetMapping("/social/google")
    public ResponseEntity<?> redirectToGoogle() {
        String authorizationUri = "https://accounts.google.com/o/oauth2/auth?" +
                "client_id=" + googleOAuth2Client.getClientId() +
                "&response_type=code" +
                "&scope=email profile" +
                "&redirect_uri=" + googleRedirectUri;

        return ResponseEntity.status(302).header("Location", authorizationUri).build();
    }

    @GetMapping("/social/kakao")
    public ResponseEntity<?> redirectToKakao() {
        String authorizationUri = "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + kakaoOAuth2Client.getClientId() +
                "&response_type=code" +
                "&redirect_uri=" + kakaoRedirectUri;

        return ResponseEntity.status(302).header("Location", authorizationUri).build();
    }

    @GetMapping("/social/naver")
    public ResponseEntity<?> redirectToNaver() {
        String authorizationUri = "https://nid.naver.com/oauth2.0/authorize?" +
                "client_id=" + naverOAuth2Client.getClientId() +
                "&response_type=code" +
                "&redirect_uri=" + naverRedirectUri;

        return ResponseEntity.status(302).header("Location", authorizationUri).build();
    }
}
