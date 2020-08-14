package com.jtang.redisson.core;

import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;

/**
 * Redis 默认配置
 * @author lin512100
 * @date 2020/8/14
 */
@Slf4j
@Component
@ConditionalOnMissingBean
public class DefaultRedisConfig implements RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Override
    public Config init() {
        Config config = new Config();
        try{
            String redisUrl = String.format("redis://%s:%s",redisProperties.getHost()+"",redisProperties.getPort()+"");
            config.useSingleServer().setAddress(redisUrl).setPassword(redisProperties.getPassword());
        }catch (Exception e){
            log.error("Redis 数据库配置异常："+ e.getMessage());
            e.printStackTrace();
        }
        return config;
    }
}
