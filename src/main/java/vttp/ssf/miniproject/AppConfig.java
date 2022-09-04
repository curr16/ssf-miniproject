package vttp.ssf.miniproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class AppConfig {

    @Value ("${spring.redis.host}")
    private String redisHost;

    @Value ("${spring.redis.port}")
    private Integer redisPort;

    @Value ("${spring.redis.database}")
    private Integer redisDatabase;

    @Value ("${spring.redis.username}")
    private String redisUsername;

    @Value ("${REDIS_PASSWORD}")
    private String redisPassword;

    @Bean("template")
    public RedisTemplate <String, String> initRedisTemplate() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();

        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);
        redisConfig.setDatabase(redisDatabase);
        redisConfig.setUsername(redisUsername);
        redisConfig.setPassword(redisPassword);

        System.out.printf("username: %s, password: %s\n", redisUsername, redisPassword);

        //create an instance of the Jedis driver
        JedisClientConfiguration jedisConfig = JedisClientConfiguration.builder().build();

        //create a factory for jedis connection
        JedisConnectionFactory jedisFac = new JedisConnectionFactory(redisConfig, jedisConfig);
        jedisFac.afterPropertiesSet();

        // create Redistemplate
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisFac);
        //explain tmr
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}