package com.gamesUP.gamesUP.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TokenBlacklistServiceTest {

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private TokenBlacklistService tokenBlacklistService;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        tokenBlacklistService = new TokenBlacklistService(redisTemplate);
    }

    @Test
    void shouldBlacklistToken() {
        // Arrange
        String token = "test-token";
        long expirationTimeInSeconds = 3600;

        // Act
        tokenBlacklistService.blacklistToken(token, expirationTimeInSeconds);

        // Assert
        verify(valueOperations).set(token, "blacklisted", expirationTimeInSeconds, TimeUnit.SECONDS);
    }

    @Test
    void shouldReturnTrueWhenTokenIsBlacklisted() {
        // Arrange
        String token = "blacklisted-token";
        when(redisTemplate.hasKey(token)).thenReturn(true);

        // Act
        boolean result = tokenBlacklistService.isTokenBlacklisted(token);

        // Assert
        assertThat(result).isTrue();
        verify(redisTemplate).hasKey(token);
    }

    @Test
    void shouldReturnFalseWhenTokenIsNotBlacklisted() {
        // Arrange
        String token = "non-blacklisted-token";
        when(redisTemplate.hasKey(token)).thenReturn(false);

        // Act
        boolean result = tokenBlacklistService.isTokenBlacklisted(token);

        // Assert
        assertThat(result).isFalse();
        verify(redisTemplate).hasKey(token);
    }
}
