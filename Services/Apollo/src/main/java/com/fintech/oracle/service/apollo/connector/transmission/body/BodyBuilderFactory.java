package com.fintech.oracle.service.apollo.connector.transmission.body;

import com.fintech.oracle.service.apollo.connector.ConnectorType;
import com.fintech.oracle.service.apollo.connector.transmission.body.builder.BodyBuilder;
import com.fintech.oracle.service.apollo.connector.transmission.body.builder.BodyBuilderImpl;
import com.fintech.oracle.service.apollo.connector.transmission.body.builder.abbyy.AbbyyRequestBodyBuilderImpl;
import org.springframework.util.Assert;

/**
 * Created by sasitha on 6/20/17.
 *
 */


public class BodyBuilderFactory {
    BodyBuilder getBodyBuilder(ConnectorType connectorType){
        Assert.notNull(connectorType, "Connector type cannot be null");
        BodyBuilder bodyBuilder = new BodyBuilderImpl();
        if(connectorType.equals(ConnectorType.ABBYY)){
            bodyBuilder = new AbbyyRequestBodyBuilderImpl();
        }
        return bodyBuilder;
    }
}
