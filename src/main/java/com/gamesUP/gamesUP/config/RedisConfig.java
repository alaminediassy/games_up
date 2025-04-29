package com.gamesUP.gamesUP.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * Déclare un bean RedisTemplate utilisé pour interagir avec Redis.
     * Utilise des sérialiseurs de type String pour les clés et les valeurs,
     * ce qui est adapté aux cas d’usage simples comme le stockage de tokens.
     *
     * @param connectionFactory le factory qui fournit la connexion à Redis
     * @return un RedisTemplate configuré pour (String, String)
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
