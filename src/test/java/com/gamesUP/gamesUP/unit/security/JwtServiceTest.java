package com.gamesUP.gamesUP.unit.security;

import com.gamesUP.gamesUP.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;
    private JwtEncoder jwtEncoder;

    @BeforeEach
    void setUp() {
        jwtEncoder = mock(JwtEncoder.class);
        jwtService = new JwtService(jwtEncoder);
        jwtService.expirationInDays = 1;
    }

    @Test
    void shouldGenerateToken() {
        var authentication = new TestingAuthenticationToken(null, null, "ADMIN");

        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("mocked-token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        String token = jwtService.generateToken(authentication);

        assertThat(token).isEqualTo("mocked-token");
        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }
}
