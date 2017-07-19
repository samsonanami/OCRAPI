package com.fintech.oracle.service.apollo.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by sasitha on 1/10/17.
 */
public class GeneralJobTest {

    @InjectMocks
    private GeneralJob generalJob;

    private String resultString;
    private String errorString;
    private String invalidResultString;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        resultString = "Surname##KULARATNE##84%%Given Names##PARINDA SANJAYA##86%%Passport Number##512779062##85%%Nationality##BRITISH CITIZEN##89%%Sex##M##83%%Date of Birth##25 JUL /JUIL 74##92%%Date of Expiry##22 NOV [NOV 22##92%%Issuing Authorithy##IPS##89%%Place of Birth##COLOMBO##83%%Place of Issue##GBR‘##19%%Date of Issue##g2TNoy /Nov 12##0%%MRZ Line1##P<GBRKULARATNE<<PARINDA<SANJAYA<<<<<<<<<<<<<##88%%MRZ Line2##5127790629GBR7407251M2211226<<<<<<<<<<<<<<01+##76%%";
        errorString = "                             Processing Failure##Invalid Template.Please contact the service provider##0%% ";
        invalidResultString = "Surname####-1%%Given Names####%%";
    }

    @Test
    public void getExtractedFieldValue() throws Exception {

    }

    @Test
    public void getExtractedFieldOcrConfidence() throws Exception {

    }

    @Test
    public void should_return_ocr_extractionField_string_from_result_string() throws Exception {
        String extractedString = generalJob.getFieldValueConfidenceFromResultString(resultString, "surname");

        assertEquals("%%Surname##KULARATNE##84%%", extractedString);
    }

    @Test
    public void should_return_error_extraction_field_from_errror_string()throws Exception{
        String extracted = generalJob.getFieldValueConfidenceFromResultString(errorString, "processing_failure");

        assertEquals("%%Processing Failure##Invalid Template.Please contact the service provider##0%%",
                extracted);
    }

    @Test
    public void should_return_the_ocr_field_value()throws Exception{
        String extracted = generalJob.getFieldValueConfidenceFromResultString(errorString, "processing_failure");
        String value = generalJob.getExtractedFieldValue(extracted, "processing_failure");

        assertEquals("Invalid Template.Please contact the service provider", value);
    }

    @Test
    public void should_return_the_ocr_field_value_as_empty_if_raw_string_value_is_empty()throws Exception{
        String extracted = generalJob.getFieldValueConfidenceFromResultString(invalidResultString, "surname");
        String value = generalJob.getExtractedFieldValue(extracted, "surname");

        assertEquals("", value);
    }

    @Test
    public void should_return_the_ocr_confidence_as_zero_if_raw_value_is_invalid()throws Exception{
        String extracted = generalJob.getFieldValueConfidenceFromResultString(invalidResultString, "surname");
        String confidence = generalJob.getExtractedFieldOcrConfidence(extracted, "surname");

        assertEquals("0.0", confidence);
    }

    @Test
    public void should_return_the_ocr_confidence_as_zero_if_raw_value_is_empty()throws Exception{
        String extracted = generalJob.getFieldValueConfidenceFromResultString(invalidResultString, "given_names");
        String confidence = generalJob.getExtractedFieldOcrConfidence(extracted, "given_names");

        assertEquals("0.0", confidence);
    }

}