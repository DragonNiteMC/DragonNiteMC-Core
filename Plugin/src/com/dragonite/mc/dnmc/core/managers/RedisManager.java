package com.dragonite.mc.dnmc.core.managers;

import com.dragonite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonite.mc.dnmc.core.config.implement.yaml.DatabaseConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.inject.Inject;

public class RedisManager implements RedisDataSource {

    private static JedisPool jedisPool;

    @Inject
    public RedisManager(CoreConfig coreConfig) {
        DatabaseConfig.RedisInfo info = ((DNMCoreConfig) coreConfig).getDatabase().redis;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(30);
        config.setMaxTotal(100);
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(false);
        config.setTestOnBorrow(false);
        config.setTestWhileIdle(true);
        if (info.usePassword) {
            jedisPool = new JedisPool(config, info.ip, info.port, info.timeout * 1000, info.password);
        } else {
            jedisPool = new JedisPool(config, info.ip, info.port, info.timeout * 1000);
        }
    }

    @Override
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    @Override
    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
