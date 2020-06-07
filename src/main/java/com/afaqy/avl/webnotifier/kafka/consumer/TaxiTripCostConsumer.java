package com.afaqy.avl.webnotifier.kafka.consumer;


import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.core.kafka.consumer.AbstractKafkaConsumer;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.connections.taxi.TaxiTripAsyncSocket;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.data.cache.ChannelRedisCache;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;
import java.util.List;



/**
 * Name : TaxiTripCostConsumer
 * <br>
 * Description :
 * <br>
 * Date : 14/11/2019
 * <br>
 * Create by : Mohamed Elkady
 * <br>
 * Mail : elkady@afaqy.com
 */
public class TaxiTripCostConsumer extends AbstractKafkaConsumer<String> {

    private ChannelRedisCache channelRedisCache;

    public TaxiTripCostConsumer() {
        super(WebNotifierContext.getInstance().getConfig().getTaxiTripCostConsumerProperties(),
                Arrays.asList(WebNotifierContext.getInstance().getConfig().getTaxiTripCostTopic()),
                null, null);
        channelRedisCache = new ChannelRedisCache(WebNotifierContext.getInstance().getRedisConnection());
    }

    @Override
    protected void processRecord(String tripCost) {
        try {
            JsonObject jsonObject = JsonParser.parseString(tripCost).getAsJsonObject();
            JsonElement unitIdJsonElement = jsonObject.get("unitId");
            String unitId = unitIdJsonElement.getAsString();
            if (!unitId.equals("")) {
                channelRedisCache.publishAsync(unitId, tripCost);
                channelRedisCache.set(TaxiTripAsyncSocket.redisKey(unitId), tripCost);
                getLogger().info(new LogMsg(WebNotifierConstants.CACHE, String.format("Ut:%s", unitId), "Update"));
            }

        } catch (Exception e) {
            getLogger().catching(e);
        }
    }

    @Override
    protected void doAfterProcessingRecords(List<String> resultList) {
        getLogger().info(new LogMsg(WebNotifierConstants.KAFKA, "Total", resultList.size() + ""));
    }

    @Override
    protected void handleEmptyPoll() {

    }

}
