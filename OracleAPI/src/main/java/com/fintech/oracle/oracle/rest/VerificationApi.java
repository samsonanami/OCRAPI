package com.fintech.oracle.oracle.rest;

import com.fintech.oracle.dto.request.VerificationRequest;
import com.fintech.oracle.dto.response.OcrResponse;
import com.fintech.oracle.dto.response.Response;
import com.fintech.oracle.dto.response.VerificationProcessResponse;
import io.swagger.annotations.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-02T04:51:04.641Z")

@Api(value = "verification", description = "the verification API")
public interface VerificationApi {

    @ApiOperation(value = "Submit new processing request", notes = "This API request allows users to submit new processing requests to OCR API. A processing request may have one ore more verification process included in it.  A verification process need to have resources. Resources are the content which  will go through the verification porcess. ", response = Object.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = Object.class),
        @ApiResponse(code = 400, message = "Invalid json structure.", response = Object.class),
        @ApiResponse(code = 401, message = "Unauthorized to acess the service.", response = Object.class),
        @ApiResponse(code = 404, message = "One ore more resources could not found", response = Object.class) })
    @RequestMapping(value = "/v1/verification",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Object> addVerification(@ApiParam(value = "Verification request", required = true) @RequestBody VerificationRequest body);


    @ApiOperation(value = "Get details of a processing request.", notes = "This API call allows users to get the status and the details regarding a processing request they have earlier submitted. ", response = OcrResponse.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = OcrResponse.class),
        @ApiResponse(code = 400, message = "No ID or invalid id submitted", response = Response.class),
        @ApiResponse(code = 401, message = "Not authorized to access", response = Response.class),
        @ApiResponse(code = 404, message = "Requested verification request not fond", response = Response.class) })
    @RequestMapping(value = "/v1/verification/{verificationId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Object> getByVerificationId(@ApiParam(value = "id of the verification request data need to be fetched.", required = true) @PathVariable("verificationId") String verificationId);
}
