/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.connections;

import com.afaqy.avl.core.model.*;
import com.afaqy.avl.core.model.vo.ChatMessageVO;
import com.afaqy.avl.webnotifier.model.DeviceConnectionStatus;
import com.afaqy.avl.webnotifier.model.WebSocketBindEvent;
import com.afaqy.avl.webnotifier.model.WebSocketsEvent;


import java.util.Set;

public interface UpdateListener {
    void onUpdateDeviceStatus(DeviceConnectionStatus status, String statusJson);

    void onUpdateEvent(WebSocketsEvent event);

    void onUpdateBindEvent(WebSocketBindEvent event);

    String getUserId();


    boolean isAdmin();

    void onUpdateChatMessage(ChatMessageVO chatMessageVO);

    void close();

    String getSocketChannelId();

    void onUpdateDevices(Set<String> imeiList);
}
