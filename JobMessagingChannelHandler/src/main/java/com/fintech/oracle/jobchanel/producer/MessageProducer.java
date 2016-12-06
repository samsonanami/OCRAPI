package com.fintech.oracle.jobchanel.producer;

import com.fintech.oracle.jobchanel.common.ProcessingJobMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * Created by sasitha on 12/6/16.
 *
 */
@Component
public class MessageProducer implements MessageProducerInterface{

    @Override
    public void sendMessage(final ProcessingJobMessage jobMessage, final JmsTemplate jmsTemplate) {
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(jobMessage);
                return objectMessage;
            }
        });
    }
}
