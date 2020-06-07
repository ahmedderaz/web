/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.service;

public interface ConsumerService {

    /**
     * start consumer in new thread
     */
    void startConsumer();

    /**
     * stop randomly consumer and kill his thread,
     * only if number of consumers greater than 1
     * @throws InterruptedException thread interrupted exception
     */
    void stopRandomConsumer() throws InterruptedException;

    /**
     * @return count of running consumers
     */
    int getConsumersCount();

    void stopAllConsumers();
}
