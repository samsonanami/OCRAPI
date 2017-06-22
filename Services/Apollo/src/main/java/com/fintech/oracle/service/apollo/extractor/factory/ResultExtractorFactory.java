package com.fintech.oracle.service.apollo.extractor.factory;

import com.fintech.oracle.service.apollo.connector.ConnectorType;
import com.fintech.oracle.service.apollo.extractor.ResultExtractor;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by sasitha on 6/22/17.
 *
 */

public class ResultExtractorFactory {

    @Resource(name = "ocrResultExtractorConfigurations")
    private Map<String, ResultExtractor> ocrResultExtractorConfigurations;

    public ResultExtractor getResultExtractor(ConnectorType connectorType){
        return ocrResultExtractorConfigurations.get(connectorType.toString());
    }
}
