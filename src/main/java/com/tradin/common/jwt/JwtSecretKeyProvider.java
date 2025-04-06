package com.tradin.common.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtSecretKeyProvider {

    private final Key secretKey;

    public JwtSecretKeyProvider(@Value("${JWT_SECRET_KEY}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public Key getSecretKey() {
        return secretKey;
    }
}
