/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 *//*


package com.afaqy.avl.webnotifier.service;

import com.afaqy.avl.webnotifier.context.WebNotifierContext;
import com.afaqy.avl.webnotifier.service.ConsumerService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class KPositionServiceTest {
    static String[] args = {"/media/afq-eg-lptp-080/work/projects/Afaqy-Service-AVL-WebNotifier/config/local/configurations.xml"};

    private static ConsumerService service;

    @BeforeClass
    public static void setUp() throws Exception {
        WebNotifierContext.init(args);
        service = WebNotifierContext.getInstance().getkPositionService();
    }

    @Test
    public void startConsumer() {
        service.startConsumer();
        Assert.assertEquals(WebNotifierContext.getInstance().getPositionConsumerMap().size(),1);
    }

    @Test
    public void stopRandomConsumer() throws InterruptedException {
        service.stopRandomConsumer();
        Assert.assertEquals(WebNotifierContext.getInstance().getPositionConsumerMap().size(),0);
    }

    @Test
    public void getConsumersCount() {
        System.out.println(service.getConsumersCount());
    }

    @Test
    public void stopAllConsumers(){
        service.stopAllConsumers();
        Assert.assertEquals(WebNotifierContext.getInstance().getPositionConsumerMap().size(),0);

    }
}*/
