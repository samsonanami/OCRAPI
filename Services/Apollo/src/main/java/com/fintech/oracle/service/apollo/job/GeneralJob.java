package com.fintech.oracle.service.apollo.job;

import com.fintech.oracle.dataabstraction.entities.*;
import com.fintech.oracle.dataabstraction.repository.ResourceNameOcrExtractionFieldRepository;
import com.fintech.oracle.dataabstraction.repository.ResourceNameRepository;
import com.fintech.oracle.dto.jni.ZvImage;
import com.fintech.oracle.dto.messaging.ProcessingJobMessage;
import com.fintech.oracle.service.apollo.exception.JobException;
import com.fintech.oracle.service.apollo.jni.JNIImageProcessor;
import com.fintech.oracle.service.common.exception.ConfigurationDataNotFoundException;
import com.fintech.oracle.service.common.exception.DataNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sasitha on 12/22/16.
 *
 */
@Component
public class GeneralJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralJob.class);
    private static final String PROCESSING_SUCCESS_STATUS = "processing_successful";
    private static final String PROCESSING_FAILD_STATUS = "processing_failed";

    @Autowired
    private JobDetailService jobDetailService;

    @Autowired
    private JNIImageProcessor jniImageProcessor;

    @Autowired
    private ResourceNameRepository resourceNameRepository;

    @Autowired
    private ResourceNameOcrExtractionFieldRepository resourceNameOcrExtractionFieldRepository;


    public void startProcessing(Serializable message) throws JobException {
        try {
            ProcessingJobMessage jobMessage = (ProcessingJobMessage)message;
            OcrProcess process = jobDetailService.getOcrProcessDetails(jobMessage.getOcrProcessId());
            Resource resource = jobDetailService.getResourceDetails(jobMessage.getResourceId());
            processResource(jobMessage, process, resource);
        } catch (DataNotFoundException e) {
            throw new JobException(e.getMessage(), e);
        }
    }

    private void processResource(ProcessingJobMessage jobMessage, OcrProcess process, Resource resource) throws JobException {
        try{

            LOGGER.debug("Start processing resource with id {}, name {}",
                    jobMessage.getResourceId(), jobMessage.getResourceName());
            ResourceName resourceName =
                    resourceNameRepository.findResourceNameByName(jobMessage.getResourceName());
            String resourceConfigurationName = resourceName.getConfigFilePath();
            if(resourceConfigurationName.isEmpty()){
                LOGGER.error("Invalid resource name '" + resource.getResourceName() +
                        "' given for the ID_VERIFICATION. This resource will be skipped from getting processed");
            }else if ("KeyedData".equalsIgnoreCase(resourceConfigurationName)){
                String json =  new String(jobMessage.getImageData());
                JSONObject jsonObj = new JSONObject(json);
                LOGGER.debug("Saving details received from json file {}", jsonObj);
                saveJsonDetails(jsonObj,  process, resourceName);
                jobDetailService.updateOcrProcessStatus(process, PROCESSING_SUCCESS_STATUS);
            }else {
                ZvImage resultImage = jniImageProcessor.processImage(resourceConfigurationName,
                        getSourceImage(jobMessage));
                LOGGER.debug("Received processed results from dll {}", resultImage);
                LOGGER.debug("Received result set from jniImageProcessor " + resultImage.getOutput());
                LOGGER.debug("Received error set from jniImageProcessor " + resultImage.getError());
                if(resultImage.getError().isEmpty()){
                    saveExtractedDetails(resultImage.getOutput(), process, resourceName);
                    jobDetailService.updateOcrProcessStatus(process, PROCESSING_SUCCESS_STATUS);
                }else {
                    saveExtractedDetails(resultImage.getError(), process, resourceName);
                    jobDetailService.updateOcrProcessStatus(process, PROCESSING_FAILD_STATUS);
                }
            }
        }catch (ConfigurationDataNotFoundException e){
            jobDetailService.updateOcrProcessStatus(process, PROCESSING_FAILD_STATUS);
            throw new JobException("Could not find required configuration data for resource with id : " +
                    "" + jobMessage.getResourceId() + " and resource name : " + resource.getResourceName(), e);
        } catch (IOException e) {
            jobDetailService.updateOcrProcessStatus(process, PROCESSING_FAILD_STATUS);
            throw new JobException("Error occurred while processing resource id " +
                    "" + jobMessage.getResourceId() + " and resource name : " + resource.getResourceName(), e);
        }
    }

    public  void saveJsonDetails(JSONObject json, OcrProcess ocrProcess, ResourceName resourceName)
            throws ConfigurationDataNotFoundException {
        List<ResourceNameOcrExtractionField> resourceNameOcrExtractionFieldList =
                resourceNameOcrExtractionFieldRepository.findResourceNameOcrExtractionFieldsByResourceName(resourceName);
        if(resourceNameOcrExtractionFieldList == null){
            throw new ConfigurationDataNotFoundException("No ocr extraction information found for resource name : "
                    + resourceName.getName());
        }
        for (ResourceNameOcrExtractionField extractionField : resourceNameOcrExtractionFieldList){

            String ocrExtractionField = extractionField.getOcrExtractionField().getField();
            OcrResult ocrResult = new OcrResult();
            String dataFromJson = "";
            try{
                dataFromJson  = json.getString(ocrExtractionField);
            }catch (JSONException e){
                LOGGER.warn("No data in the json file found for the extraction field {} in ocr process {} " +
                        "and will be replaced by an empty string ", extractionField, ocrProcess, e);
            }
            ocrResult.setOcrProcess(ocrProcess);
            ocrResult.setResourceNameOcrExtractionField(extractionField);
            ocrResult.setValue(dataFromJson);
            ocrResult.setResultName(resourceName.getName() + "##" +ocrExtractionField);

            LOGGER.debug("saving results {}", ocrResult);
            jobDetailService.saveOcrResults(ocrResult);
        }
    }

    public void saveExtractedDetails(String resultString, OcrProcess ocrProcess, ResourceName resourceName)
            throws ConfigurationDataNotFoundException {
        List<ResourceNameOcrExtractionField> resourceNameOcrExtractionFieldList =
                resourceNameOcrExtractionFieldRepository.findResourceNameOcrExtractionFieldsByResourceName(resourceName);
        if(resourceNameOcrExtractionFieldList == null){
            throw new ConfigurationDataNotFoundException("No ocr extraction information found for resource name : " +
                    resourceName.getName());
        }
        for (ResourceNameOcrExtractionField extractionField : resourceNameOcrExtractionFieldList){
            String ocrExtractionField = extractionField.getOcrExtractionField().getField();
            String fieldValueConfidenceString = getFieldValueConfidenceFromResultString(resultString, ocrExtractionField);
            String extractedValue = getExtractedFieldValue(fieldValueConfidenceString, ocrExtractionField);
            String ocrConfidenceValue = getExtractedFieldOcrConfidence(fieldValueConfidenceString, ocrExtractionField);
            if(ocrConfidenceValue.isEmpty()){
                ocrConfidenceValue = "0";
            }
            OcrResult ocrResult = new OcrResult();
            ocrResult.setOcrProcess(ocrProcess);
            ocrResult.setResourceNameOcrExtractionField(extractionField);
            ocrResult.setValue(extractedValue);
            ocrResult.setOcrConfidence(Double.valueOf(ocrConfidenceValue));
            ocrResult.setResultName(resourceName.getName() + "##" +ocrExtractionField);
            LOGGER.debug("saving results {}", ocrResult);
            jobDetailService.saveOcrResults(ocrResult);
        }
    }

    @Transactional
    public String getExtractedFieldValue(String fieldValueConfidenceString, String field){
        String pattern = "##(.*?)##";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fieldValueConfidenceString.trim());
        String result = "";
        if(m.find()){
            result = m.group(0);
        }
        result = result.replace("##", "");
        return result;
    }

    @Transactional
    public String getExtractedFieldOcrConfidence(String fieldValueConfidenceString, String field){
        String pattern = "##([0-9]+\\.?[0-9]+)%%";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fieldValueConfidenceString.trim());
        String result = "";
        if(m.find()){
            result = m.group(0);
        }
        result = result.replace("%%", "");
        result = result.replace("##", "");
        return result;
    }

    @Transactional
    public String getFieldValueConfidenceFromResultString(String resultString, String field){
        String pattern = "(?i)%%("+ field.replace("_", " ") +")(.*?)%%";

        Pattern r = Pattern.compile(pattern);
        String trimmedResultString = resultString.trim();
        Matcher m = r.matcher("%%"+trimmedResultString);
        String result = "";
        if(m.find()){
            result = m.group(0);
        }
        return result;
    }
    public ZvImage getSourceImage(ProcessingJobMessage resource) throws IOException {
        ZvImage zvImage = new ZvImage();
        zvImage.setError("");
        zvImage.setOutput("");
        if (resource.getImageData() == null){
            throw new IOException("Resource byte array could not be null ");
        }
        zvImage.setData(resource.getImageData());
        return zvImage;
    }
}
