package com.fintech.oracle.service.apollo.connector.transmission.request;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.BaseRequest;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by sasitha on 6/20/17.
 *
 */
public abstract class AbstractRequestBuilder implements RequestBuilder{

    @Override
    public BaseRequest buildPostRequest(Map<String, String> configurations, Map<String, Object> content) {
        Assert.notNull(configurations, "Configurations cannot be null");
        String body = (String)content.get("body");

        return Unirest.post(configurations.get("url"))
                .body(body);
    }

    @Override
    public BaseRequest buildGetRequest(Map<String, String> configurations, Map<String, Object> content) {
        Assert.notNull(configurations, "Configurations cannot be null");
        return Unirest.get(configurations.get("url"));
    }

    @Override
    public BaseRequest buildGetRequestWithoutAuthentication(Map<String, String> configurations, Map<String, Object> content) {
        return Unirest.get(configurations.get("url"));
    }
}
