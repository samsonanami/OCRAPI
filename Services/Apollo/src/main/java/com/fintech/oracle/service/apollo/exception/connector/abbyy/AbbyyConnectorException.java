package com.fintech.oracle.service.apollo.exception.connector.abbyy;

import com.fintech.oracle.service.apollo.exception.connector.ConnectorException;

/**
 * Created by sasitha on 6/21/17.
 *
 */
public class AbbyyConnectorException extends ConnectorException {
    public AbbyyConnectorException(String message) {
        super(message);
    }

    public AbbyyConnectorException(String message, Exception ex) {
        super(message, ex);
    }

    public AbbyyConnectorException(Exception ex) {
        super(ex);
    }
}
