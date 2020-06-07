/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.connections;

import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.helper.JettyServer;
import com.afaqy.avl.webnotifier.model.JettyConfigModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebServer extends JettyServer {

    private final static Logger LOGGER = LogManager.getLogger(WebServer.class);

    public WebServer(JettyConfigModel config) {
        super(config);
        this.init();
    }

    public WebServer(int port, String scanPackage) {
        super(WebNotifierContext.getInstance().getConfig().getIntList("jetty.ports"),
                scanPackage, WebNotifierContext.getInstance().getConfig().getJettyMicroServiceName(),
                WebNotifierContext.getInstance().getConfig().getServerVersion(),
                WebNotifierContext.getInstance().getConfig().getServerSwaggerEnv());
        this.init();
    }

    @Override
    public void init() {
        LOGGER.info("Init. Web Server for Notifier ..");

        super.init();

        getApiContext().addServlet(AsyncSocketServlet.class, APPLICATION_PATH + "/socket/");

    }
}
