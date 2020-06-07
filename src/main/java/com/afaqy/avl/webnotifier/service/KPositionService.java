/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.service;

import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.webnotifier.WebNotifierConstants;

import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.kafka.consumer.KPositionConsumer;
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
public class KPositionService implements ConsumerService {
    private final static Logger LOGGER = LogManager.getLogger(KPositionService.class);
    private final int MAX_NUMBER_OF_CONSUMERS = 10;

    @Override
    public void startConsumer() {

        LOGGER.info(new LogMsg(WebNotifierConstants.KAFKA, "Starting New Position Consumer."));

        if (WebNotifierContext.getInstance().getPositionConsumerMap().size() < MAX_NUMBER_OF_CONSUMERS) {
            KPositionConsumer consumer = new KPositionConsumer(WebNotifierContext.getInstance().getLatch());

//            Thread thread = new Thread(consumer, "KPosition-Th");
            Thread thread = new Thread(consumer);

            thread.start();
            WebNotifierContext.getInstance().getPositionConsumerMap().put(thread, consumer);

            LOGGER.info(new LogMsg(WebNotifierConstants.KAFKA, "New Position Consumer [" + thread.getName() + "] Started."));
        } else
            LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Position Consumer Reached Max. [" + MAX_NUMBER_OF_CONSUMERS + "]"));
    }

    @Override
    public void stopRandomConsumer() throws InterruptedException {

        LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Stopping Random Position Consumer."));

        Map<Thread, KPositionConsumer> consumersMap = WebNotifierContext.getInstance().getPositionConsumerMap();

        if (consumersMap.keySet().size() <= 1) return;

        else {
            Map.Entry<Thread, KPositionConsumer> consumerEntry = consumersMap.entrySet().iterator().next();

            consumerEntry.getValue().shutdown();
            Thread.sleep(5000); //to make sure consumer is closed
            consumerEntry.getKey().interrupt();

            WebNotifierContext.getInstance().getPositionConsumerMap().remove(consumerEntry.getKey());

            LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Random Position Consumer [" + consumerEntry.getKey().getName() + "] Stopped."));
        }
    }

    @Override
    public int getConsumersCount() {
        return WebNotifierContext.getInstance().getPositionConsumerMap().size();
    }

    @Override
    public void stopAllConsumers() {

        LOGGER.warn(new LogMsg(WebNotifierConstants.KAFKA, "Stop all Position Consumers"));

        WebNotifierContext.getInstance().getPositionConsumerMap().forEach((key, value) -> {
            value.shutdown();
            try {
                Thread.sleep(5000); //to make sure consumer is closed
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.catching(e);
            }
            key.interrupt();

            WebNotifierContext.getInstance().getPositionConsumerMap().remove(key);
            LOGGER.info(new LogMsg(WebNotifierConstants.KAFKA, "Position Consumer [" + key.getName() + "] Stopped."));
        });
    }
}
