/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */
package com.afaqy.avl.webnotifier.connections;


import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.core.model.vo.UserVO;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.config.WebNotifierConfig;
import com.afaqy.avl.webnotifier.connections.taxi.TaxiTripAsyncSocket;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AsyncSocketServlet extends WebSocketServlet {
    private final static Logger LOGGER = LogManager.getLogger(AsyncSocketServlet.class);

    @Override
    public void configure(WebSocketServletFactory factory) {
        try {
            final String SOLUTION = WebNotifierContext.getInstance().getSolution();

            factory.getPolicy().setIdleTimeout(WebNotifierConstants.Web.ASYNC_TIMEOUT);
            factory.getPolicy().delegateAs(WebSocketBehavior.SERVER);
            factory.setCreator((req, resp) -> {
                try {
                    switch (SOLUTION) {
                        case WebNotifierConfig.SOLUTION_AIRPORT_TAXI:
                            return getTripTaxiSocket(req, resp);
                        case WebNotifierConfig.SOLUTION_AVL:
                            return getAvlSocket(req, resp);
                    }
                } catch (Exception e) {
                    LOGGER.error(new LogMsg("Invalid connection request", mapToString(req.getParameterMap()), e.getMessage()));
                    LOGGER.catching(e);
                }
                LOGGER.warn(new LogMsg("Connection rejected ", mapToString(req.getParameterMap())));
                return null;
            });
        } catch (IllegalArgumentException ex) {
            LOGGER.catching(ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
    }

    private Object getTripTaxiSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) throws Exception {
        // validate the request with needed params
        String unitId = req.getParameterMap().get("unitId").get(0);
        if (unitId == null || unitId.equals("") || unitId.equals("null")) {
            LOGGER.error(new LogMsg("Invalid connection request", req.toString()));
            return null;
        }

        return new TaxiTripAsyncSocket(unitId);
    }

    private Object getAvlSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        try {
            String userId = req.getParameterMap().get("userId").get(0);
            try {
                // validate that the user is a mongoDb id representable
                new ObjectId(userId);
            } catch (IllegalArgumentException ex) {
                LOGGER.error(userId + " is not an mongodb id");
                return null;
            }
            UserVO user = WebNotifierContext.getInstance().getUserService().get(userId);
            if (user == null) {
                LOGGER.error("No user found with id [{}]" + userId);
                return null;
            }
            boolean admin = WebNotifierContext.getInstance().getUserService().isAdmin(userId);
            LOGGER.info(new LogMsg(WebNotifierConstants.WEB, userId, "Going to create websocket for admin=" + admin));

            return new AsyncSocket(user, admin, WebNotifierContext.getInstance().getConfig().getRedisHost());
        } catch (Exception e) {
            LOGGER.catching(e);
            LOGGER.error("Invalid request " + mapToString(req.getParameterMap()), e.getMessage());
        }
        return null;
    }

    public String mapToString(Map<String, List<String>> map) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, List<String>>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, List<String>> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue().get(0));
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();

    }

}
