/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.kafka.consumer;

import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.core.kafka.consumer.AbstractKafkaConsumer;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.model.DeviceConnectionStatus;



import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * Name: KPositionConsumer
 * <br>
 * Description:
 * <br>
 * <br>
 *
 * Created by: Mohammed ElAdly
 * <br>
 * <br>
 * Mail : mohammed.eladly@afaqy.com
 * <br>
 */
public class KDeviceStatusConsumer extends AbstractKafkaConsumer<String> {

    private Gson gson = new Gson();

    /**
     * pass configuration to superClass, init. Position Handler
     *
     * @param latch count down latch
     */
    public KDeviceStatusConsumer(CountDownLatch latch) {
        super(WebNotifierContext.getInstance().getConfig().getKDeviceStatusConsumerProperties(0),
                Arrays.asList("avlv2_position_server_status1"), latch, null);

        getLogger().info(new LogMsg(getGroupId(),
                "KDeviceStatusConsumer initialized successfully."));
    }


    @Override
    protected void handleEmptyPoll() {
       getLogger().info(new LogMsg(getGroupId(),
                "KDeviceStatusConsumer --> Empty Poll"));
    }

    @Override
    protected void processRecord(String record) {
        DeviceConnectionStatus status = gson.fromJson(record, DeviceConnectionStatus.class);

        WebNotifierContext.getInstance().getConnectionManager().updateDeviceStatus(status, record);

        getLogger().info(new LogMsg(getGroupId(),
                String.format("Consumed [Ut:%s][%s], Count[%s]", status.getUnitId(),
                        status.getStatus().toUpperCase(),
                        WebNotifierConstants.TOTAL_CONSUMED_DEVICE_STATUS.incrementAndGet().toString())));
    }

    /**
     * handle positions after polling messages from kafka
     *
     * @param resultList result list
     */
    @Override
    protected void doAfterProcessingRecords(List<String> resultList) {
        getLogger().info(new LogMsg(getGroupId(), String.format("Poll/TOTAL : [%d/%s]", resultList.size(),
                WebNotifierConstants.TOTAL_CONSUMED_DEVICE_STATUS.get())));

    }
}
