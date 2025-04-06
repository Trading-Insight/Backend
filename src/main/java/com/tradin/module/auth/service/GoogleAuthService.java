package com.tradin.module.auth.service;

import com.tradin.module.auth.service.dto.GoogleAccessTokenResponseDto;
import com.tradin.module.auth.service.dto.UserDataDto;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class GoogleAuthService {

    private final RestTemplate authClient = new RestTemplate();
    @Value("${google.login.client_id}")
    private String googleClientId;

    @Value("${google.login.client_secret}")
    private String googleClientSecret;

    @Value("${google.login.redirect_uri}")
    private String googleRedirectUri;

    public UserDataDto getUserInfo(String authorizationCode) {
        String host = "https://oauth2.googleapis.com/tokeninfo?id_token=";
        String accessToken = getGoogleAccessToken(authorizationCode);
        String url = host + accessToken;

        return authClient.getForObject(url, UserDataDto.class);
    }

    private String getGoogleAccessToken(String authorizationCode) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("client_id", googleClientId);
        params.put("client_secret", googleClientSecret);
        params.put("code", authorizationCode);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", googleRedirectUri);
        String host = "https://oauth2.googleapis.com/token";

        GoogleAccessTokenResponseDto googleAccessTokenResponseDto = authClient.postForObject(
            host,
            params,
            GoogleAccessTokenResponseDto.class
        );

        return googleAccessTokenResponseDto.accessToken();
    }
}
