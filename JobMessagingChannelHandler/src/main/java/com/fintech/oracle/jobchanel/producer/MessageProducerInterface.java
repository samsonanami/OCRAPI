package com.fintech.oracle.jobchanel.producer;

import com.fintech.oracle.jobchanel.common.ProcessingJobMessage;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

/**
 * Created by sasitha on 12/6/16.
 *
 */
public interface MessageProducerInterface {

    void sendMessage(ProcessingJobMessage jobMessage, JmsTemplate jmsTemplate);
}
