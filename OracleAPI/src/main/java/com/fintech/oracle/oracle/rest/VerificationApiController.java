package com.fintech.oracle.oracle.rest;

import com.fintech.oracle.dto.request.VerificationRequest;
import com.fintech.oracle.dto.response.OcrResponse;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-02T04:51:04.641Z")

@Controller
public class VerificationApiController implements VerificationApi {

    public ResponseEntity<Object> addVerification(@ApiParam(value = "Verification request" ,required=true ) @RequestBody VerificationRequest body) {
        // do some magic!
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    public ResponseEntity<OcrResponse> getByVerificationId(@ApiParam(value = "id of the verification request data need to be fetched.",required=true ) @PathVariable("verificationId") String verificationId) {
        // do some magic!
        return new ResponseEntity<OcrResponse>(HttpStatus.OK);
    }

}
