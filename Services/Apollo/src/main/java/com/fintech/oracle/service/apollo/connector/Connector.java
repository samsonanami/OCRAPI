package com.fintech.oracle.service.apollo.connector;

import com.fintech.oracle.service.apollo.exception.connector.ConnectorException;
import com.fintech.oracle.service.apollo.exception.connector.abbyy.AbbyyConnectorException;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by sasitha on 6/20/17.
 *
 */
public interface Connector<E> {
    void setConfigurations(Map<String, String> configurations);
    CompletableFuture<E> submitForProcessing(byte[] image, Map<String, String> processingConfigurations) throws ConnectorException;
}
