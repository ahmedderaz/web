package com.afaqy.avl.webnotifier;

import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.webnotifier.connections.WebServer;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.kafka.consumer.TaxiTripCostConsumer;
import com.afaqy.avl.webnotifier.model.JettyConfigModel;
import com.afaqy.avl.webnotifier.service.KDeviceStatusService;
import com.afaqy.avl.webnotifier.service.KNotificationService;
import com.afaqy.avl.webnotifier.service.KPositionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Name: WebNotifierMain
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
public class Main {
    private final static Logger LOGGER = LogManager.getLogger(Main.class);


    public static void main(String[] args) throws Exception {
        LOGGER.info("Initializing Web Notifier .... ");
        WebNotifierContext.init(args);

        startJettyServer();

        String solution = WebNotifierContext.getInstance().getSolution();

        WebNotifierContext.getInstance().getSlackNotifier().sendStartedNotification(solution);

        addShutdownHook();

        LOGGER.info(String.format("[%s] Web Notifier Started.... ", solution));

        switch (solution) {
            case WebNotifierConstants.SOLUTION_AIRPORT_TAXI:
                startTaxiTripCostConsumer();
                break;
            case WebNotifierConstants.SOLUTION_AVL:
                startKNotificationConsumer();

                startKPositionConsumer();

                startKDeviceStatusConsumer();
                break;
        }

        WebNotifierContext.getInstance().getSlackNotifier().sendStartedNotification();

    }

    private static void startKDeviceStatusConsumer() {
        LOGGER.info("Starting Device Status Consumer...");

        new KDeviceStatusService().startConsumer();
    }

    private static void startTaxiTripCostConsumer() {
        new TaxiTripCostConsumer().run();
    }

    /**
     * start kafka position consumer thread
     */
    private static void startKPositionConsumer() {
        LOGGER.info("Starting Position Consumer...");

        new KPositionService().startConsumer();
    }

    /**
     * start jetty server thread for calling API's
     */
    private static void startJettyServer() {
        JettyConfigModel config = new JettyConfigModel();

        config.setPorts(WebNotifierContext.getInstance().getConfig().getIntList("jetty.ports"));
        config.setScanPackage(WebNotifierContext.getInstance().getConfig().getJettyScanPackage());
        config.setMicroServiceName(WebNotifierContext.getInstance().getConfig().getJettyMicroServiceName());
        config.setEnableSsl(WebNotifierContext.getInstance().getConfig().isSslEnabled());
        config.setServerVersion(WebNotifierContext.getInstance().getConfig().getServerVersion());
        config.setSwaggerEnv(WebNotifierContext.getInstance().getConfig().getServerSwaggerEnv());
        config.setCertificatePath(WebNotifierContext.getInstance().getConfig().getJettyCertificatePath());
        config.setKeyStorePassword(WebNotifierContext.getInstance().getConfig().getJettyKeyStorePassword());
        config.setKeyManagerPassword(WebNotifierContext.getInstance().getConfig().getJettyKeyManagerPassword());


        WebServer jettyServer = new WebServer(config);
        new Thread(jettyServer, "WebServer-Thread").start();
    }

    /**
     * start kafka notification consumer thread
     */
    private static void startKNotificationConsumer() {
        new KNotificationService().startConsumer();
    }

    /**
     * send notification in case of micro-service Failure
     */
    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                WebNotifierContext.getInstance().getSlackNotifier().sendStoppedNotifications();

                LOGGER.fatal(new LogMsg(WebNotifierContext.getInstance().getConfig().serverName(),
                        WebNotifierContext.getInstance().getConfig().serverPublicIp(),
                        "Web Notifier Service was stopped"));


            } catch (Exception e) {
                LOGGER.catching(e);
                LOGGER.fatal(e);
            }
        }));
    }
}

