package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10); // 최대 연결 수 설정
        return new JedisPool(config, "localhost", 6379); // Redis 서버 정보
    }

    @Bean
    public Jedis jedis(JedisPool jedisPool) {
        return jedisPool.getResource(); // Jedis 객체 생성
    }
}
