package com.fintech.oracle.service.apollo.connector.transmission.request;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.BaseRequest;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * Created by sasitha on 6/20/17.
 *
 */
public class AbbyyRequestBuilder extends AbstractRequestBuilder implements RequestBuilder {
    @Override
    public BaseRequest buildPostRequest(Map<String, String> configurations, Map<String, Object> content) {
        return Unirest.post(configurations.get("url"))
                .header("Authorization", configurations.get("authorization"))
                .field("file", new ByteArrayInputStream((byte[]) content.get("fileData")),
                        ContentType.APPLICATION_OCTET_STREAM, (String)content.get("fileName"));
    }

    @Override
    public BaseRequest buildGetRequest(Map<String, String> configurations, Map<String, Object> content) {
        return Unirest.get(configurations.get("url"))
                .header("Authorization", configurations.get("authorization"));
    }

    public BaseRequest buildGetRequestWithoutAuthentication(Map<String, String> configurations, Map<String, Object> content){
        return Unirest.get(configurations.get("url"));
    }
}
