package org.dsa.iot.redis.driver;

import org.dsa.iot.redis.model.RedisConfig;

import redis.clients.jedis.JedisPoolConfig;

public class RedisConnectionHelper {
   // private static String[] cashedDriversName;

    public static JedisPoolConfig configureDataSource(RedisConfig config) {
    	
    final JedisPoolConfig poolConfig = new JedisPoolConfig();
       poolConfig.setMaxIdle(128);
       poolConfig.setMinIdle(16);
       poolConfig.setTestOnBorrow(true);
       poolConfig.setTestOnReturn(true);
       poolConfig.setTestWhileIdle(true);
       poolConfig.setMinEvictableIdleTimeMillis(1800000);
       poolConfig.setTimeBetweenEvictionRunsMillis(1);
       poolConfig.setNumTestsPerEvictionRun(3);
       return poolConfig;
     }
}
