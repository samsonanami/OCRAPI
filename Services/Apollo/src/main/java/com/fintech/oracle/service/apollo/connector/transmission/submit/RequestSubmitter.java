package com.fintech.oracle.service.apollo.connector.transmission.submit;

import com.fintech.oracle.service.apollo.exception.request.FailedRequestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.BaseRequest;

/**
 * Created by sasitha on 6/20/17.
 *
 */
public interface RequestSubmitter {

    HttpResponse<String> submitRequest(BaseRequest baseRequest) throws FailedRequestException;
}
