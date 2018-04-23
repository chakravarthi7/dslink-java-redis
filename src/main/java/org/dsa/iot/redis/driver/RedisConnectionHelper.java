package org.dsa.iot.redis.driver;

import org.dsa.iot.redis.model.RedisConfig;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnectionHelper {
   // private static String[] cashedDriversName;

    public static JedisPoolConfig configureDataSource(RedisConfig config) {
    	
    	
         
         
     final JedisPoolConfig poolConfig = new JedisPoolConfig();
   
  
     //  poolConfig.setMaxTotal(128);
       poolConfig.setMaxIdle(128);
       poolConfig.setMinIdle(16);
       poolConfig.setTestOnBorrow(true);
       poolConfig.setTestOnReturn(true);
       poolConfig.setTestWhileIdle(true);
       poolConfig.setMinEvictableIdleTimeMillis(1800000);
       poolConfig.setTimeBetweenEvictionRunsMillis(1);
       poolConfig.setNumTestsPerEvictionRun(3);
   
    //   poolConfig.setBlockWhenExhausted(true);
      
       JedisPool jedisPool = new JedisPool(poolConfig, config.getUrl());
    //   Jedis jedis = jedisPool.getResource();
       System.out.println(jedisPool.getResource().ping());
       return poolConfig;
    
        
/*        
        
        
        
        
        
        
        
        BasicDataSource dataSource = new BasicDataSource();
        // dataSource.setDriverClassName("com.mysql.jdbc.Driver");
     //   dataSource.setDriverClassName(config.getDriverName());
        // dataSource.setUrl(jdbc:mysql://127.0.0.1:3306);
       
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUser());
        dataSource.setPassword(String.valueOf(config.getPassword()));
        dataSource.setInitialSize(3);
        dataSource.setMaxIdle(10);
        dataSource.setMinIdle(1);
        dataSource.setMaxOpenPreparedStatements(20);
        dataSource.setTestWhileIdle(false);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setTimeBetweenEvictionRunsMillis(1);
        dataSource.setNumTestsPerEvictionRun(50);
        dataSource.setMinEvictableIdleTimeMillis(1800000);
        return dataSource;*/
    }

  /*  public static String[] getRegisteredDrivers() {
        if (cashedDriversName == null) {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            Set<String> set = new HashSet<>();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                // skip MySQL fabric
                if (!driver.getClass().getName().contains("fabric")) {
                    set.add(driver.getClass().getName());
                }
            }
            cashedDriversName = set.toArray(new String[set.size()]);
        }
        return cashedDriversName.clone();
    }*/
}
