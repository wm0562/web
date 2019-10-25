package com.vortex.cloud.ums.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@RefreshScope
public class RedisConfig {

	@Value("${spring.redis.database}")
	private Integer database;
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private Integer port;
	@Value("${spring.redis.password}")
	private String password;
	@Value("${spring.redis.pool.max-active}")
	private Integer maxActive;
	@Value("${spring.redis.pool.max-wait}")
	private Long maxWait;
	@Value("${spring.redis.pool.max-idle}")
	private Integer maxIdle;
	@Value("${spring.redis.pool.min-idle}")
	private Integer minIdle;
	@Value("${spring.redis.timeout}")
	private Integer timeout;

	private JedisConnectionFactory initFactory() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxActive);
		config.setMaxTotal(maxActive);
		config.setMaxIdle(maxIdle);
		config.setMinIdle(minIdle);
		config.setMaxWaitMillis(maxWait);
		JedisConnectionFactory factory = new JedisConnectionFactory(config);
		factory.setHostName(host);
		factory.setPort(port);
		factory.setDatabase(database);
		factory.setPassword(password);
		factory.setTimeout(timeout);
		factory.afterPropertiesSet();
		return factory;
	}

	/**
	 * 实例化 RedisTemplate 对象
	 *
	 * @return
	 */
	@Bean
	@RefreshScope
	public RedisTemplate<String, Object> functionDomainRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(initFactory());
		initDomainRedisTemplate(redisTemplate);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	/**
	 * 设置数据存入 redis 的序列化方式
	 *
	 * @param redisTemplate
	 */
	private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
	}
}

