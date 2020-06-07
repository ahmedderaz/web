/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */
package com.afaqy.avl.webnotifier.connections;

import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.core.model.vo.ChatMessageVO;
import com.afaqy.avl.core.model.vo.UserVO;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.data.cache.AsyncSocketUnitsCache;
import com.afaqy.avl.webnotifier.model.*;
import com.afaqy.core.cache.redis.RedisConnection;
import com.afaqy.core.serialization.DateTimeDeserializer;
import com.afaqy.core.serialization.DateTimeSerializer;
import com.afaqy.core.utils.CollectionsUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lettuce.core.pubsub.RedisPubSubListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncSocket extends WebSocketAdapter implements UpdateListener, RedisPubSubListener<String, String> {

    private static final Logger LOGGER = LogManager.getLogger(AsyncSocket.class);

    private static final String KEY_UPDATES = "updates";
    private static final String KEY_STATUS = "status";
    private static final String KEY_EVENTS = "events";
    private static final String KEY_GRPRS = "gprs";
    private static final String KEY_CHAT = "chat";
    private String socketChannelId;
    private String userId;
    private Date loginDate = new Date();
    private long messageCount;
    private String remoteHostName;
    private boolean isAdmin;
    private boolean allUnits = true;
    private AsyncSocketUnitsCache asyncSocketUnitsCache;


    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
            .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
            .create();

    private Type msgType = new TypeToken<Map<String, WebSocketMessage>>() {
    }.getType();

    public AsyncSocket(UserVO user, boolean isAdmin, String redisUri) {
        this(user, isAdmin, new AsyncSocketUnitsCache(new RedisConnection(redisUri)));
    }

    public AsyncSocket(UserVO user, boolean isAdmin, AsyncSocketUnitsCache asyncSocketUnitsCache) {
        this.userId = user.getId();
        this.isAdmin = isAdmin;
        this.socketChannelId = new ObjectId().toString(); // getting random session id (i used mongodb id for simplicity)

        this.asyncSocketUnitsCache = asyncSocketUnitsCache;
        this.asyncSocketUnitsCache.subscribeAsync(userId, this);
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        remoteHostName = session.getRemoteAddress().getHostName();

        WebNotifierContext.getInstance().getConnectionManager().addListener(this);
        LOGGER.info(new LogMsg(String.format("Ur:[%s],IP:[%s]", userId, remoteHostName), "Connected"));
    }

    @Override
    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public void onUpdateChatMessage(ChatMessageVO chatMessageVO) {
        if (chatMessageVO != null) {
            Map<String, Object> result = new ConcurrentHashMap<>();
            result.put(chatMessageVO.getUnitId(), chatMessageVO);
            sendData(KEY_CHAT, result);
        }
    }

    @Override
    public void close() {
        if (getSession() != null) {
            getSession().close();
            LOGGER.info(new LogMsg(String.format("Ur:[%s], IP[%s]", userId, remoteHostName), "Session closed"));
        } else {
            LOGGER.info(new LogMsg(String.format("Ur:[%s], IP[%s]", userId, remoteHostName), "Session already closed"));
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        asyncSocketUnitsCache.deleteAllUnits(socketChannelId);
        asyncSocketUnitsCache.close();
        LOGGER.error(new LogMsg(String.format("Ur:[%s], IP[%s]", userId, remoteHostName), "Disconnected because of " + reason));
//        WebNotifierContext.getInstance().getConnectionManager().removeListener(this);
        close();
        super.onWebSocketClose(statusCode, reason);
        LOGGER.error(new LogMsg(String.format("Ur:[%s], IP[%s]", userId, remoteHostName),
                String.format("Disconnected because of [%s] with code [%d]", reason, statusCode)));
    }

    @Override
    public void onUpdateDeviceStatus(DeviceConnectionStatus status, String statusJson) {
        if (status != null) {
            sendData(KEY_STATUS, gson.toJson(status));
        }
//        LOGGER.info(new KeyMessage(KeyMessage.DEVICE_STATUS,
//                String.format("Ur:%s, U:%s", getUserId(), status.getUnitId()),
//                status.getStatus()));
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        LOGGER.catching(cause);
    }

    /**
     * @param eventJson websocket event json
     */
    @Override
    public void onUpdateEvent(WebSocketsEvent eventJson) {
        if (eventJson != null) {
            sendData(KEY_EVENTS, eventJson);
            LOGGER.info(new LogMsg(WebNotifierConstants.EVENT,
                    String.format("Ur:%s, E:%s", getUserId(), eventJson.getEventId())));
        }
    }

    /**
     * @param eventJson websocket event json
     */
    @Override
    public void onUpdateBindEvent(WebSocketBindEvent eventJson) {
        if (eventJson != null) {
            sendData(eventJson.getType(), eventJson);
            LOGGER.info(new LogMsg(WebNotifierConstants.EVENT,
                    String.format("Ur:%s, Dr:%s", getUserId(), eventJson.getId())));
        }
    }

    /**
     * converts message from Json to WebSocketRequest model
     * and change value of allUnits depend on request
     * and handle redis process depend on request
     * request is {
     * "action":"ADD", // "ADD" | "REMOVE" | "ADDALL" | "REMOVEALL" // mandatory
     * "units" : [ // optional
     * "id1","id2","id3"
     * ]
     * }
     *
     * @param message message will be sent from web with actions
     */
    @Override
    public void onWebSocketText(String message) {
        try {
            LOGGER.debug("CONTROL Received {}", message);
            super.onWebSocketText(message);
            WebSocketRequest webSocketRq;
            if (message != null) {
                webSocketRq = gson.fromJson(message, WebSocketRequest.class);
                allUnits = false;
                switch (webSocketRq.getAction()) {
                    case "ADD":
                        addUnitsToRedis(webSocketRq.getUnits());
                        break;
                    case "REMOVE":
                        removeUnitsFromRedis(webSocketRq.getUnits());
                        break;
                    case "REMOVEALL":
                        asyncSocketUnitsCache.deleteAllUnits(socketChannelId);
                        break;
                    case "ADDALL":
                        allUnits = true;
                        asyncSocketUnitsCache.deleteAllUnits(socketChannelId);
                        break;
                }
            }
        } catch (Exception exc) {
            LOGGER.error("Invalid request  " + message);
            LOGGER.catching(exc);
            allUnits = true;
        }

    }

    /**
     * add all units to redis cash to be notified when web request need udated units
     *
     * @param unitIds all unit will be notified to web and added to redis
     */
    private void addUnitsToRedis(List<String> unitIds) {
        if (CollectionsUtil.isNotBlank(unitIds)) {
            for (String unitId : unitIds) {
                asyncSocketUnitsCache.set(socketChannelId + "_" + unitId, "true");
            }
        }
    }

    /**
     * remove unit ids from redis to not be notified in web
     *
     * @param unitIds ids of units  that came from web request
     *                and will concatenate to id of socket as it saved in redis (socketId_unitID)
     */
    private void removeUnitsFromRedis(List<String> unitIds) {
        if (CollectionsUtil.isNotBlank(unitIds)) {
            for (String unitId : unitIds) {
                asyncSocketUnitsCache.delete(socketChannelId + "_" + unitId);
            }
        }
    }

    private boolean sendData(String type, Object rs) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("type", type);
            data.put("data", rs);


            if (isConnected() && getRemote() != null && rs != null) {
                String jsonMessage = gson.toJson(data);
                getRemote().sendString(jsonMessage, new WriteCallback() {
                    @Override
                    public void writeFailed(Throwable x) {
                        LOGGER.catching(x);
                    }

                    @Override
                    public void writeSuccess() {
                        messageCount++;
                    }
                });
            }
            return true;
        } catch (Exception ex) {
            LOGGER.catching(ex);
        }
        return false;
    }

    public long getMessageCount() {
        return messageCount;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getSocketChannelId() {
        return socketChannelId;
    }

    @Override
    public void onUpdateDevices(Set<String> imeiList) {
    }

    /**
     * send units to be updated to jetty server if allUnits==true
     * else update units that in redis and its value true
     *
     * @param channel channel name of websocket connection
     * @param message all units need to be updated
     */
    @Override
    public void message(String channel, String message) {
        try {
            if (message != null) {
                Map<String, WebSocketMessage> map = gson.fromJson(message, msgType);
                if (!allUnits) {
                    map = removeIgnoredUnitsFromRedis(map);
                }
                if (map.size() > 0) {
                    sendData(KEY_UPDATES, map);
                    LOGGER.info("Ur[{}] updated with batch size [{}] ", channel, map.size());
                }
            }
        } catch (Exception ex) {
            LOGGER.catching(ex);
        }
    }

    /**
            * remove all unit (not found in redis || its value  is false )form request
     *
             * @param map containt all units need to be validated came from request message
     * @return map after ignoring units not be in redis
     */
    public Map<String, WebSocketMessage> removeIgnoredUnitsFromRedis(Map<String, WebSocketMessage> map) {
        Iterator<Map.Entry<String, WebSocketMessage>> itr = map.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, WebSocketMessage> entry = itr.next();
            String enabled = asyncSocketUnitsCache.get(socketChannelId + "_" + entry.getKey());
            if (enabled == null || !enabled.equals("true"))
                itr.remove();
        }
        return map;
    }

    @Override
    public void message(String pattern, String channel, String message) {

    }

    @Override
    public void subscribed(String channel, long count) {
        LOGGER.debug("Subscribed to channel {} with count {}", channel, count);
    }

    @Override
    public void psubscribed(String pattern, long count) {
        LOGGER.debug("PSubscribed to pattern {} with count {}", pattern, count);
    }

    @Override
    public void unsubscribed(String channel, long count) {
        LOGGER.debug("Unsubscribed to channel {} with count {}", channel, count);

    }

    @Override
    public void punsubscribed(String pattern, long count) {
        LOGGER.debug("PUnSubscribed to pattern {} with count {}", pattern, count);
    }

    public boolean isAllUnits() {
        return allUnits;
    }

    public void setAllUnits(boolean allUnits) {
        this.allUnits = allUnits;
    }
}
