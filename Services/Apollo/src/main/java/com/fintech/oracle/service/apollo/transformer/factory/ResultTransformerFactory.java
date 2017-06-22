package com.fintech.oracle.service.apollo.transformer.factory;

import com.fintech.oracle.service.apollo.connector.ConnectorType;
import com.fintech.oracle.service.apollo.transformer.ResultTransformer;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by sasitha on 6/22/17.
 *
 */
public class ResultTransformerFactory {

    @Resource(name="resultTransformerMap")
    private Map<String, ResultTransformer> resultTransformerMap;


    public ResultTransformer getResultTransformer(ConnectorType connectorType){
        return resultTransformerMap.get(connectorType.toString());
    }
}
