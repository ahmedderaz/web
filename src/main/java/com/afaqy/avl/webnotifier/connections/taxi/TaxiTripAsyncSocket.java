package com.afaqy.avl.webnotifier.connections.taxi;

import com.afaqy.avl.core.helper.log.LogMsg;

import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.data.cache.AsyncSocketUnitsCache;
import com.afaqy.core.cache.redis.RedisConnection;
import io.lettuce.core.pubsub.RedisPubSubListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.joda.time.DateTime;



/**
 * Name : TaxiTripAsyncSocket
 * <br>
 * Description :
 * <br>
 * Date : 14/11/2019
 * <br>
 * Create by : Mohamed Elkady
 * <br>
 * Mail : elkady@afaqy.com
 */
public class TaxiTripAsyncSocket extends WebSocketAdapter implements RedisPubSubListener<String, String> {
    private static final Logger LOGGER = LogManager.getLogger(TaxiTripAsyncSocket.class);
    private String unitId;
    public static final String TAXI_TRIP_SOCKET_PREFIX = "TAXI_TRIP:";
    private DateTime lastUpdateTime = DateTime.now();
    private AsyncSocketUnitsCache asyncSocketUnitsCache;

    public TaxiTripAsyncSocket(String unitId) {
        this.unitId = unitId;
        RedisConnection redisConnection = new RedisConnection(WebNotifierContext.getInstance().getConfig().getRedisHost());
        asyncSocketUnitsCache = new AsyncSocketUnitsCache(redisConnection);
        asyncSocketUnitsCache.subscribeAsync(this.unitId, this);
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        sendMessage((String) asyncSocketUnitsCache.get(redisKey(unitId)));
        LOGGER.info(new LogMsg(String.format("Ut:[%s],IP:[%s]", unitId, session.getRemoteAddress().getAddress()),
                "Connected"));
    }

    public static String redisKey(String unitId) {
        return TAXI_TRIP_SOCKET_PREFIX + unitId;
    }


    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        asyncSocketUnitsCache.close();
        LOGGER.warn(new LogMsg(WebNotifierConstants.WEB, String.format("Ut:[%s]", unitId),
                "WebSocket Closed because of : " + reason + " code :" + statusCode));
        super.onWebSocketClose(statusCode, reason);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        String returnMsg = "";
        switch (message) {
            case "FETCH":
                returnMsg = (String) asyncSocketUnitsCache.get(redisKey(unitId));
                LOGGER.debug("redis key :{}", asyncSocketUnitsCache.get(redisKey(unitId)));
                break;
            case "LAST_TIME":
                returnMsg = lastUpdateTime.toString();
                break;
        }

        LOGGER.debug(new LogMsg(WebNotifierConstants.WEB, String.format("Ut:[%s]", unitId),
                "WebSocket Received : " + message));
        LOGGER.debug(new LogMsg(WebNotifierConstants.WEB, String.format("Ut:[%s]", unitId),
                "WebSocket Replay : " + returnMsg));
        sendMessage(returnMsg);
    }

    @Override
    public void message(String channel, String tripJson) {
        sendMessage(tripJson);
    }

    public void sendMessage(String msg) {
        try {
            if (msg != null) {
                getRemote().sendString(msg);
                lastUpdateTime = DateTime.now();
                LOGGER.info(new LogMsg(WebNotifierConstants.WEB, String.format("Ut:[%s]", unitId), msg));
            }
        } catch (Exception e) {
            LOGGER.catching(e);
            getSession().close();
        }
    }

    @Override
    public void message(String pattern, String channel, String message) {

    }

    @Override
    public void subscribed(String channel, long count) {

    }

    @Override
    public void psubscribed(String pattern, long count) {

    }

    @Override
    public void unsubscribed(String channel, long count) {

    }

    @Override
    public void punsubscribed(String pattern, long count) {

    }
}
