/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.service;

import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.webnotifier.WebNotifierConstants;

import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.kafka.consumer.KDeviceStatusConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


/**
 * Name: KPositionService
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
public class KDeviceStatusService implements ConsumerService {
    private final static Logger LOGGER = LogManager.getLogger(KDeviceStatusService.class);
    private final int MAX_NUMBER_OF_CONSUMERS = 10;

    @Override
    public void startConsumer() {

        LOGGER.info(new LogMsg(WebNotifierConstants.KAFKA, "Starting New Device Status Consumer."));

        if (WebNotifierContext.getInstance().getDeviceStatusConsumerMap().size() < MAX_NUMBER_OF_CONSUMERS) {
            KDeviceStatusConsumer consumer = new KDeviceStatusConsumer(WebNotifierContext.getInstance().getLatch());

            Thread thread = new Thread(consumer);

            thread.start();

            WebNotifierContext.getInstance().getDeviceStatusConsumerMap().put(thread, consumer);

            LOGGER.info(new LogMsg(WebNotifierConstants.KAFKA, "New Device Status Consumer [" + thread.getName() + "] Started."));
        } else
            LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Device Status Consumer Reached Max. [" + MAX_NUMBER_OF_CONSUMERS + "]"));
    }

    @Override
    public void stopRandomConsumer() throws InterruptedException {

        LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Stopping Random Device Status Consumer."));

        Map<Thread, KDeviceStatusConsumer> consumersMap = WebNotifierContext.getInstance().getDeviceStatusConsumerMap();

        if (consumersMap.keySet().size() <= 1) return;

        else {
            Map.Entry<Thread, KDeviceStatusConsumer> consumerEntry = consumersMap.entrySet().iterator().next();

            consumerEntry.getValue().shutdown();
            Thread.sleep(5000); //to make sure consumer is closed
            consumerEntry.getKey().interrupt();

            WebNotifierContext.getInstance().getDeviceStatusConsumerMap().remove(consumerEntry.getKey());

            LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Random Device Status Consumer [" + consumerEntry.getKey().getName() + "] Stopped."));
        }
    }

    @Override
    public int getConsumersCount() {
        return WebNotifierContext.getInstance().getDeviceStatusConsumerMap().size();
    }

    @Override
    public void stopAllConsumers() {

        LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Stop all Device Status Consumers"));

        WebNotifierContext.getInstance().getDeviceStatusConsumerMap().forEach((key, value) -> {
            value.shutdown();
            try {
                Thread.sleep(5000); //to make sure consumer is closed
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.catching(e);
            }
            key.interrupt();

            WebNotifierContext.getInstance().getDeviceStatusConsumerMap().remove(key);
            LOGGER.info(new LogMsg(WebNotifierConstants.KAFKA, "Position Consumer [" + key.getName() + "] Stopped."));
        });
    }
}
