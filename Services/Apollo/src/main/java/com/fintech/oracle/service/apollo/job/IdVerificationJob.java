package com.fintech.oracle.service.apollo.job;

import com.fintech.oracle.dataabstraction.entities.*;
import com.fintech.oracle.dataabstraction.repository.OcrExtractionFieldRepository;
import com.fintech.oracle.dataabstraction.repository.ResourceNameOcrExtractionFieldRepository;
import com.fintech.oracle.dataabstraction.repository.ResourceNameRepository;
import com.fintech.oracle.dto.jni.ZvImage;
import com.fintech.oracle.dto.messaging.JobResource;
import com.fintech.oracle.dto.messaging.ProcessingJobMessage;
import com.fintech.oracle.service.apollo.exception.JobException;
import com.fintech.oracle.service.apollo.jni.JNIImageProcessor;
import com.fintech.oracle.service.common.exception.ConfigurationDataNotFoundException;
import com.fintech.oracle.service.common.exception.DataNotFoundException;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sasitha on 12/7/16.
 *
 */
@Component
public class IdVerificationJob implements Job{

    private static final Logger LOGGER = LoggerFactory.getLogger(IdVerificationJob.class);

    @Autowired
    private JobDetailService jobDetailService;

    @Autowired
    private JNIImageProcessor jniImageProcessor;

    @Autowired
    private ResourceNameRepository resourceNameRepository;

    @Autowired
    private OcrExtractionFieldRepository ocrExtractionFieldRepository;

    @Autowired
    private ResourceNameOcrExtractionFieldRepository resourceNameOcrExtractionFieldRepository;

    @Autowired
    private String resourceFileBasePath;

    @Override
    public void doJob(Serializable message) throws JobException {
        try {
            ProcessingJobMessage jobMessage = (ProcessingJobMessage)message;
            OcrProcess process = jobDetailService.getOcrProcessDetails(jobMessage.getOcrProcessId());
            for (JobResource resource : jobMessage.getResources()){
                String resourceConfigurationName = "";
                LOGGER.debug("start processing resource with Id {}, name {} " , resource.getResourceId(), resource.getResourceName());
                switch (resource.getResourceName()){
                    case "drivingLicenseFront":
                        resourceConfigurationName = "Driving_License_Front";
                        break;
                    case "drivingLicenseBack":
                        resourceConfigurationName = "Driving_License_Back";
                        break;
                    case "passport":
                        resourceConfigurationName = "Passport";
                        break;
                    case "utilityBill":
                        resourceConfigurationName = "Utility_Bill";
                        break;
                    case "id":
                        resourceConfigurationName = "Identity_Card";
                        break;
                    case "keyedData":
                        resourceConfigurationName = "keyedData";

                }
                if(resourceConfigurationName.isEmpty()){
                    LOGGER.error("Invalid resource name '" + resource.getResourceName() + "' given for the idVerification. This resource will be skipped from getting processed");
                }else if (resourceConfigurationName.equals("keyedData")){
                    String json =  new String(resource.getData());
                    JSONObject jsonObj = new JSONObject(json);
                    saveJsonDetails(jsonObj,  process, resourceNameRepository.findResourceNameByName(resource.getResourceName()));
                }else {
                    ZvImage resultImage = jniImageProcessor.processDocument(resourceConfigurationName, getSourceImage(resource));
                    LOGGER.debug("Received result set from jniImageProcessor " + resultImage.getOutput());
                    LOGGER.debug("Received error set from jniImageProcessor " + resultImage.getError());
                    saveExtractedDetails(resultImage.getOutput(), process, resourceNameRepository.findResourceNameByName(resource.getResourceName()));
                }

            }
        } catch (DataNotFoundException e) {
            throw new JobException(e.getMessage(), e);
        } catch (ConfigurationDataNotFoundException e) {
            throw new JobException(e.getMessage(), e);
        } catch (IOException e) {
            throw new JobException(e.getMessage(), e);
        }
    }

    private void saveJsonDetails(JSONObject json, OcrProcess ocrProcess, ResourceName resourceName) throws ConfigurationDataNotFoundException {
        List<ResourceNameOcrExtractionField> resourceNameOcrExtractionFieldList = resourceNameOcrExtractionFieldRepository.findResourceNameOcrExtractionFieldsByResourceName(resourceName);
        if(resourceNameOcrExtractionFieldList == null){
            throw new ConfigurationDataNotFoundException("No ocr extraction information found for resource name : " + resourceName.getName());
        }
        for (ResourceNameOcrExtractionField extractionField : resourceNameOcrExtractionFieldList){
            String ocrExtractionField = extractionField.getOcrExtractionField().getField();
            OcrResult ocrResult = new OcrResult();
            ocrResult.setOcrProcess(ocrProcess);
            ocrResult.setResourceNameOcrExtractionField(extractionField);
            ocrResult.setValue(json.getString(ocrExtractionField));
            ocrResult.setResultName(resourceName.getName() + "##" +ocrExtractionField);
            jobDetailService.saveOcrResults(ocrResult);
        }
    }

    private void saveExtractedDetails(String resultString, OcrProcess ocrProcess, ResourceName resourceName) throws ConfigurationDataNotFoundException {
        List<ResourceNameOcrExtractionField> resourceNameOcrExtractionFieldList = resourceNameOcrExtractionFieldRepository.findResourceNameOcrExtractionFieldsByResourceName(resourceName);
        if(resourceNameOcrExtractionFieldList == null){
            throw new ConfigurationDataNotFoundException("No ocr extraction information found for resource name : " + resourceName.getName());
        }
        for (ResourceNameOcrExtractionField extractionField : resourceNameOcrExtractionFieldList){
            String ocrExtractionField = extractionField.getOcrExtractionField().getField();
            String fieldValueConfidenceString = getFieldValueConfidenceFromResultString(resultString, ocrExtractionField);
            String extractedValue = getExtractedFieldValue(fieldValueConfidenceString, ocrExtractionField);
            String ocrConfidenceValue = getExtractedFieldOcrConfidence(fieldValueConfidenceString, ocrExtractionField);
            OcrResult ocrResult = new OcrResult();
            ocrResult.setOcrProcess(ocrProcess);
            ocrResult.setResourceNameOcrExtractionField(extractionField);
            ocrResult.setValue(extractedValue);
            ocrResult.setOcrConfidence(Double.valueOf(ocrConfidenceValue));
            ocrResult.setResultName(resourceName.getName() + "##" +ocrExtractionField);
            jobDetailService.saveOcrResults(ocrResult);
        }
    }

    @Transactional
    private String getExtractedFieldValue(String fieldValueConfidenseString, String field){
        String pattern = "##(.*?)##";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fieldValueConfidenseString);
        String result = "";
        if(m.find()){
            result = m.group(0);
        }
        result = result.replace("##", "");
        return result;
    }

    @Transactional
    private String getExtractedFieldOcrConfidence(String fieldValueConfidenseString, String field){
        String pattern = "##([0-9]+\\.?[0-9]+)%%";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fieldValueConfidenseString);
        String result = "";
        if(m.find()){
            result = m.group(0);
        }
        result = result.replace("%%", "");
        result = result.replace("##", "");
        return result;
    }

    @Transactional
    private String getFieldValueConfidenceFromResultString(String resultString, String field){
        String pattern = "(?i)%%("+ field.replace("_", " ") +")(.*?)%%";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher("%%"+resultString);
        String result = "";
        if(m.find()){
            result = m.group(0);
        }
        return result;
    }
    private ZvImage getSourceImage(JobResource resource) throws IOException {
        ZvImage zvImage = new ZvImage();
        zvImage.setError("");
        zvImage.setOutput("");
        zvImage.setData(resource.getData());
        return zvImage;
    }

    private static byte[] getRawBytesFromFile(String path) throws IOException {

        byte[] image;
        File file = new File(path);
        image = new byte[(int)file.length()];

        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(image);

        return image;
    }
}
