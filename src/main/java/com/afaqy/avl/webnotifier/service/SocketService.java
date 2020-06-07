package com.afaqy.avl.webnotifier.service;

import com.afaqy.avl.webnotifier.connections.AsyncSocket;
import com.afaqy.avl.webnotifier.connections.ConnectionManager;
import com.afaqy.avl.webnotifier.connections.UpdateListener;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Name: SocketService
 * <br>
 * Description:
 * <br>
 *
 * Create by Mohammed ElAdly
 * <br>
 * Mail : mohammed.eladly@afaqy.com
 * <br>
 */
public class SocketService {
    private ConnectionManager connectionManager;

    public SocketService(ConnectionManager connectionManage) {
        this.connectionManager = connectionManage;
    }

    /**
     * @return list of all connected sockets
     */
    public ArrayList getOnlineSockets() {
        ArrayList rsList = new ArrayList();

        for (UpdateListener listener : connectionManager.getListeners()) {
            rsList.add(prepareSocketAsMap((AsyncSocket) listener));
        }
        return rsList;
    }

    /**
     * formatting sockets in map
     *
     * @param listener
     * @return
     */
    private HashMap<String, Object> prepareSocketAsMap(AsyncSocket listener) {
        HashMap<String, Object> map = new HashMap<>();

        AsyncSocket asyncSocket = listener;

        map.put("userId", asyncSocket.getUserId());
        map.put("admin", asyncSocket.isAdmin());
        map.put("messageCount", asyncSocket.getMessageCount());
        map.put("connected", asyncSocket.isConnected());
        map.put("id", asyncSocket.getSocketChannelId());
        map.put("loginDate", asyncSocket.getLoginDate());
        map.put("host", asyncSocket.getSession().getRemoteAddress().getHostString());
        map.put("port", asyncSocket.getSession().getRemoteAddress().getPort());
        return map;
    }

    /**
     * removeUnit socket from listeners map
     *
     * @param id socket id
     * @return boolean
     */
    public boolean closeSocket(String id) {
        boolean found = false;
        for (UpdateListener listener : connectionManager.getListeners()) {
            if (id.equals(listener.getSocketChannelId())) {
                WebNotifierContext.getInstance().getConnectionManager().closeListener(listener);
                found = true;
                break;
            }
        }
        return found;
    }


    /**
     * @return msg of number of removed sockets
     */
    public String removeAllSockets() {
        Map<String, Integer> mapCount = connectionManager.getListenersCount();
        connectionManager.removeAllListener();

        return String.format("All Sockets [%d] closed", mapCount.get("total"));
    }

    public Map<String, Integer> getOnlineSocketsCount() {
        return connectionManager.getListenersCount();
    }
}
