/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.kafka.consumer;

import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.core.kafka.consumer.AbstractKafkaConsumer;
import static com.afaqy.avl.core.helper.AvlConstants.KConsumer.KAFKA_NOTIFICATION_CONSUMER;


import com.afaqy.avl.core.model.NotificationRecord;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.handlers.WebNotificationHandler;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




/**
 * Name: KNotificationConsumer
 * <br>
 * Description:
 * <br>
 * <br>
 *
 * Created by : Mohammed ElAdly
 * <br>
 * <br>
 * Mail : mohammed.eladly@afaqy.com
 * <br>
 */
public class KNotificationConsumer extends AbstractKafkaConsumer<NotificationRecord> {

    private final int NUMBER_OF_THREADS = 10;
    private final int CHUNK_SIZE = 20;

    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    /**
     * pass configuration to superClass, init. Notification Handler
     *
     * @param latch count down latch
     */
    public KNotificationConsumer(CountDownLatch latch) {
        super(WebNotifierContext.getInstance().getConfig().getKNotificationConsumerProperties(0),
                Arrays.asList(
                        WebNotifierContext.getInstance().getConfig()
                                .getKafkaTopicName(KAFKA_NOTIFICATION_CONSUMER,
                                        0)),
                latch, null);

        getLogger().info(new LogMsg(WebNotifierConstants.NOTIFICATION_CONSUMER, getGroupId(),
                "KNotificationConsumer  initialized successfully."));
    }

    @Override
    protected void processRecord(NotificationRecord record) {

        WebNotifierConstants.NOTIFICATION_TOTAL_CONSUMED.incrementAndGet();
        if (record.getRecordType() == NotificationRecord.AUTO_BIND) {
            getLogger().debug(record.toString());
        }
    }

    /**
     * handle notification after polling messages from kafka,
     * split records to N-Partitions based on Fixed chunk size,
     * then submit to ExecutorService,
     * if N-Partitions greater than threads in the pool,
     * new submitted will added to queue until any active thread finish then use it.
     *
     * @param resultList notification list
     */
    @Override
    protected void doAfterProcessingRecords(List<NotificationRecord> resultList) {

        getLogger().info(new LogMsg(WebNotifierConstants.NOTIFICATION_CONSUMER, getGroupId(), String.format("Poll/TOTAL : [%d/%s]", resultList.size(),
                WebNotifierConstants.NOTIFICATION_TOTAL_CONSUMED.get())));


        List<List<NotificationRecord>> partitions = Lists.partition(resultList, CHUNK_SIZE);

        partitions.forEach(sublist -> {

            Thread handlerThread = new Thread(() -> new WebNotificationHandler().handleNotifications(sublist));

            executorService.execute(handlerThread);
        });
    }

    @Override
    protected void handleEmptyPoll() {
    }

}
