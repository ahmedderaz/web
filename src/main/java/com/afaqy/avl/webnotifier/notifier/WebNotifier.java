/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.notifier;

import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.core.model.NotificationRecord;
import com.afaqy.avl.core.notifier.NotificationCallback;
import com.afaqy.avl.core.notifier.Notifier;
import com.afaqy.avl.core.serialization.JsonConverter;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.model.WebSocketBindEvent;
import com.afaqy.avl.webnotifier.model.WebSocketsEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Name: WebNotifier
 * <br>
 * Description:
 * <br>
 * <br>
 *
 * @author Mohammed ElAdly
 * <br>
 * <br>
 * Mail : mohammed.eladly@afaqy.com
 * <br>
 */
public class WebNotifier implements Notifier {
    private static final Logger LOGGER = LogManager.getLogger(WebNotifier.class);

    @Override
    public void notify(NotificationRecord notificationRecord, NotificationCallback callback) {
        try {
            //todo: extraData if auto_bind will use WebSocketBindEvent
            switch (notificationRecord.getRecordType()) {
                case NotificationRecord.WEB_RECORD:
                    WebSocketsEvent event = JsonConverter.fromJson(notificationRecord.getMessage(), WebSocketsEvent.class);

                    WebNotifierContext.getInstance().getConnectionManager().updateEvent(notificationRecord.getRecipients(), event);

                    LOGGER.info(new LogMsg(WebNotifierConstants.WEB, String.format("Ut:%s, E:%s", event.getUnitId(), event.getEventId()),
                            event.getEventDesc()));
                    break;

                case NotificationRecord.AUTO_BIND:
                    WebSocketBindEvent autoBindEvent = JsonConverter.fromJson(notificationRecord.getMessage(), WebSocketBindEvent.class);


                    WebNotifierContext.getInstance().getConnectionManager().updateBindEvent(notificationRecord.getRecipients(), autoBindEvent);

                    LOGGER.info(new LogMsg(WebNotifierConstants.WEB, String.format("Ut:%s, Dr:%s", autoBindEvent.getUnitId(), autoBindEvent.getId())));
                    break;
            }


        } catch (Exception ex) {
            callback.callback(notificationRecord, ex);
        }
    }
}
