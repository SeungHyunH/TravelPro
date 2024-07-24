package com.sirokuma.travelpro.Util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class NaverOAuth2Client {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    public String getEmail(String code) {
        String accessToken = getAccessToken(code);
        return fetchEmailFromAccessToken(accessToken);
    }

    private String getAccessToken(String code) {
        String url = "https://nid.naver.com/oauth2.0/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<NaverTokenResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, NaverTokenResponse.class);
        return response.getBody() != null ? response.getBody().getAccessToken() : null;
    }

    private String fetchEmailFromAccessToken(String accessToken) {
        String url = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<NaverUserInfo> response = restTemplate.exchange(url, HttpMethod.GET, entity, NaverUserInfo.class);
        NaverUserInfo userInfo = response.getBody();

        return userInfo != null && userInfo.getResponse() != null ? userInfo.getResponse().getEmail() : null;
    }

    public String getClientId() {
        return clientId;
    }

    @Setter
    @Getter
    static class NaverTokenResponse {
        private String access_token;

        public String getAccessToken() {
            return access_token;
        }

        public void setAccessToken(String access_token) {
            this.access_token = access_token;
        }
    }

    @Setter
    @Getter
    static class NaverUserInfo {
        private Response response;

        @Setter
        @Getter
        static class Response {
            private String email;
        }
    }
}
