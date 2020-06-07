/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.api;

import com.afaqy.avl.core.helper.log.LogMsg;
import com.afaqy.avl.webnotifier.WebNotifierConstants;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.service.SocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Path("sockets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "SocketController")
public class SocketController {
    private final String REQUEST = "Request <=";
    private final String RESPONSE = "Response =>";
    private final Logger logger = LogManager.getLogger(this.getClass());

    private SocketService service;

    public SocketController() {
        this.service = WebNotifierContext.getInstance().getSocketService();
    }

    protected String request(String methodName) {
        return REQUEST + " " + methodName;
    }

    protected String response(String methodName) {
        return RESPONSE + " " + methodName;
    }

    protected Logger getLogger() {
        return logger;
    }


    @GET
    @ApiOperation(value = "get all online sockets")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Success"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 404, message = "Not Found")})
    public Response getOnlineSockets() {
        try {
            getLogger().info(request("getOnlineSockets"));
            ArrayList rsList = service.getOnlineSockets();
            getLogger().info(new LogMsg(response("getOnlineSockets"), rsList.toString()));
            return Response.status(Response.Status.OK).entity(rsList).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }


    @Path("/{id}")
    @DELETE
    @ApiOperation(value = "close socket by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Success"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 404, message = "Not Found")})
    public Response closeSocket(@PathParam("id") String id) {
        try {
            getLogger().info(new LogMsg(WebNotifierConstants.HTTP, id, request("closeSocket")));
            boolean found = service.closeSocket(id);

            String msg = String.format("Socket [%s] not found to close", id);
            if (found) {
                msg = String.format("Socket %s closed", id);
            }
            Map<String, String> result = new HashMap();
            result.put("result", msg);
            getLogger().info(new LogMsg(WebNotifierConstants.HTTP, msg, response("closeSocket")));

            return Response.status(Response.Status.OK).entity(result).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("/count")
    @GET
    @ApiOperation(value = "Get online Sockets count")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Success"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 404, message = "Not Found")})
    public Response socketsCount() {
        try {
            getLogger().info(new LogMsg(WebNotifierConstants.HTTP, request("socketsCount")));
            Map<String, Integer> result = service.getOnlineSocketsCount();

            getLogger().info(new LogMsg(WebNotifierConstants.HTTP, response("closeSocket"), result.get("total") + ""));

            return Response.status(Response.Status.OK).entity(result).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @ApiOperation(value = "close all sockets")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Success"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 404, message = "Not Found")})
    public Response closeAllSockets() {
        try {
            getLogger().info(new LogMsg(WebNotifierConstants.HTTP, request("closeAllSockets")));
            String resultMsg = service.removeAllSockets();
            Map<String, String> result = new HashMap();
            result.put("result", resultMsg);
            getLogger().info(new LogMsg(WebNotifierConstants.HTTP, response("closeAllSockets"), resultMsg));

            return Response.status(Response.Status.OK).entity(result).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

}
