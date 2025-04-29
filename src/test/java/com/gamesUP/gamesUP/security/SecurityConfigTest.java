package com.gamesUP.gamesUP.security;

import com.gamesUP.gamesUP.filter.JwtTokenFilter;
import org.junit.jupiter.api.Test;
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

class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void shouldCreatePasswordEncoder() {
        assertThat(securityConfig.passwordEncoder()).isNotNull();
    }

    @Test
    void shouldCreateJwtAuthenticationConverter() {
        assertThat(securityConfig.jwtAuthenticationConverter()).isNotNull();
    }

    @Test
    void shouldCreateJwtEncoder() {
        // Injecter manuellement la clé secrète
        ReflectionTestUtils.setField(securityConfig, "key", "my-test-key");

        JwtEncoder encoder = securityConfig.jwtEncoder();

        assertThat(encoder).isNotNull();
    }

    @Test
    void shouldCreateJwtDecoder() {
        // Injecter la clé secrète pour éviter NullPointerException
        ReflectionTestUtils.setField(securityConfig, "key", "my-test-key");

        JwtDecoder decoder = securityConfig.jwtDecoder();

        assertThat(decoder).isNotNull();
    }

    @Test
    void shouldCreateSecurityFilterChain() throws Exception {
        // Arrange
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        JwtTokenFilter jwtTokenFilter = mock(JwtTokenFilter.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.oauth2ResourceServer(any())).thenReturn(httpSecurity);
        when(httpSecurity.httpBasic(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);

        // Act
        SecurityFilterChain filterChain = securityConfig.securityFilterChain(httpSecurity, jwtTokenFilter);

        // Assert
        assertThat(filterChain).isNotNull();
    }

}
