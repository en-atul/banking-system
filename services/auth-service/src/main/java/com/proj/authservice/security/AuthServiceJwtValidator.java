package com.proj.authservice.security;

import com.proj.authservice.service.token.TokenService;
import com.proj.shared.jwt.AbstractJwtValidator;
import com.proj.shared.jwt.JwtClaims;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceJwtValidator extends AbstractJwtValidator {

    private final TokenService tokenService;

    public AuthServiceJwtValidator(TokenService tokenService, @Value("${auth.token.jwtSecret}") String jwtSecret) {
        super(jwtSecret);
        this.tokenService = tokenService;
    }

    @Override
    public JwtClaims validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return new JwtClaims(null, null, null, null, null, false);
        }

        try {
            Claims claims = parseClaims(token);
            if (claims == null) {
                return new JwtClaims(null, null, null, null, null, false);
            }

            // Check if token is expired
            if (isTokenExpired(token)) {
                return new JwtClaims(
                    claims.getId(),
                    claims.getSubject(),
                    claims.get("userId", Long.class),
                    (java.util.Collection<String>) claims.get("roles"),
                    claims.get("type", String.class),
                    false
                );
            }

            String tokenType = extractTokenType(token);
            boolean isValid = false;

            // For access tokens, validate against database
            if ("ACCESS".equals(tokenType)) {
                String tokenId = claims.getId();
                isValid = tokenService.validateAccessToken(tokenId);
            }
            // For refresh tokens, validate against database
            else if ("REFRESH".equals(tokenType)) {
                isValid = tokenService.validateRefreshToken(token);
            }

            return new JwtClaims(
                claims.getId(),
                claims.getSubject(),
                claims.get("userId", Long.class),
                (java.util.Collection<String>) claims.get("roles"),
                tokenType,
                isValid
            );

        } catch (Exception e) {
            return new JwtClaims(null, null, null, null, null, false);
        }
    }
} 