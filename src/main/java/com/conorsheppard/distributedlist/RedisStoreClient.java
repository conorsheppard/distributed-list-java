package com.conorsheppard.distributedlist;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisStoreClient implements StoreClient<String> {
    private final JedisPool jedisPool;

    public RedisStoreClient(String host, int port) {
        this.jedisPool = new JedisPool(host, port);
    }

    @Override
    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    @Override
    public void set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (value == null) jedis.del(key);
            else jedis.set(key, value);
        }
    }

    @Override
    public int incrementAndGet(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            long count = jedis.incr(key);
            if (count < Integer.MIN_VALUE || count > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Value " + count + " is out of int range");
            }
            return (int) count;
        }
    }

    public void close() {
        jedisPool.close();
    }
}
