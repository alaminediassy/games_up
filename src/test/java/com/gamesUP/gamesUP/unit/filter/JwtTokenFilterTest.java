package com.gamesUP.gamesUP.unit.filter;

import com.gamesUP.gamesUP.filter.JwtTokenFilter;
import com.gamesUP.gamesUP.security.JwtService;
import com.gamesUP.gamesUP.security.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtTokenFilterTest {

    private JwtService jwtService;
    private TokenBlacklistService tokenBlacklistService;
    private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        tokenBlacklistService = mock(TokenBlacklistService.class);
        jwtTokenFilter = new JwtTokenFilter(jwtService, tokenBlacklistService);
    }

    @Test
    void shouldContinueFilterChainWhenTokenIsNotBlacklisted() throws ServletException, IOException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(tokenBlacklistService.isTokenBlacklisted("valid-token")).thenReturn(false);

        // Act
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(tokenBlacklistService).isTokenBlacklisted("valid-token");
    }

    @Test
    void shouldRejectRequestWhenTokenIsBlacklisted() throws ServletException, IOException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer blacklisted-token");
        when(tokenBlacklistService.isTokenBlacklisted("blacklisted-token")).thenReturn(true);

        // Act
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
        verify(tokenBlacklistService).isTokenBlacklisted("blacklisted-token");
        verifyNoInteractions(filterChain);
    }

    @Test
    void shouldContinueFilterChainWhenNoAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(tokenBlacklistService);
    }
}
