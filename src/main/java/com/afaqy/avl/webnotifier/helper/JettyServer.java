/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.helper;



import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.model.JettyConfigModel;
import io.swagger.jaxrs.config.DefaultJaxrsConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.List;


/**
 * Name: JettyServer
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
public class JettyServer implements Runnable {

    private final static Logger LOGGER = LogManager.getLogger(JettyServer.class);

    protected String APPLICATION_PATH = "/api";
    private String CONTEXT_ROOT;

    private List<Integer> PORTS;
    private String SCAN_PACKAGE, SERVICE_NAME, DOCS_PATH,
            CERTIFICATE_PATH, KEY_STORE_PASSWORD, KEY_MANAGER_PASSWORD;
    private String VERSION, SWAGGER_ENV;
    private boolean ENABLE_SSL;

    private Server jettyServer;
    private ServletContextHandler apiContext;

    public JettyServer(JettyConfigModel config) {
        this(config.getPorts(), config.getScanPackage(), config.getMicroServiceName(), config.getServerVersion()
                , config.getSwaggerEnv());

        this.CERTIFICATE_PATH = config.getCertificatePath();
        this.KEY_STORE_PASSWORD = config.getKeyStorePassword();
        this.KEY_MANAGER_PASSWORD = config.getKeyManagerPassword();
        this.ENABLE_SSL = config.isEnableSsl();
    }

    public JettyServer(List<Integer> ports, String scanPackage, String microServiceName,
                       String version, String swaggerEnv) {
        this.PORTS = ports;
        this.SCAN_PACKAGE = scanPackage;
        this.SERVICE_NAME = microServiceName;
        this.VERSION = version;
        this.SWAGGER_ENV = swaggerEnv;

        this.CONTEXT_ROOT = "/" + SERVICE_NAME + "/";
        this.DOCS_PATH = CONTEXT_ROOT + "docs";
    }

    public void run() {
        try {
            jettyServer.start();
            jettyServer.dump(System.err);
            jettyServer.join();
        } catch (Exception e) {
            LOGGER.warn(new LogMsg(WebNotifierConstants.Jetty.JETTY, e.getMessage()));
            LOGGER.catching(e);
            jettyServer.destroy();
        }

    }

    /**
     * configuring jetty server
     */
    public void init() {

        // setup thread pool
        ThreadPool pool = new ExecutorThreadPool();
        jettyServer = new Server(pool);

        Connector[] connectors = ENABLE_SSL ? getSSLConnectors() : getConnectors();

        jettyServer.setConnectors(connectors);
//        jettyServer.setDumpAfterStart(true);
//        jettyServer.setDumpBeforeStop(true);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        jettyServer.setHandler(contexts);

        this.apiContext = new ServletContextHandler(contexts, CONTEXT_ROOT);

        ServletHolder jerseyServlet = apiContext.addServlet(ServletContainer.class, APPLICATION_PATH + "/*");
        jerseyServlet.setInitOrder(1);
//         Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages",
                "com.api.resources;io.swagger.jaxrs.json;io.swagger.jaxrs.listing;" + SCAN_PACKAGE);


        // Setup Swagger servlet
        ServletHolder swaggerServlet = apiContext.addServlet(DefaultJaxrsConfig.class, "/swagger-core");
        swaggerServlet.setInitOrder(2);
        swaggerServlet.setInitParameter("swagger.api.basepath", CONTEXT_ROOT + "api");
        swaggerServlet.setInitParameter("scan.all.resources", String.valueOf(true));

        swaggerServlet.setInitParameter("api.version", VERSION);

        ServletContextHandler docsContext = new ServletContextHandler(contexts, DOCS_PATH);
        // Setup Swagger-UI static resources

        String resourceBasePath = JettyServer.class.getResource("/" + SWAGGER_ENV).toExternalForm();
        docsContext.setWelcomeFiles(new String[]{"index.html"});
        docsContext.setResourceBase(resourceBasePath);

        docsContext.addServlet(new ServletHolder(new DefaultServlet()), "/*");

    }

    private Connector[] getConnectors() {
        Connector[] connectors = new Connector[PORTS.size()];
        for (int i = 0; i < PORTS.size(); i++) {
            int port = PORTS.get(i);
            ServerConnector connector = new ServerConnector(jettyServer); //not over ssl
            connector.setPort(port);
            connectors[i] = connector;
        }
        return connectors;
    }

    private Connector[] getSSLConnectors() {
        //ssl
        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();

        sslContextFactory.setKeyStorePath(this.CERTIFICATE_PATH);
        sslContextFactory.setKeyStorePassword(this.KEY_STORE_PASSWORD);
        sslContextFactory.setKeyManagerPassword(this.KEY_MANAGER_PASSWORD);
        Connector[] connectors = new Connector[PORTS.size()];
        for (int i = 0; i < PORTS.size(); i++) {
            int port = PORTS.get(i);

            ServerConnector sslConnector = new ServerConnector(jettyServer,
                    new SslConnectionFactory(sslContextFactory, "http/1.1"),
                    new HttpConnectionFactory(https));
            sslConnector.setPort(port);
            connectors[i] = sslConnector;
        }
        return connectors;
    }


    public ServletContextHandler getApiContext() {
        return apiContext;
    }
}
