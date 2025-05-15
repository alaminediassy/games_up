package com.gamesUP.gamesUP.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String CLAIM_SCOPE = "scope";
    private static final String SCOPE_PREFIX = "SCOPE_";
    private static final String ROLE_PREFIX = "ROLE_";

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration-in-days}")
    public int expirationInDays;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Génère un token JWT pour un utilisateur authentifié.
     * Le token inclut les rôles (scope) avec le préfixe SCOPE_ pour Spring Security.
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> {
                    if (authority.startsWith(ROLE_PREFIX)) {
                        return SCOPE_PREFIX + authority;
                    }
                    return SCOPE_PREFIX + ROLE_PREFIX + authority;
                })
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("gamesup")
                .issuedAt(now)
                .expiresAt(now.plus(expirationInDays, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim(CLAIM_SCOPE, scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims
        )).getTokenValue();
    }
}

