package redisdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author zhoubin
 * @create 2020/2/14
 * @since 1.0.0
 */
@Configuration
public class RedisConfig {

	//服务器地址
	@Value("${spring.redis.host}")
	private String host;
	//服务器端口
	@Value("${spring.redis.port}")
	private int port;
	//访问密码
	@Value("${spring.redis.password}")
	private String password;
	//连接超时时间
	@Value("${spring.redis.timeout}")
	private String timeout;
	//最大连接数
	@Value("${spring.redis.jedis.pool.max-active}")
	private int maxTotal;
	//最大连接阻塞等待时间
	@Value("${spring.redis.jedis.pool.max-wait}")
	private String maxWaitMillis;
	//最大空闲连接
	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;
	//最小空闲连接
	@Value("${spring.redis.jedis.pool.min-idle}")
	private int minIdle;



	@Bean
	public JedisPool getJedisPool(){
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		//最大连接数
		jedisPoolConfig.setMaxTotal(maxTotal);
		//最大连接阻塞等待时间
		jedisPoolConfig.setMaxWaitMillis(Long.valueOf(maxWaitMillis.substring(0,maxWaitMillis.length()-2)));
		//最大空闲连接
		jedisPoolConfig.setMaxIdle(maxIdle);
		//最小空闲连接
		jedisPoolConfig.setMinIdle(minIdle);
		JedisPool jedisPool = new JedisPool(jedisPoolConfig,host,port,Integer.valueOf(timeout.substring(0, timeout.length()-2)),password);
		return jedisPool;
	}

}