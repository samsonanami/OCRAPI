package com.fintech.oracle.service.apollo.connector.transmission.submit;

import com.fintech.oracle.service.apollo.exception.request.FailedRequestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;

/**
 * Created by sasitha on 6/21/17.
 *
 */
public class RequestSubmitterImpl implements RequestSubmitter {
    @Override
    public HttpResponse<String> submitRequest(BaseRequest baseRequest) throws FailedRequestException {
        try {
            return baseRequest.asString();
        } catch (UnirestException e) {
            throw new FailedRequestException("Request submission fail, ", e);
        }
    }
}
