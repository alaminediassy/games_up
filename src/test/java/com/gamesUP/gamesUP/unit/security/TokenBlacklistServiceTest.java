package com.gamesUP.gamesUP.unit.security;

import com.gamesUP.gamesUP.security.TokenBlacklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TokenBlacklistServiceTest {

    private TokenBlacklistService tokenBlacklistService;

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        tokenBlacklistService = new TokenBlacklistService(redisTemplate);
    }

    @Test
    void shouldBlacklistToken() {
        String token = "token123";
        long expiration = 3600L;

        tokenBlacklistService.blacklistToken(token, expiration);

        verify(valueOperations).set(token, "blacklisted", expiration, TimeUnit.SECONDS);
    }

    @Test
    void shouldReturnTrueIfTokenIsBlacklisted() {
        String token = "blacklisted-token";

        when(redisTemplate.hasKey(token)).thenReturn(true);

        boolean result = tokenBlacklistService.isTokenBlacklisted(token);

        assertThat(result).isTrue();
        verify(redisTemplate).hasKey(token);
    }

    @Test
    void shouldReturnFalseIfTokenIsNotBlacklisted() {
        String token = "valid-token";

        when(redisTemplate.hasKey(token)).thenReturn(false);

        boolean result = tokenBlacklistService.isTokenBlacklisted(token);

        assertThat(result).isFalse();
        verify(redisTemplate).hasKey(token);
    }
}
