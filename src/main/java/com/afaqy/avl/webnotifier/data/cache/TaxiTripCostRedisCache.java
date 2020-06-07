package com.afaqy.avl.webnotifier.data.cache;


import com.afaqy.core.cache.redis.RedisCache;
import com.afaqy.core.cache.redis.RedisConnection;
import io.lettuce.core.api.StatefulRedisConnection;

/**
 * Name : TaxiTripCostRedisCache
 * <br>
 * Description :
 * <br>
 * Date : 23/01/2020
 * <br>
 * Create by : Mohamed Elkady
 * <br>
 * Mail : elkady@afaqy.com
 */
public class TaxiTripCostRedisCache extends RedisCache {

    public TaxiTripCostRedisCache(String prefix, RedisConnection redisConnection) {
        super(prefix, redisConnection);
    }
}
