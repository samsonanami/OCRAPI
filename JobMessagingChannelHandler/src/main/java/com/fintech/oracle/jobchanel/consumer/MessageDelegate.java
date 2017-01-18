package com.fintech.oracle.jobchanel.consumer;

import java.io.Serializable;

/**
 * Created by sasitha on 12/7/16.
 *
 */
@FunctionalInterface
public interface MessageDelegate {

    void delegateMessage(Serializable message);
}
