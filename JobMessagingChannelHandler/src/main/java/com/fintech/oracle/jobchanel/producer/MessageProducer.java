package com.fintech.oracle.jobchanel.producer;


import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.Serializable;

/**
 * Created by sasitha on 12/6/16.
 *
 */
@Component
public class MessageProducer implements MessageProducerInterface{

    @Override
    public void sendMessage(final Serializable jobMessage, final JmsTemplate jmsTemplate) {
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return  session.createObjectMessage(jobMessage);
            }
        });
    }
}
