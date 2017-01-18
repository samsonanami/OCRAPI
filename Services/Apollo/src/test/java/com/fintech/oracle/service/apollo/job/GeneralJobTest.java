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
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        resultString = "Surname##KULARATNE##84%%Given Names##PARINDA SANJAYA##86%%Passport Number##512779062##85%%Nationality##BRITISH CITIZEN##89%%Sex##M##83%%Date of Birth##25 JUL /JUIL 74##92%%Date of Expiry##22 NOV [NOV 22##92%%Issuing Authorithy##IPS##89%%Place of Birth##COLOMBO##83%%Place of Issue##GBR‘##19%%Date of Issue##g2TNoy /Nov 12##0%%MRZ Line1##P<GBRKULARATNE<<PARINDA<SANJAYA<<<<<<<<<<<<<##88%%MRZ Line2##5127790629GBR7407251M2211226<<<<<<<<<<<<<<01+##76%%";
        errorString = "                             Processing Failure##Invalid Template.Please contact the service provider##0%% ";
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

}