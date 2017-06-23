package com.fintech.oracle.service.apollo.job;

import com.fintech.oracle.dataabstraction.entities.*;
import com.fintech.oracle.dataabstraction.repository.ResourceNameOcrExtractionFieldRepository;
import com.fintech.oracle.dataabstraction.repository.ResourceNameRepository;
import com.fintech.oracle.dto.jni.ZvImage;
import com.fintech.oracle.dto.messaging.ProcessingJobMessage;
import com.fintech.oracle.service.apollo.connector.Connector;
import com.fintech.oracle.service.apollo.connector.ConnectorType;
import com.fintech.oracle.service.apollo.connector.abbyy.task.Task;
import com.fintech.oracle.service.apollo.connector.factory.ConnectorFactory;
import com.fintech.oracle.service.apollo.exception.JobException;
import com.fintech.oracle.service.apollo.exception.connector.ConnectorException;
import com.fintech.oracle.service.apollo.extractor.ResultExtractor;
import com.fintech.oracle.service.apollo.extractor.factory.ResultExtractorFactory;
import com.fintech.oracle.service.apollo.jni.JNIImageProcessor;
import com.fintech.oracle.service.apollo.transformer.ResultTransformer;
import com.fintech.oracle.service.apollo.transformer.abbyy.pojo.Document;
import com.fintech.oracle.service.apollo.transformer.factory.ResultTransformerFactory;
import com.fintech.oracle.service.common.exception.ConfigurationDataNotFoundException;
import com.fintech.oracle.service.common.exception.DataNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by sasitha on 12/22/16.
 *
 */
@Component
public class GeneralJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralJob.class);
    private static final String PROCESSING_SUCCESS_STATUS = "processing_successful";
    private static final String PROCESSING_FAILED_STATUS = "processing_failed";

    @Autowired
    private JobDetailService jobDetailService;

    @Autowired
    private JNIImageProcessor jniImageProcessor;

    @Autowired
    private ResourceNameRepository resourceNameRepository;

    @Autowired
    private ResourceNameOcrExtractionFieldRepository resourceNameOcrExtractionFieldRepository;

    @Autowired
    private ConnectorFactory connectorFactory;

    @Autowired
    private ResultTransformerFactory resultTransformerFactory;

    @Autowired
    private ResultExtractorFactory resultExtractorFactory;

    @javax.annotation.Resource(name = "abbyyCloudAPIConfigurations")
    private Map<String,String>  abbyyCloudAPIConfigurations;


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
                saveProcessedImages(jobMessage, resultImage);

                if (resultImage.getError().isEmpty()){
                    processImagesWithAbby(jobMessage, process, resourceName, resultImage);
                    processWithLocalOcr(process, resourceName, resultImage);
                }else {
                    jobDetailService.updateOcrProcessStatus(process, PROCESSING_FAILED_STATUS);
                }
            }
        }catch (ConfigurationDataNotFoundException  e){
            jobDetailService.updateOcrProcessStatus(process, PROCESSING_FAILED_STATUS);
            throw new JobException("Could not find required configuration data for resource with id : " +
                    "" + jobMessage.getResourceId() + " and resource name : " + resource.getResourceName(), e);
        } catch (ConnectorException e) {
            jobDetailService.updateOcrProcessStatus(process, PROCESSING_FAILED_STATUS);
            throw new JobException("Error occurred while processing documents using ABBYY API. ocr process id : " +
            jobMessage.getOcrProcessId() + " resource id : " + jobMessage.getResourceId());
        } catch (IOException e) {
            jobDetailService.updateOcrProcessStatus(process, PROCESSING_FAILED_STATUS);
            throw new JobException("Unable to get image data ", e);
        }
    }

    private void processWithLocalOcr(OcrProcess process, ResourceName resourceName,
                                     ZvImage resultImage){
        ResultExtractor<String> resultExtractor = resultExtractorFactory.getResultExtractor(ConnectorType.LOCAL);
        List<OcrResult> ocrResultList = new ArrayList<>();
        ocrResultList.addAll(resultExtractor.extractOcrResultSet(resultImage.getOutput(), process, resourceName, ""));
        jobDetailService.saveOcrResults(ocrResultList);
    }

    private void processImagesWithAbby(ProcessingJobMessage jobMessage, OcrProcess process, ResourceName resourceName,
                                       ZvImage resultImage) throws ConnectorException, JobException {
        Map<String,String> connectorConfigurations = new HashMap<>();
        connectorConfigurations.putAll(abbyyCloudAPIConfigurations);
        Connector<Task> abbyConnector =  connectorFactory.getConnector(ConnectorType.ABBYY);
        abbyConnector.setConfigurations(connectorConfigurations);

        Map<String, String> processingConfiguration = new HashMap<>();
        processingConfiguration.put("fileName", jobMessage.getResourceId()+".jpg");
        CompletableFuture<Task> abbyResultForNonPreProcessedImage =
                abbyConnector.submitForProcessing(resultImage.getPreProcessedImage(),
                        processingConfiguration);
        CompletableFuture<Task> abbyyResultsForPreProcessedImage = abbyConnector.submitForProcessing(
                resultImage.getPreProcessedImage(), processingConfiguration
        );
        CompletableFuture.allOf(abbyResultForNonPreProcessedImage,abbyyResultsForPreProcessedImage).join();
        try {
            LOGGER.info("Completed processing task : {} ", abbyResultForNonPreProcessedImage.get());

            ResultTransformer<Document, String> resultTransformer =
                    resultTransformerFactory.getResultTransformer(ConnectorType.ABBYY);

            List<OcrResult> ocrResultList = new ArrayList<>();
            ResultExtractor<Document> abbyOcrResultExtractor =
                    resultExtractorFactory.getResultExtractor(ConnectorType.ABBYY);
            if(abbyResultForNonPreProcessedImage.get().getResultString() != null){
                Document nonePreProcesseImageResults =
                        resultTransformer.transformResults(
                                abbyResultForNonPreProcessedImage.get().getResultString());
                LOGGER.info("none pre processed result document {}", nonePreProcesseImageResults);
                ocrResultList.addAll(abbyOcrResultExtractor.extractOcrResultSet(nonePreProcesseImageResults,
                        process, resourceName, "NPP"));
            }
            if(abbyyResultsForPreProcessedImage.get().getResultString() != null){
                Document preProcesseImageResults = resultTransformer.transformResults(
                        abbyyResultsForPreProcessedImage.get().getResultString());
                LOGGER.info("pre processed result document {}", preProcesseImageResults);
                ocrResultList.addAll(abbyOcrResultExtractor.extractOcrResultSet(preProcesseImageResults,
                        process, resourceName, "PP"));
            }
            jobDetailService.saveOcrResults(ocrResultList);
            if (abbyyResultsForPreProcessedImage.get().getResultString() == null &&
                    abbyResultForNonPreProcessedImage.get().getResultString() == null ){
                jobDetailService.updateOcrProcessStatus(process, PROCESSING_FAILED_STATUS);
            }else {
                jobDetailService.updateOcrProcessStatus(process, PROCESSING_SUCCESS_STATUS);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new JobException("Error while processing resource ", e);
        }
    }

    private void saveProcessedImages(ProcessingJobMessage jobMessage, ZvImage resultImage){
        boolean saveProcessedImages = Boolean.parseBoolean(System.getProperty("saveProcessedImages"));
        String processedImageSavingBasePath = System.getProperty("processImageSavingPath");

        if(saveProcessedImages && processedImageSavingBasePath != null){
            String nonePreProcessedImageFileLocation = processedImageSavingBasePath +
                    "/" + jobMessage.getOcrProcessId() + "/" + jobMessage.getResourceId() + "-none-pre-processed.jpg";
            File nonePreProcessedImageFile = new File(nonePreProcessedImageFileLocation);
            nonePreProcessedImageFile.getParentFile().mkdirs();
            String preProcessedImageFileLocation = processedImageSavingBasePath +
                    "/" + jobMessage.getOcrProcessId() + "/" + jobMessage.getResourceId() + "-pre-processed.jpg";
            File preProcessedImageFile = new File(preProcessedImageFileLocation);
            preProcessedImageFile.getParentFile().mkdirs();

            try(FileOutputStream nonePreProcessedImageFileOutputStream = new FileOutputStream(nonePreProcessedImageFile)){
                nonePreProcessedImageFileOutputStream.write(resultImage.getAlignedImage());
            }catch (IOException ex){
                LOGGER.warn("Unable to write the none pre processed image file of job message {} ", jobMessage, ex);
            }
            try(FileOutputStream preProcessedImageOutputStream = new FileOutputStream(preProcessedImageFile)){
                preProcessedImageOutputStream.write(resultImage.getPreProcessedImage());
            }catch (IOException ex){
                LOGGER.warn("Unable to write the pre processed image file of job message {} ", jobMessage, ex);
            }
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
        List<OcrResult> ocrResultList = new ArrayList<>();
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
            ocrResultList.add(ocrResult);

        }
        jobDetailService.saveOcrResults(ocrResultList);
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
