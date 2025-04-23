package com.gamesUP.gamesUP.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JwtService {

    private static final String CLAIM_SCOPE = "scope";
    private static final String SCOPE_PREFIX = "SCOPE_";
    private static final String ROLE_PREFIX = "ROLE_";

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration-in-days}")
    private int expirationInDays;

    @Value("${jwt.secret-key}")
    private String secretKey;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        // Scope contient les rôles avec SCOPE_ comme préfixe
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

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public long getExpirationTime(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expirationDate = claims.getExpiration();
        return (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
    }
}
