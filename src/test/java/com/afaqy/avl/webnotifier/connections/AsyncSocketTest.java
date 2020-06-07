package com.afaqy.avl.webnotifier.connections;

import com.afaqy.avl.core.model.vo.UserVO;
import com.afaqy.avl.webnotifier.config.WebNotifierConfig;
import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.data.cache.AsyncSocketUnitsCache;
import com.afaqy.avl.webnotifier.model.WebSocketMessage;
import com.afaqy.core.cache.redis.RedisConnection;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

/**
 * Name : AsyncSocketTest
 * <p>
 * Description :
 * <p>
 * Date : 01/05/2020
 * <p>
 * Create by : Mohamed Elkady
 * <p>
 * Mail : elkady@afaqy.com
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AsyncSocketTest {

    private AsyncSocketUnitsCache cache;
    private AsyncSocket socket;
    @Mock
    private WebNotifierContext context;
    @Mock
    private WebNotifierConfig config;

    @BeforeEach
    private void beforeEach() {
        cache = Mockito.mock(AsyncSocketUnitsCache.class);
        socket = new AsyncSocket(getUserVO(), false, cache);
    }

    @Test
    public void onWebSocketCloseTest() {
        cache.set(socket.getSocketChannelId() + "_5e6f3f9fdc4fae532259f47a", true + "");
        socket.onWebSocketClose(1000, "null");
        String cachedValue = cache.get(socket.getSocketChannelId() + "5e6f3f9fdc4fae532259f47a");
        Assertions.assertNull(cachedValue);
    }

    @Test
    public void testRemoveIgnoredUnitsFromRedis() {
        Map<String, WebSocketMessage> map = new HashMap<>();
        map.put("1", new WebSocketMessage());
        map.put("1", new WebSocketMessage());
        map.put("3", new WebSocketMessage());
        Mockito.when(cache.get(socket.getSocketChannelId()+"_1")).thenReturn("true");
        socket.removeIgnoredUnitsFromRedis(map);
        Assertions.assertEquals(1, map.size());

    }

    private UserVO getUserVO() {
        UserVO userVO = new UserVO();
        System.out.println(new ObjectId());
        System.out.println(new ObjectId());
        System.out.println(new ObjectId());
        userVO.setId("");
        return userVO;
    }


}
