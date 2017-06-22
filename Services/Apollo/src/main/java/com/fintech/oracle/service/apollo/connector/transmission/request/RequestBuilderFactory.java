package com.fintech.oracle.service.apollo.connector.transmission.request;

import com.fintech.oracle.service.apollo.connector.ConnectorType;
import com.mashape.unirest.request.BaseRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by sasitha on 6/20/17.
 *
 */
@Component
public class RequestBuilderFactory {

    public RequestBuilder getRequestBuilder(ConnectorType connectorType){
        Assert.notNull(connectorType, "Connector tpe cannot be null");
        RequestBuilder requestBuilder = new AbstractRequestBuilder() {
            @Override
            public BaseRequest buildPostRequest(Map<String, String> configurations, Map<String, Object> content) {
                return super.buildPostRequest(configurations, content);
            }

            @Override
            public BaseRequest buildGetRequest(Map<String, String> configurations, Map<String, Object> content) {
                return super.buildGetRequest(configurations, content);
            }

            @Override
            public BaseRequest buildGetRequestWithoutAuthentication(Map<String, String> configurations, Map<String, Object> content) {
                return super.buildGetRequestWithoutAuthentication(configurations,content);
            }
        };

        if(connectorType.equals(ConnectorType.ABBYY)){
            requestBuilder = new AbbyyRequestBuilder();
        }
        return requestBuilder;
    }
}
