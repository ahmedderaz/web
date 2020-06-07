/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.context;

import com.afaqy.avl.core.helper.SlackNotifier;
import com.afaqy.avl.core.service.UserService;
import com.afaqy.avl.webnotifier.config.WebNotifierConfig;
import com.afaqy.avl.webnotifier.connections.ConnectionManager;
import com.afaqy.avl.webnotifier.kafka.consumer.KDeviceStatusConsumer;
import com.afaqy.avl.webnotifier.kafka.consumer.KNotificationConsumer;
import com.afaqy.avl.webnotifier.kafka.consumer.KPositionConsumer;
import com.afaqy.avl.webnotifier.service.ConsumerService;
import com.afaqy.avl.webnotifier.service.KNotificationService;
import com.afaqy.avl.webnotifier.service.KPositionService;
import com.afaqy.avl.webnotifier.service.SocketService;
import com.afaqy.core.cache.redis.RedisConnection;
import com.afaqy.core.context.CoreContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;


/**
 * Name: WebNotifierContext
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
public class WebNotifierContext extends CoreContext {

    private final static Logger LOGGER = LogManager.getLogger(WebNotifierContext.class);

    private static WebNotifierContext INSTANCE;
    private WebNotifierConfig config;
    private ConnectionManager connectionManager;
    private CountDownLatch latch = new CountDownLatch(15);

    private Map<Thread, KNotificationConsumer> notificationConsumerMap = new ConcurrentHashMap<>();
    private Map<Thread, KPositionConsumer> positionConsumerMap = new ConcurrentHashMap<>();
    private Map<Thread, KDeviceStatusConsumer> deviceStatusConsumerMap = new ConcurrentHashMap<>();

    private SlackNotifier slackNotifier;
    private ConsumerService kNotificationService;
    private ConsumerService kPositionService;
    private SocketService socketService;
    private String solution;
    private UserService userService;
    private  RedisConnection redisConnection;
    private WebNotifierContext(String[] arguments) throws Exception {
        super(arguments);
        init();
    }

    public static void init(String[] args) throws Exception {
        if (INSTANCE == null) INSTANCE = new WebNotifierContext(args);
    }

    public static WebNotifierContext getInstance() {
        return INSTANCE;
    }


    public WebNotifierConfig getConfig() {
        return config;
    }

    public void init() throws Exception {
        LOGGER.info("Init. Web Notifier Context ..");
        this.config = new WebNotifierConfig(getVmArguments()[0]);
        redisConnection = new RedisConnection(config.getRedisHosts().get(0).getUri());
        userService = new UserService(config.getRedisHosts().get(0), config.getMasterDataBaseUri());
        this.slackNotifier = new SlackNotifier(
                config.slackToken(),
                config.slackChannel());
        initServices();
    }

    /**
     * init. services
     */
    protected void initServices() {
        LOGGER.info("Init. Web Notifier Services ..");
        this.connectionManager = new ConnectionManager();
        this.kNotificationService = new KNotificationService();
        this.kPositionService = new KPositionService();
        this.socketService = new SocketService(connectionManager);
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public Map<Thread, KNotificationConsumer> getNotificationConsumerMap() {
        return notificationConsumerMap;
    }

    public SlackNotifier getSlackNotifier() {
        return slackNotifier;
    }

    public ConsumerService getkNotificationService() {
        return kNotificationService;
    }

    public ConsumerService getkPositionService() {
        return kPositionService;
    }

    public Map<Thread, KDeviceStatusConsumer> getDeviceStatusConsumerMap() {
        return deviceStatusConsumerMap;
    }

    public Map<Thread, KPositionConsumer> getPositionConsumerMap() {
        return positionConsumerMap;
    }

    public SocketService getSocketService() {
        return socketService;
    }

    public String getSolution() {
        if (solution == null)
            solution = config.getSolution(); // one call to system environment
        return solution;
    }

    public UserService getUserService() {
        return userService;
    }


    public RedisConnection getRedisConnection() {
        return redisConnection;
    }

    public void setRedisConnection(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }
}
