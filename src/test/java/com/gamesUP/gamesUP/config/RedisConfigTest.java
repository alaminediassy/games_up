package com.gamesUP.gamesUP.config;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RedisConfigTest {

    private final RedisConfig redisConfig = new RedisConfig();

    @Test
    void shouldCreateRedisTemplate() {
        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);
        RedisTemplate<String, String> redisTemplate = redisConfig.redisTemplate(connectionFactory);

        assertThat(redisTemplate).isNotNull();
        assertThat(redisTemplate.getKeySerializer()).isInstanceOf(StringRedisSerializer.class);
        assertThat(redisTemplate.getValueSerializer()).isInstanceOf(StringRedisSerializer.class);
    }
}
