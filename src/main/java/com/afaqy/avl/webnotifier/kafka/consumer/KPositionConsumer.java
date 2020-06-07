/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.kafka.consumer;

import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.core.kafka.consumer.AbstractKafkaConsumer;
import com.afaqy.avl.core.model.vo.PositionVO;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.data.cache.ChannelRedisCache;
import com.afaqy.avl.webnotifier.model.WebSocketMessage;
import com.afaqy.core.model.Tuple;
import com.afaqy.core.serialization.DateTimeDeserializer;
import com.afaqy.core.serialization.DateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lettuce.core.RedisFuture;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


/**
 * Name: KPositionConsumer
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
public class KPositionConsumer extends AbstractKafkaConsumer<Tuple<PositionVO, PositionVO>> {

    private ChannelRedisCache channelRedisCache;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
            .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
            .serializeSpecialFloatingPointValues()
            .create();
    Map<String, Map<String, WebSocketMessage>> userUnitsJsonMap = new HashMap<>();

    /**
     * pass configuration to superClass, init. Position Handler
     *
     * @param latch count down latch
     */
    public KPositionConsumer(CountDownLatch latch) {
        super(WebNotifierContext.getInstance().getConfig().getKPositionConsumerProperties(0),
                WebNotifierContext.getInstance().getConfig()
                        .getKafkaTopicsList(WebNotifierContext.getInstance().getConfig().getConsumerTopic(WebNotifierConstants.KAFKA_POSITION_CONSUMER), 0), latch, null);

        channelRedisCache = new ChannelRedisCache(WebNotifierContext.getInstance().getRedisConnection());

        getLogger().info(new LogMsg(getGroupId(),
                "KPositionConsumer initialized successfully."));
    }


    @Override
    protected void handleEmptyPoll() {
        getLogger().info(new LogMsg(getGroupId(),
                Thread.currentThread().getId() + "", "KPositionConsumer --> Empty Poll"));

    }

    DateTime currentMinus5 = null;

    @Override
    protected void processRecord(Tuple<PositionVO, PositionVO> record) {
        try {
            if (record.first.isDelayed()) {
                getLogger().warn(new LogMsg(WebNotifierConstants.DELAYED,
                        String.format("Ut:%s", record.first.getId())));
            } else {
                // make sure that signal is not delayed and updated
                // keep track the most updated signal of unit's signals
                PositionVO current = record.first;
                current.getParams().remove("raw");
                current.getParamsWithDate().remove("raw");
                PositionVO last = channelRedisCache.getPositionVO(current.getImei());
                if (last != null) {
                    if (current.getTrackerDate().isAfter(last.getTrackerDate())) {
                        if (current.getAssignedUsers() != null) {
                            current.getAssignedUsers().forEach(user -> {
                                Map<String, WebSocketMessage> units = userUnitsJsonMap.getOrDefault(user, new HashMap<>());
                                units.put(current.getId(), new WebSocketMessage(current));
                                userUnitsJsonMap.put(user, units);
                            });
                        }
                    }

                }
                channelRedisCache.setPositionVO(current);
                getLogger().debug(new LogMsg(WebNotifierConstants.CACHE, current.getId(), current.toString()));
            }
            WebNotifierConstants.POSITIONS_TOTAL_CONSUMED.incrementAndGet();

        } catch (Exception ex) {
            getLogger().catching(ex);
        }
    }

    @Override
    protected void doBeforeProcessingRecords(int size) {
        currentMinus5 = DateTime.now().minusMinutes(5);
    }

    /**
     * Set the default location if the current is zero lat and long
     *
     * @param current
     * @param last
     */
    private void handleZeroLocation(PositionVO current, PositionVO last) {
        // make sure that location is not zeros
        if (current.getLatitude() + current.getLongitude() == 0) {
            current.setLatitude(last.getLatitude());
            current.setLongitude(last.getLongitude());
            // make sure that last latitude is not zero
            if (last.getLatitude() == 0) {
                current.setLatitude(24.6944362);
            }
            // make sure that last longitude is not zero
            if (last.getLongitude() == 0) {
                current.setLongitude(46.6851655);
            }
        }
    }

    /**
     * handle positions after polling messages from kafka
     *
     * @param resultList tuple of positions
     */
    @Override
    protected void doAfterProcessingRecords(List<Tuple<PositionVO, PositionVO>> resultList) { // O(n) + O(n)*O(k)
        getLogger().info(new LogMsg(getGroupId(), String.format("Poll/TOTAL : [%d/%s]", resultList.size(),
                WebNotifierConstants.POSITIONS_TOTAL_CONSUMED.get())));

        if (userUnitsJsonMap.size() > 1) {
            updateRedis();
            userUnitsJsonMap = new HashMap<>();
        }
    }

    /**
     * Update redis with webSocket message and prepare a map of user-unit assignations
     *
     * @return
     */
    private void updateRedis() { // O(n)*O(k)
        userUnitsJsonMap.forEach((user, units) -> {
            // reflect the unit-user assignations to be from user side
            RedisFuture<Long> future = channelRedisCache.publishAsync(user, gson.toJson(units));
            future.whenComplete((type, ex) -> {
                if (ex != null) {
                    getLogger().catching(ex);
                }
            });
        });
    }
}
