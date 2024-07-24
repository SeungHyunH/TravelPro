package com.sirokuma.travelpro.controller;

import com.sirokuma.travelpro.Util.JwtProvider;
import com.sirokuma.travelpro.dto.LoginDTO;
import com.sirokuma.travelpro.dto.UserDTO;
import com.sirokuma.travelpro.entity.User;
import com.sirokuma.travelpro.exception.InvalidCredentialsException;
import com.sirokuma.travelpro.exception.UserNotFoundException;
import com.sirokuma.travelpro.response.CustomResponse;
import com.sirokuma.travelpro.response.CustomResponseCode;
import com.sirokuma.travelpro.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        try {
            User user = userService.getUserByEmailAndPassword(email, password);
            String jwt = jwtProvider.generateToken(user);
            return ResponseEntity.ok(Map.of("token", jwt));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new CustomResponse(CustomResponseCode.USER_NOT_FOUND, e.getMessage()));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.badRequest().body(new CustomResponse(CustomResponseCode.INVALID_CREDENTIALS, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CustomResponse(CustomResponseCode.ERROR, "An unexpected error occurred"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            if (userService.emailExists(userDTO.getEmail())) {
                return ResponseEntity.badRequest().body(new CustomResponse(CustomResponseCode.DUPLICATE_EMAIL, "Email already in use"));
            }

            User user = userService.createUser(userDTO.getEmail(), userDTO.getPassword(), userDTO.getName());
            return ResponseEntity.ok(new CustomResponse(CustomResponseCode.SUCCESS, "User created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CustomResponse(CustomResponseCode.ERROR, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new CustomResponse(CustomResponseCode.ERROR, "An unexpected error occurred"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
        String jwt = jwtProvider.resolveToken(request);
        if (jwt != null && jwtProvider.validateToken(jwt)) {
            String email = jwtProvider.getUserEmailFromToken(jwt);
            Optional<User> userOptional = userService.getUserByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(user.getEmail());
                userDTO.setName(user.getName());
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.status(404).build();
            }
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}
