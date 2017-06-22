package com.fintech.oracle.service.apollo.connector.transmission.body.builder;

import java.util.Map;

/**
 * Created by sasitha on 6/20/17.
 *
 */
public interface BodyBuilder {
    String getRequestBody(Map<String, Object> bodyContent);
}
