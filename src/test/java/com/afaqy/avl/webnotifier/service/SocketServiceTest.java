/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 *//*


package com.afaqy.avl.webnotifier.service;


import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class SocketServiceTest {

    static String[] args = {"/media/afq-eg-lptp-080/work/projects/Afaqy-Service-AVL-WebNotifier/config/local/configurations.xml"};

    String socketId = "";//to be added

    static SocketService service;

    @BeforeClass
    public static void setUp() throws Exception {
        WebNotifierContext.init(args);
        service = WebNotifierContext.getInstance().getSocketService();

    }

    @Test
    public void getOnlineSockets() {
        ArrayList onlineSockets = service.getOnlineSockets();
        System.out.println(onlineSockets);

    }


    @Test
    public void removeAllSockets() {
        System.out.println(service.removeAllSockets());
    }
}*/
