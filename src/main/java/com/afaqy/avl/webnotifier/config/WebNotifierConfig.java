/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.config;

import com.afaqy.avl.core.AvlConfig;
import com.afaqy.avl.core.helper.AvlConstants;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.afaqy.avl.core.helper.AvlConstants.KConsumer.*;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;


/**
 * Name: WebNotifierConfig
 * <br>
 * Description:
 * <br>
 *
 * @author Mohammed ElAdly
 * <br>
 * Mail : mohammed.eladly@afaqy.com
 * <br>
 */
public class WebNotifierConfig extends AvlConfig {
    public static final String SOLUTION_AIRPORT_TAXI = "airport_taxi";
    public static final String SOLUTION_AVL = "avl";

    public WebNotifierConfig(String configFile) {
        super(configFile);
    }

    /**
     * prepare kafka notification consumer
     *
     * @param consumerIndex kafka consumer index
     * @return Properties
     */
    public Properties getKNotificationConsumerProperties(int consumerIndex) {
        Properties properties = getDefaultKafkaConsumerProperties();
        properties.put(GROUP_ID_CONFIG, getKConsumerGroupId(consumerIndex, KAFKA_NOTIFICATION_CONSUMER));
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG, configuration.getString(getConsumerKeyDeserializer(KAFKA_NOTIFICATION_CONSUMER)));
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, configuration.getString(getConsumerValueDeserializer(KAFKA_NOTIFICATION_CONSUMER)));

        return properties;
    }

    /**
     * get kafka consumer group id
     *
     * @param consumerIndex kafka consumer index
     * @param groupId kafka group's id
     * @return groupId
     */
    public String getKConsumerGroupId(int consumerIndex, String groupId) {
        return configuration.getStringArray(getConsumerGroupId(groupId))[consumerIndex];
    }

    /**
     * prepare kafka position consumer
     *
     * @param consumerIndex kafka consumer index
     * @return kafka properties
     */
    public Properties getKPositionConsumerProperties(int consumerIndex) {
        Properties properties = getDefaultKafkaConsumerProperties();

        properties.put(GROUP_ID_CONFIG, getKConsumerGroupId(consumerIndex, KAFKA_POSITION_CONSUMER));
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG, configuration.getString(getConsumerKeyDeserializer(KAFKA_POSITION_CONSUMER)));
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, configuration.getString(getConsumerValueDeserializer(KAFKA_POSITION_CONSUMER)));

        return properties;
    }

    /**
     * prepare kafka position consumer
     *
     * @param consumerIndex consumer index
     * @return kafka properties
     */
    public Properties getKDeviceStatusConsumerProperties(int consumerIndex) {
        Properties properties = getDefaultKafkaConsumerProperties();

        properties.put(GROUP_ID_CONFIG, getKConsumerGroupId(consumerIndex, KAFKA_POSITION_CONSUMER));
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG, Serdes.String().deserializer().getClass());
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, Serdes.String().deserializer().getClass());

        return properties;
    }


    public String getSolution() throws IllegalArgumentException {
        String solution = System.getenv("AFAQY_SOLUTION");
        if (solution == null)
            return SOLUTION_AVL;
        else if (!solution.equals(SOLUTION_AVL) && !solution.equals(SOLUTION_AIRPORT_TAXI)) {
            LogManager.getLogger().error(new IllegalArgumentException("Invalid solution :" + solution));
            System.exit(1);
        }
        return solution;
    }

    public String getTaxiTripCostTopic() {
        return configuration.getString(getConsumerTopic(KAFKA_TAXI_TRIP_COST_CONSUMER));
    }

    public Properties getTaxiTripCostConsumerProperties() {
        Properties properties = getDefaultKafkaConsumerProperties();
        properties.put(GROUP_ID_CONFIG, getKConsumerGroupId(0, KAFKA_TAXI_TRIP_COST_CONSUMER));
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG, configuration.getString(getConsumerKeyDeserializer(KAFKA_TAXI_TRIP_COST_CONSUMER)));
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, configuration.getString(getConsumerValueDeserializer(KAFKA_TAXI_TRIP_COST_CONSUMER)));

        return properties;
    }


    public String serverName() {
        return configuration.getString(WebNotifierConstants.Support.SERVER_NAME);
    }

    public String serverPublicIp() {
        return configuration.getString(WebNotifierConstants.Support.SERVER_PUBLIC_IP);
    }

    public String slackToken() {
        return configuration.getString(AvlConstants.Slack.TOKEN);
    }

    public String slackChannel() {
        return configuration.getString(AvlConstants.Slack.CHANNEL);
    }

    public static String getConsumerKeyDeserializer(String consumerName) {
        return String.format("kafka.consumer.%s.group.keyDeserializer", consumerName);
    }

    public static String getConsumerValueDeserializer(String consumerName) {
        return String.format("kafka.consumer.%s.group.valueDeserializer", consumerName);
    }

    public static String getConsumerTopic(String consumerName) {
        return String.format("kafka.consumer.%s.group.topic", consumerName);
    }

    public static String getConsumerGroupId(String consumerName) {
        return String.format("kafka.consumer.%s.group.id", consumerName);
    }

    public String getJettyScanPackage() {
        return configuration.getString(WebNotifierConstants.Jetty.SCAN_PACKAGE);
    }

    public String getJettyMicroServiceName() {
        return configuration.getString(WebNotifierConstants.Jetty.MICRO_SERVICE_NAME);
    }

    public String getJettyCertificatePath() {
        return configuration.getString(WebNotifierConstants.Jetty.CERTIFICATE_PATH,"Not defined");
    }

    public String getKafkaTopicName(String property, int index) {
        return configuration.getStringArray(getConsumerTopic(property))[index];
    }

    public String getJettyKeyStorePassword() {
        return configuration.getString(WebNotifierConstants.Jetty.KEY_STORE_PASSWORD,"Not defined");
    }

    public String getJettyKeyManagerPassword() {
        return configuration.getString(WebNotifierConstants.Jetty.KEY_MANAGER_PASSWORD,"Not defined");
    }

    public boolean isSslEnabled() {
        return configuration.getBoolean(WebNotifierConstants.Jetty.ENABLE_SSL, false);
    }

    public String getServerVersion() {
        return configuration.getString(WebNotifierConstants.Jetty.SERVER_VERSION);
    }

    public String getServerSwaggerEnv() {
        return configuration.getString(WebNotifierConstants.Jetty.SERVER_SWAGGER_ENV);
    }


    public String getRedisHost() {
        return super.getRedisHosts().get(0).getUri();
    }



    public List<String> getKafkaTopicsList(String property, int index) {
        String string = configuration.getStringArray(property)[index];
        return Arrays.asList(string.split(","));
    }

}
