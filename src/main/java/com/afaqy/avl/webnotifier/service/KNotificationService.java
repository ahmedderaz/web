/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.service;


import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.kafka.consumer.KNotificationConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


/**
 * Name: KNotificationService
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
public class KNotificationService implements ConsumerService {

    private final static Logger LOGGER = LogManager.getLogger(KNotificationService.class);
    private final int MAX_NUMBER_OF_CONSUMERS = 10;

    @Override
    public void startConsumer() {
        LOGGER.info(new LogMsg(WebNotifierConstants.KAFKA, "Starting New Notification Consumer."));

        if (WebNotifierContext.getInstance().getNotificationConsumerMap().size() < MAX_NUMBER_OF_CONSUMERS) {

            KNotificationConsumer kNotificationConsumer = new KNotificationConsumer(WebNotifierContext.getInstance().getLatch());
//            Thread thread = new Thread(kNotificationConsumer, "KNotification-Thread");
            Thread thread = new Thread(kNotificationConsumer);

            thread.start();
            WebNotifierContext.getInstance().getNotificationConsumerMap().put(thread, kNotificationConsumer);

            LOGGER.info(new LogMsg(WebNotifierConstants.KAFKA, "New Notification Consumer [" + thread.getName() + "] Started."));
        } else
            LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, " Notification Consumer Reached Max. [" + MAX_NUMBER_OF_CONSUMERS + "]"));
    }


    @Override
    public void stopRandomConsumer() throws InterruptedException {
        LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Stopping Random Notification Consumer."));

        Map<Thread, KNotificationConsumer> consumersMap = WebNotifierContext.getInstance().getNotificationConsumerMap();
        if (consumersMap.keySet().size() <= 1) return;
        else {
            Map.Entry<Thread, KNotificationConsumer> consumerEntry = consumersMap.entrySet().iterator().next();

            consumerEntry.getValue().shutdown();
            Thread.sleep(5000); //to make sure consumer is closed
            consumerEntry.getKey().interrupt();

            consumersMap.remove(consumerEntry.getKey());
            LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Notification Random Consumer [" + consumerEntry.getKey().getName() + "] Stopped."));
        }

    }


    @Override
    public int getConsumersCount() {
        return WebNotifierContext.getInstance().getNotificationConsumerMap().size();
    }

    @Override
    public void stopAllConsumers() {

        LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Stop All Notification Consumers."));

        WebNotifierContext.getInstance().getNotificationConsumerMap().forEach((key, value) -> {
            value.shutdown();
            try {
                Thread.sleep(5000); //to make sure consumer is closed
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.catching(e);
            }
            key.interrupt();

            WebNotifierContext.getInstance().getNotificationConsumerMap().remove(key);
            LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Notification Consumer [" + key.getName() + "] Stopped."));
        });

    }
}
