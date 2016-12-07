package com.fintech.oracle.jobchanel.producer;

import org.springframework.jms.core.JmsTemplate;

import java.io.Serializable;

/**
 * Created by sasitha on 12/6/16.
 *
 */
public interface MessageProducerInterface {

    void sendMessage(Serializable jobMessage, JmsTemplate jmsTemplate);
}
