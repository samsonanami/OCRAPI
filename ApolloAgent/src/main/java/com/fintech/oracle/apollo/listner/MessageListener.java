package com.fintech.oracle.apollo.listner;

import com.fintech.oracle.jobchanel.consumer.MessageDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by sasitha on 12/7/16.
 *
 */
public class MessageListener implements MessageDelegate{

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    @Override
    public void delegateMessage(Serializable message) {
        LOGGER.debug("received message from job chanel {}", message);
    }
}
