package com.afaqy.avl.webnotifier.data.cache;

import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.core.cache.redis.RedisCache;
import com.afaqy.core.cache.redis.RedisConnection;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;

/**
 * Name : AsyncSocketPositionCache
 * <br>
 * Description :
 * <br>
 * Date : 30/04/2020
 * <br>
 * Create by : Mohamed Elkady
 * <br>
 * Mail : elkady@afaqy.com
 */
public class AsyncSocketUnitsCache extends RedisCache {
    private StatefulRedisPubSubConnection<String, String> pushSubConnection;
    public static final String MODULE_NAME = "UNITS_SOCKET";
    private RedisPubSubListener listener;

    public AsyncSocketUnitsCache(RedisConnection redis) {
        super(MODULE_NAME, redis);
        init(redis);
    }

    public AsyncSocketUnitsCache(String environment, String serviceName, RedisConnection redis, boolean flush) {
        super(environment, serviceName, MODULE_NAME, redis, flush);
        init(redis);
    }

    private void init(RedisConnection redis) {
        pushSubConnection = redis.getRedisClient().connectPubSub();
    }

    /**
     * Subscribe to a channel asynchronously
     *
     * @param channel  redis channel name
     * @param listener redis push/sub listener
     */
    public void subscribeAsync(String channel, RedisPubSubListener listener) {
        if (listener != null) {
            this.listener = listener;
            pushSubConnection.addListener(listener);
            pushSubConnection.async().subscribe(channel);
        }
    }

    /**
     * Close and release resources
     */
    public void close() {
        if (listener != null) {
            pushSubConnection.removeListener(listener);
        }
        getLogger().info(new LogMsg(WebNotifierConstants.CACHE, hashCode() + "", "ChannelRedisCache was closed"));
        super.close();
    }

    public Long deleteAllUnits(String key) {
        return super.deleteContains(key);
    }
}
