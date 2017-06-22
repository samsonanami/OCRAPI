package com.fintech.oracle.service.apollo.connector.transmission.request;

import com.mashape.unirest.request.BaseRequest;

import java.util.Map;

/**
 * Created by sasitha on 6/20/17.
 *
 */
public interface RequestBuilder {

    BaseRequest buildPostRequest(Map<String, String> configurations, Map<String, Object> content);

    BaseRequest buildGetRequest(Map<String, String> configurations, Map<String, Object> content);

    BaseRequest buildGetRequestWithoutAuthentication(Map<String, String> configurations, Map<String, Object> content);
}
