package com.afaqy.avl.webnotifier.data.cache;


import com.afaqy.avl.core.data.cache.positon.LastPositionCache;
import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.core.cache.redis.RedisConnection;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Name : RedisChannelCache
 * <br>
 * Description : Represent redis connection for redis push/subscribe
 * <br>
 * Date : 31/01/2020
 * <br>
 * Create by : Mohamed Elkady
 * <br>
 * Mail : elkady@afaqy.com
 */
public class ChannelRedisCache extends LastPositionCache {
    private final static Logger LOGGER = LogManager.getLogger(ChannelRedisCache.class);
    private StatefulRedisPubSubConnection<String, String> pushSubConnection;
    private RedisConnection redisConnection;

    public ChannelRedisCache(RedisConnection redisConnection) {
        super(redisConnection);
        this.redisConnection = redisConnection;
        pushSubConnection = redisConnection.getRedisClient().connectPubSub();
    }

    /**
     * Post a message to a channel.
     *
     * @param channel the channel type: key
     * @param msg the message type: value
     * @return Long integer-reply the number of clients that received the message.
     */
    public RedisFuture<Long> publishAsync(String channel, String msg) {
        return pushSubConnection.async().publish(channel, msg);
    }

    /**
     * Close and release resources
     */
    public void close() {
        pushSubConnection.close();
        LOGGER.info(new LogMsg(WebNotifierConstants.CACHE, hashCode()+"", "ChannelRedisCache was closed"));
        redisConnection.shutdown();
    }
}
