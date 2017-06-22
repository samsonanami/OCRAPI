package com.fintech.oracle.service.apollo.connector.abbyy;

import com.fintech.oracle.service.apollo.connector.ConnectorType;
import com.fintech.oracle.service.apollo.connector.abbyy.config.roi.factory.RoiConfigurationFileFactory;
import com.fintech.oracle.service.apollo.connector.abbyy.config.roi.reader.RoiConfigurationReader;
import com.fintech.oracle.service.apollo.connector.abbyy.task.Task;
import com.fintech.oracle.service.apollo.connector.abbyy.task.TaskStatus;
import com.fintech.oracle.service.apollo.connector.transmission.request.RequestBuilder;
import com.fintech.oracle.service.apollo.connector.transmission.request.RequestBuilderFactory;
import com.fintech.oracle.service.apollo.connector.transmission.submit.RequestSubmitter;
import com.fintech.oracle.service.apollo.exception.config.ConfigurationFileReaderException;
import com.fintech.oracle.service.apollo.exception.connector.abbyy.AbbyyConnectorException;
import com.fintech.oracle.service.apollo.exception.request.FailedRequestException;
import com.fintech.oracle.service.apollo.exception.task.TaskParseException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.BaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by sasitha on 6/20/17.
 *
 */
@Service
public class AbbyyConnectorImpl implements AbbyyConnector {
    private static Logger LOGGER = LoggerFactory.getLogger(AbbyyConnectorImpl.class);
    private final int WAITING_TIME_TO_CHECK_TASK_COMPLETION = 2000;
    @Autowired
    private RequestBuilderFactory requestBuilderFactory;

    @Autowired
    private RequestSubmitter requestSubmitter;

    @Autowired
    private RoiConfigurationFileFactory roiConfigurationFileFactory;


    private Map<String, String> connectorConfigurations;
    @Override
    public void setConfigurations(Map<String, String> configurations) {
        this.connectorConfigurations = configurations;
    }

    @Override
    @Async
    public CompletableFuture<Task> submitForProcessing(byte[] image, Map<String, String> processingConfigurations) throws AbbyyConnectorException {
        LOGGER.info("Start submitting for processing ...");
        String result = "";
        Task task = new Task();
        RequestBuilder requestBuilder = requestBuilderFactory.getRequestBuilder(ConnectorType.ABBYY);
        Map<String, Object> fileUploadContent = new HashMap<String,Object>();
        fileUploadContent.putAll(processingConfigurations);
        fileUploadContent.put("fileData", image);
        fileUploadContent.put("content-type", "application/octet-stream");
        connectorConfigurations.put("url",connectorConfigurations.get("baseUrl") + "/submitImage");
        connectorConfigurations.put("authorization", getAuthorizationHeader());
        BaseRequest imageUploadRequest = requestBuilder.buildPostRequest(connectorConfigurations,fileUploadContent);
        try {
            HttpResponse<String> imageUploadResponse = requestSubmitter.submitRequest(imageUploadRequest);
            result = imageUploadResponse.getBody();
            task = Task.parseTask(result);
            String configurationFilePath = roiConfigurationFileFactory.getConfigurationFilePath("DEFAULT");
            byte[] configurationFileData = readConfigurationFileData(configurationFilePath);
            String processingResults = submitTaskForProcessing(requestBuilder, task, configurationFileData);
            task = waitForTaskCompletion(requestBuilder, processingResults);
            if(task.getTaskStatus().equals(TaskStatus.Completed)){
                getResultsFromCompletedTask(requestBuilder, task);
            }
        } catch (FailedRequestException e) {
            throw new AbbyyConnectorException("Failed to send the request ", e);
        } catch (TaskParseException e) {
            throw new AbbyyConnectorException("Error parsing response to a Task object ", e);
        } catch (ConfigurationFileReaderException | IOException e) {
            throw new AbbyyConnectorException("Unable to read configuration file with ROI details", e);
        } catch (InterruptedException e) {
            throw new AbbyyConnectorException("Thread is interrupted while waiting for task to be completed", e);
        }
        LOGGER.info("Done processing ....");
        return CompletableFuture.completedFuture(task);
    }

    private String submitTaskForProcessing(RequestBuilder requestBuilder, Task task, byte[] configurationFileData) throws FailedRequestException {
        Map<String, Object> processingRequestContent = new HashMap<>();
        processingRequestContent.put("fileData", configurationFileData);
        processingRequestContent.put("fileName", "default-roi-configuration.xml");
        processingRequestContent.put("content-type", "application/octet-stream");

        connectorConfigurations.put("url",connectorConfigurations.get("baseUrl") + "/processFields?taskId="+task.getTaskId());

        BaseRequest processingRequest = requestBuilder.buildPostRequest(connectorConfigurations, processingRequestContent);

        HttpResponse<String> processingResponse = requestSubmitter.submitRequest(processingRequest);
        return processingResponse.getBody();
    }

    private byte[] readConfigurationFileData(String configurationFilePath) throws ConfigurationFileReaderException, IOException {
        RoiConfigurationReader configurationReader = new RoiConfigurationReader();
        return configurationReader.readDataFromConfigurationFile(configurationFilePath);
    }

    private void getResultsFromCompletedTask(RequestBuilder requestBuilder, Task task) throws FailedRequestException {
        connectorConfigurations.put("url", task.getResultUrl());
        BaseRequest resultRequest = requestBuilder.buildGetRequestWithoutAuthentication(connectorConfigurations, null);
        HttpResponse<String> resultResponse = requestSubmitter.submitRequest(resultRequest);
        task.setResultString(resultResponse.getBody());
    }

    private Task waitForTaskCompletion(RequestBuilder requestBuilder, String processingResults) throws TaskParseException, InterruptedException, FailedRequestException {
        Task task;
        task = Task.parseTask(processingResults);
        while (task.isTaskActive()){
            Thread.sleep(WAITING_TIME_TO_CHECK_TASK_COMPLETION);
            connectorConfigurations.put("url",connectorConfigurations.get("baseUrl") + "/getTaskStatus?taskId="+task.getTaskId());

            BaseRequest statusCheckRequest = requestBuilder.buildGetRequest(connectorConfigurations, null);
            HttpResponse<String> statusResponse = requestSubmitter.submitRequest(statusCheckRequest);
            String status = statusResponse.getBody();
            task = Task.parseTask(status);
        }
        return task;
    }

    private String getAuthorizationHeader(){
        String toEncode = connectorConfigurations.get("appId") + ":" + connectorConfigurations.get("password");
        return "Basic " + new String(Base64.getEncoder().encode(toEncode.getBytes()));
    }
}
