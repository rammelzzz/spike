package com.imooc.spike.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午6:52 18-4-17
 * @Modified By:
 **/
@Service
public class RedisPoolFactory {

        private final Logger log = LoggerFactory.getLogger(getClass());

        @Autowired
        private RedisConfig redisConfig;

        @Bean
        public JedisPool jedisPoolFactory() {
                log.info("配置redis...");
                log.info("redisConfig={}", redisConfig);
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxIdle(redisConfig.getPoolMaxIdle());
                config.setMaxTotal(redisConfig.getPoolMaxTotal());
                config.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000); //millis
                JedisPool jedisPool = new JedisPool(config,
                        redisConfig.getHost(),
                        redisConfig.getPort());
                return jedisPool;
        }
}
