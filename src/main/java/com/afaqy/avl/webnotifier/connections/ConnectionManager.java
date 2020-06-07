package com.afaqy.avl.webnotifier.connections;
import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.model.DeviceConnectionStatus;
import com.afaqy.avl.webnotifier.model.WebSocketBindEvent;
import com.afaqy.avl.webnotifier.model.WebSocketsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private static final Logger LOGGER = LogManager.getLogger(ConnectionManager.class);

    private final ConcurrentHashMap<String, Set<UpdateListener>> normalListenersMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AsyncSocket> adminsMap = new ConcurrentHashMap<>();

    private volatile long listenersCount = 0;

    public ConnectionManager() {
    }

    public ConcurrentHashMap<String, Set<UpdateListener>> getNormalListenersMap() {
        return normalListenersMap;
    }

    public ConcurrentHashMap<String, AsyncSocket> getAdminsMap() {
        return adminsMap;
    }

    /**
     * @param users users to be notified
     * @param event websocket event
     */
    public synchronized void updateEvent(List<String> users, WebSocketsEvent event) {
        try {
            for (String userId : users) {
                if (normalListenersMap.containsKey(userId)) {
                    for (UpdateListener listener : normalListenersMap.get(userId)) {
                        listener.onUpdateEvent(event);
                    }
                }
            }
            adminsMap.values().forEach(x -> x.onUpdateEvent(event));
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.catching(ex);
        }
    }

    /**
     * @param users users to be notified
     * @param event websocket event
     */
    public synchronized void updateBindEvent(List<String> users, WebSocketBindEvent event) {
        try {
            for (String userId : users) {
                if (normalListenersMap.containsKey(userId)) {
                    for (UpdateListener listener : normalListenersMap.get(userId)) {
                        listener.onUpdateBindEvent(event);
                    }
                }
            }
            adminsMap.values().forEach(x -> x.onUpdateBindEvent(event));
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.catching(ex);
        }
    }

    /**
     * add new listener check if admin ad to admin map, else listeners map
     *
     * @param listener socket listener
     */
    public synchronized void addListener(UpdateListener listener) {
        try {

            if (listener.isAdmin()) {
                // make sure that user is admin
                AsyncSocket socket = (AsyncSocket) listener;
                adminsMap.put(socket.getSocketChannelId(), socket);
                listenersCount++;
            } else {
                if (!normalListenersMap.containsKey(listener.getUserId())) {
                    normalListenersMap.put(listener.getUserId(), new HashSet<>());
                }
                normalListenersMap.get(listener.getUserId()).add(listener);

                listenersCount++;
            }
            LOGGER.info(new LogMsg(WebNotifierConstants.WEB, listener.getUserId(), String.format("New listener connected, admin=%s,T[%d]", listener.isAdmin() + "", listenersCount)));
        } catch (Exception ex) {
            LOGGER.catching(ex);
        }

    }

    /**
     * Close specific socket listener
     *
     * @param listener socket listener
     */
    public synchronized void closeListener(UpdateListener listener) {
        AsyncSocket socket = (AsyncSocket) listener;
        socket.close();
        removeListener(listener);
        LOGGER.info(new LogMsg(WebNotifierConstants.WEB, listener.getUserId(), String.format("Listener removed, admin=%s,T[%d]", listener.isAdmin() + "", listenersCount)));
    }

    /**
     * remove specific socket listener
     *
     * @param listener socket listener
     */
    public synchronized void removeListener(UpdateListener listener) {
        if (listener.isAdmin()) {
            AsyncSocket socket = (AsyncSocket) listener;
            adminsMap.remove(socket.getSocketChannelId());
            listenersCount--;
        } else {
            Set<UpdateListener> listeners = normalListenersMap.get(listener.getUserId());
            if (listeners != null) {
                listeners.remove(listener);
                listenersCount--;
            }
        }
        LOGGER.info(new LogMsg(WebNotifierConstants.WEB, listener.getUserId(), String.format("Listener removed, admin=%s,T[%d]", listener.isAdmin() + "", listenersCount)));
    }

    /**
     * delete all sockets
     */
    public synchronized void removeAllListener() {
        for (UpdateListener listener : getListeners()) {
            listener.close();
        }
        normalListenersMap.clear();
        adminsMap.clear();
        listenersCount = 0;
    }

    public Set<UpdateListener> getListeners() {
        Set<UpdateListener> listeners = new HashSet<>();
        normalListenersMap.values().forEach(listeners::addAll);
        listeners.addAll(adminsMap.values());
        return listeners;
    }

    public Map<String, Integer> getListenersCount() {
        int adminMapSize = adminsMap.size();
        int normalListenersSize = getNormalListenersSize();

        Map<String, Integer> result = new HashMap();

        result.put("admin", adminMapSize);
        result.put("normal", normalListenersSize);
        result.put("total", (adminMapSize + normalListenersSize));
        return result;
    }

    public int getNormalListenersSize() {
        int normalListenersSize = 0;
        Collection<Set<UpdateListener>> sets = normalListenersMap.values();

        for (Set<UpdateListener> itm : sets) {
            normalListenersSize += itm.size();
        }
        return normalListenersSize;
    }

    public void updateDeviceStatus(DeviceConnectionStatus status, String statusJson) {
        // just update associated user to an unit's update
        try {
            if (status.getUsers() != null) {
                for (String userId : status.getUsers()) {
                    if (normalListenersMap.containsKey(userId)) {
                        for (UpdateListener listener : normalListenersMap.get(userId)) {
                            listener.onUpdateDeviceStatus(status, statusJson);
                        }
                    }
                }
                adminsMap.values().forEach(x -> x.onUpdateDeviceStatus(status, statusJson));
            }
        }catch (Exception ex){
            LOGGER.catching(ex);
        }

    }

}
