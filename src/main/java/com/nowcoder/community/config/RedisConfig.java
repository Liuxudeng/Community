package com.nowcoder.community.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {




    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,Object> template = new RedisTemplate<>() ;
        template.setConnectionFactory(factory);
        /**
         * 设置key的序列化方式
         */
        template.setKeySerializer(RedisSerializer.string());

        /**
         * 设置value的序列化方式
         * value可以是普通的值 也可以是序列化的列表
         *
         */

        template.setValueSerializer(RedisSerializer.json());


        /**
         * 设置哈希的key的序列化方式
         *
         */
        template.setHashKeySerializer(RedisSerializer.string());


        /**
         * 设置hash的value的序列化方式
         *
         */
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();

        return template;




    }
}
