package com.gamesUP.gamesUP.unit.security;

import com.gamesUP.gamesUP.filter.JwtTokenFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gamesUP.gamesUP.filter.JwtTokenFilter;
import com.gamesUP.gamesUP.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
        securityConfig.key = "this-is-a-very-secret-key-for-test-1234567890";
    }

    @Test
    void jwtAuthenticationConverter_shouldExtractAuthoritiesFromScope() {
        JwtAuthenticationConverter converter = securityConfig.jwtAuthenticationConverter();

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "HS256")
                .claim("scope", "ROLE_ADMIN ROLE_CLIENT")
                .subject("user")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        var auth = converter.convert(jwt);

        assertThat(auth).isNotNull();
        assertThat(auth.getAuthorities())
                .extracting("authority")
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_CLIENT");
    }

    @Test
    void passwordEncoder_shouldEncodePasswordCorrectly() {
        var encoder = securityConfig.passwordEncoder();
        String rawPassword = "password123";
        String encoded = encoder.encode(rawPassword);

        assertThat(encoded).isNotEqualTo(rawPassword);
        assertThat(encoder.matches(rawPassword, encoded)).isTrue();
    }

    @Test
    void shouldReturnJwtEncoder() {
        JwtEncoder encoder = securityConfig.jwtEncoder();
        assertThat(encoder).isNotNull();
    }

    @Test
    void shouldReturnJwtDecoder() {
        JwtDecoder decoder = securityConfig.jwtDecoder();
        assertThat(decoder).isNotNull();
    }

    @Test
    void shouldBuildSecurityFilterChain() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, Mockito.RETURNS_DEEP_STUBS);
        JwtTokenFilter filter = mock(JwtTokenFilter.class);

        SecurityFilterChain chain = securityConfig.securityFilterChain(http, filter);
        assertThat(chain).isNotNull();
    }

}
