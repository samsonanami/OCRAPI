package com.fintech.oracle.jobchanel.consumer;

import com.fintech.oracle.jobchanel.exception.JobChanelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by sasitha on 12/7/16.
 *
 */
@Component
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    private MessageDelegate delegate;

    public void setDelegate(MessageDelegate delegate) {
        this.delegate = delegate;
    }

    public void processMessage(final Serializable message) throws JobChanelException {
        LOGGER.debug("receiving message with message body {} from active mq ", message);
        if(delegate == null){
            throw new JobChanelException("message could not be delegate. No delegate class is available ");
        }
        LOGGER.debug("delegating message");
        delegate.delegateMessage(message);
    }

}
