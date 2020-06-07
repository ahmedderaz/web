/*package com.afaqy.avl.webnotifier.service;


import com.afaqy.avl.core.model.Parameter;
import com.afaqy.avl.core.model.vo.PositionVO;
import com.afaqy.avl.core.model.vo.UserVO;
import com.afaqy.avl.webnotifier.connections.AsyncSocket;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.model.WebSocketMessage;
import com.afaqy.core.serialization.DateTimeDeserializer;
import com.afaqy.core.serialization.DateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * Name :AsyncSocketTest
 * <br>
 * Description :  for test action come form web and test add
 * requested units to redis and remove it from redis depend on action
 * <br>
 * Date : 26/04/2020
 * <br>
 * Create by : Mona Adel
 *
 * <br>
 * Mail: mona.adel@afaqy.com
 */
/*

public class AsyncSocketTest {


    private  AsyncSocket asyncSocket;
    private  UserVO user;
    private  PositionVO positionVO;
    private  Gson gson;
      String[] args = {"/media/afq-eg-lptp-080/work/projects/Afaqy-Service-AVL-WebNotifier/config/local/configurations.xml"};


    @Before
    public  void init() throws Exception {
        WebNotifierContext.init(args);
         gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
                .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
                .create();
        positionVO = Mockito.spy(PositionVO.class);
        user = Mockito.spy(UserVO.class);
        user.setId("mona");
        asyncSocket=new AsyncSocket(user,false);
    }

    @Test
    public void onWebSocketTextTest() {
        asyncSocket.onWebSocketText("{\"action\":\"ADD\",\"units\" : [ \"id1\",\"id2\",\"id3\"]}");
        Assert.assertFalse("ADD", asyncSocket.isAllUnits());
        asyncSocket.onWebSocketText("{\"action\":\"ADDALL\",\"units\" : []}");
        Assert.assertTrue("ADDALL", asyncSocket.isAllUnits());
        asyncSocket.onWebSocketText("{\"action\":\"REMOVE\",\"units\" : [ \"id1\"]}");
        Assert.assertFalse("REMOVE", asyncSocket.isAllUnits());
        asyncSocket.onWebSocketText("{\"action\":\"REMOVEALL\",\"units\" : []}");
        Assert.assertFalse("REMOVEALL", asyncSocket.isAllUnits());
    }

    /**
     * test message function in AsyncSocket that responsible for update units in web
     */
/*
    @Test
    public void messageTest() {
        positionVO.setId("id1");
        positionVO.setTrackerDate(new DateTime());
        positionVO.setAccChangeTime(new DateTime());
        Map <String, Parameter> paramsWithDate=new HashMap<String, Parameter>();
        paramsWithDate .put("test",new Parameter("test",new Date().getTime()));
        positionVO.setParamsWithDate(paramsWithDate);
        Map<String, WebSocketMessage> units = new HashMap<>();
        units.put(positionVO.getId(), new WebSocketMessage(positionVO));
        asyncSocket.message("test_channel", gson.toJson(units));

    }

}*/
