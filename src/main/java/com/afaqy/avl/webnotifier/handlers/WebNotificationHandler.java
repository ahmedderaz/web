/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.handlers;


import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.core.model.NotificationRecord;
import com.afaqy.avl.core.notifier.NotificationHandler;
import com.afaqy.avl.core.notifier.Notifier;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.notifier.WebNotifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Name : WebNotificationHandler
 * Description :
 * Date : 12/09/2019
 * Create by : Mohammed ElAdly
 * E-Mail : mohammed.eladly@afaqy.com
 */

public class WebNotificationHandler implements NotificationHandler {
    private static final Logger LOGGER = LogManager.getLogger(WebNotificationHandler.class);
    private Notifier webNotifier;
    private boolean enabled;

    public WebNotificationHandler() {
        this.webNotifier = new WebNotifier();
        enabled = WebNotifierContext.getInstance().getConfig().getConfiguration().getBoolean(WebNotifierConstants.Notifier.ENABLE_WEB);
    }

    @Override
    public synchronized void handleNotifications(List<NotificationRecord> records) {

        LOGGER.info(new LogMsg(WebNotifierConstants.WEB, "Handle Notification Batch Size [" + records.size() + "]"));

        if (enabled) {

            for (NotificationRecord record : records) {

                if (record.getRecordType() == NotificationRecord.WEB_RECORD || record.getRecordType() == NotificationRecord.AUTO_BIND) {
                    webNotifier.notify(record, this);
                }
            }
        }
    }

    @Override
    public void logDisabledNotifier(NotificationRecord record) {
        LOGGER.warn(new LogMsg(WebNotifierConstants.WEB_NOTIFICATION,
                String.format("Can't send message %s, type %s is disabled.", record.getMessage(), record.getRecordType())));
    }

    @Override
    public void callback(NotificationRecord record, Exception exception) {
        if (exception != null) {
            LOGGER.error(new LogMsg(WebNotifierConstants.WEB_NOTIFICATION, String.format("Couldn't send notification %s due to ",
                    record)), exception);
        }
    }
}
