package com.fintech.oracle.service.apollo.connector.factory;

import com.fintech.oracle.service.apollo.connector.Connector;
import com.fintech.oracle.service.apollo.connector.ConnectorType;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by sasitha on 6/20/17.
 *
 */

public class ConnectorFactory {

    @Resource(name="connectorMap")
    private Map<String, Connector> connectorMap;

    public Connector getConnector(ConnectorType connectorType){
        Assert.notNull(connectorType, "Connector type cannot be null");
        return connectorMap.get(connectorType.toString());
    }
}
