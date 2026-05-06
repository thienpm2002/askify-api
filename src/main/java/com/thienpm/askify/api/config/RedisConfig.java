package com.thienpm.askify.api.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

        @Bean
        public ObjectMapper objectMapper() {
                ObjectMapper mapper = new ObjectMapper();
                mapper.findAndRegisterModules();

                mapper.activateDefaultTyping(
                                mapper.getPolymorphicTypeValidator(),
                                ObjectMapper.DefaultTyping.NON_FINAL);

                return mapper;
        }

        @Bean
        public RedisSerializer<Object> jacksonSerializer(ObjectMapper objectMapper) {
                return new GenericJackson2JsonRedisSerializer(objectMapper);
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory,
                        RedisSerializer<Object> jacksonSerializer) {
                RedisTemplate<String, Object> template = new RedisTemplate<>();
                template.setConnectionFactory(factory);
                template.setKeySerializer(RedisSerializer.string());
                template.setValueSerializer(jacksonSerializer);
                template.setHashKeySerializer(RedisSerializer.string());
                template.setHashValueSerializer(jacksonSerializer);
                template.afterPropertiesSet();
                return template;
        }

        @Bean
        public RedisCacheManager cacheManager(RedisConnectionFactory factory,
                        RedisSerializer<Object> jacksonSerializer) {
                RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                                .serializeKeysWith(RedisSerializationContext.SerializationPair
                                                .fromSerializer(RedisSerializer.string()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                                .fromSerializer(jacksonSerializer))
                                .entryTtl(Duration.ofMinutes(10))
                                .disableCachingNullValues();

                return RedisCacheManager.builder(factory).cacheDefaults(config).build();
        }
}