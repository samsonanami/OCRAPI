package com.fintech.oracle.oracle.rest;

import com.fintech.oracle.dto.request.VerificationRequest;
import com.fintech.oracle.dto.response.OcrResponse;
import com.fintech.oracle.dto.response.Response;
import com.fintech.oracle.dto.response.VerificationProcessResponse;
import com.fintech.oracle.service.api.request.ProcessingRequestService;
import com.fintech.oracle.service.common.exception.ConfigurationDataNotFoundException;
import com.fintech.oracle.service.common.exception.DataNotFoundException;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.jms.JMSException;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-02T04:51:04.641Z")

@Controller
public class VerificationApiController implements VerificationApi {

    private static Logger LOGGER = LoggerFactory.getLogger(VerificationApiController.class);

    @Autowired
    private  ProcessingRequestService processingRequestService;

    public ResponseEntity<Object> addVerification(@ApiParam(value = "Verification request" ,required=true ) @RequestBody VerificationRequest body) {
        VerificationProcessResponse successfullResponse = null;
        ResponseEntity<Object> responseEntity = null;
        Response errorResponse = new Response();
        try {
            successfullResponse = processingRequestService.saveProcessingRequest(body);
            responseEntity = new ResponseEntity<Object>(successfullResponse, HttpStatus.OK);
        } catch (ConfigurationDataNotFoundException e) {
            LOGGER.error("Error", e);
            errorResponse.setMessage("Internal server error");
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseEntity = new ResponseEntity<Object>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataNotFoundException e) {
            LOGGER.error("Error", e);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            responseEntity = new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    public ResponseEntity<Object> getByVerificationId(@ApiParam(value = "id of the verification request data need to be fetched.",required=true ) @PathVariable("verificationId") String verificationId) {
        OcrResponse successfullResponse = null;
        ResponseEntity<Object> responseEntity = null;
        Response errorResponse = new Response();

        try {
            successfullResponse = processingRequestService.getProcessingResult(verificationId);
            responseEntity = new ResponseEntity<Object>(successfullResponse, HttpStatus.OK);
        } catch (DataNotFoundException e) {
            LOGGER.error("Error", e);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            responseEntity = new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

}
