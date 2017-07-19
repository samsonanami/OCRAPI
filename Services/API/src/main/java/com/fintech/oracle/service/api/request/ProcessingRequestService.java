package com.fintech.oracle.service.api.request;

import com.fintech.oracle.dataabstraction.entities.*;
import com.fintech.oracle.dataabstraction.repository.*;
import com.fintech.oracle.dto.messaging.ProcessingJobMessage;
import com.fintech.oracle.dto.request.Resource;
import com.fintech.oracle.dto.request.VerificationProcess;
import com.fintech.oracle.dto.request.VerificationRequest;
import com.fintech.oracle.dto.response.OcrFieldData;
import com.fintech.oracle.dto.response.OcrFieldValue;
import com.fintech.oracle.dto.response.OcrResponse;
import com.fintech.oracle.dto.response.VerificationProcessResponse;
import com.fintech.oracle.jobchanel.producer.MessageProducer;
import com.fintech.oracle.service.common.exception.ConfigurationDataNotFoundException;
import com.fintech.oracle.service.common.exception.DataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by sasitha on 12/4/16.
 *
 */
@Service
public class ProcessingRequestService implements ProcessingRequestServiceInterface{

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingRequestService.class);

    @Autowired
    private OcrProcesingRequestRepositoryInterface ocrProcessingRequestRepository;

    @Autowired
    private OcrProcessingStatusRepository ocrProcessingStatusRepository;

    @Autowired
    private OcrProcessTypeRepository ocrProcessTypeRepository;

    @Autowired
    private OcrProcessRepository ocrProcessRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private OcrExtractionFieldRepository ocrExtractionFieldRepository;

    @Autowired
    private ResourceNameRepository resourceNameRepository;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private String resourceFileBasePath;

    @Transactional
    @Override
    public VerificationProcessResponse saveProcessingRequest(VerificationRequest verificationRequest) throws DataNotFoundException {
        VerificationProcessResponse response = new VerificationProcessResponse();
        OcrProcessingRequest ocrProcessingRequest = createNewOcrProcessingRequest();
        ocrProcessingRequestRepository.save(ocrProcessingRequest);

        for (VerificationProcess v : verificationRequest.getVerificationProcesses()){
            for (Resource r : v.getResources()){
                OcrProcess ocrProcess = null;
                try {
                    ocrProcess = createNewOcrProcess(v, ocrProcessingRequest);
                } catch (ConfigurationDataNotFoundException e) {
                    throw new DataNotFoundException("One or more configuration data was not found ", e);
                }
                ocrProcessRepository.save(ocrProcess);
                com.fintech.oracle.dataabstraction.entities.Resource resourceEntity =
                        resourceRepository.findResourcesByResourceIdentificationCode(r.getResourceId());
                resourceEntity.setOcrProcess(ocrProcess);
                resourceRepository.save(resourceEntity);
            }

        }
        response.setVerificationProcessCode(ocrProcessingRequest.getProcessingRequestCode());
        return response;
    }

    @Override
    @Transactional
    public OcrResponse getProcessingResult(String code) throws DataNotFoundException {
        List<OcrProcessingRequest> processingRequestList = ocrProcessingRequestRepository.findOcrProcessingRequestsByProcessingRequestCode(code);
        if(processingRequestList.isEmpty()){
            throw new DataNotFoundException("No processing request found with the processing request identification code : " + code);
        }

        return getOcrResponseObject(processingRequestList.get(0));
    }


    private OcrProcessingRequest createNewOcrProcessingRequest(){
        OcrProcessingRequest ocrProcessingRequest = new OcrProcessingRequest();
        ocrProcessingRequest.setProcessingRequestCode(UUID.randomUUID().toString());
        ocrProcessingRequest.setReceivedOn(new Date());
        return ocrProcessingRequest;
    }

    private OcrProcess createNewOcrProcess(VerificationProcess verificationProcess, OcrProcessingRequest processingRequest) throws ConfigurationDataNotFoundException, DataNotFoundException {
        OcrProcess ocrProcess = new OcrProcess();
        ocrProcess.setOcrProcessingRequest(processingRequest);
        ocrProcess.setOcrProcessingStatus(getProcessingStatus("pending"));
        ocrProcess.setOcrProcessType(getOcrProcessType(verificationProcess.getVerificationProcessType()));

        return ocrProcess;

    }

    @Transactional
    @Override
    public void updateJobQueue(String ocrProcessingRequestCode){

        OcrProcessingRequest ocrProcessingRequest =
                ocrProcessingRequestRepository.findOcrProcessingRequestsByProcessingRequestCode(ocrProcessingRequestCode).get(0);

        for (OcrProcess ocrProcess : ocrProcessingRequest.getOcrProcesses()){
            for (com.fintech.oracle.dataabstraction.entities.Resource resource : ocrProcess.getResources()){
                Resource resourceDto = new Resource();
                resourceDto.setResourceName(resource.getResourceName().getName());
                resourceDto.setResourceId(resource.getResourceIdentificationCode());
                sendJob(ocrProcess, resourceDto);
            }
        }
    }

    private void sendJob(OcrProcess process, Resource resource){
        ProcessingJobMessage jobMessage = new ProcessingJobMessage();
        jobMessage.setOcrProcessId(String.valueOf(process.getId()));
        jobMessage.setResourceName(resource.getResourceName());
        jobMessage.setResourceId(resource.getResourceId());
        jobMessage.setImageData(getImageData(resource.getResourceId()));
        messageProducer.sendMessage(jobMessage, jmsTemplate);
    }


    private byte[] getImageData(String resourceIdentificationCode){
        com.fintech.oracle.dataabstraction.entities.Resource resourceEntity =
                resourceRepository.findResourcesByResourceIdentificationCode(resourceIdentificationCode);
        return getRawBytesFromFile(resourceFileBasePath + resourceEntity.getLocation());
    }


    private static byte[] getRawBytesFromFile(String path){

        byte[] image;
        File file = new File(path);
        image = new byte[(int)file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            int byteCount = 0;
            byteCount = fileInputStream.read(image);
            if (byteCount <= 0){
                LOGGER.error("0 bytes read from file {}", path);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Could not read file from path {} ", path, e);

        } catch (IOException e) {
            LOGGER.error("Unable to read file {} ", path, e);
        }
        return image;
    }


    private OcrProcessType getOcrProcessType(String type) throws ConfigurationDataNotFoundException {
        List<OcrProcessType> processTypeList = ocrProcessTypeRepository.findOcrProcessTypesByType(type);
        if(processTypeList.isEmpty()){
            throw new ConfigurationDataNotFoundException("No ocr processing type entry found in the table ocr_processing_type for type : " + type);
        }
        return processTypeList.get(0);
    }


    private OcrProcessingStatus getProcessingStatus(String status) throws ConfigurationDataNotFoundException {
        List<OcrProcessingStatus> processingStatusList = ocrProcessingStatusRepository.findOcrProcessingStatusByStatus(status);
        if(processingStatusList.isEmpty()){
            throw new ConfigurationDataNotFoundException("No ocr processing status entry found in the table ocr_processing_status for status : " + status);
        }
        return processingStatusList.get(0);
    }


    private OcrResponse getOcrResponseObject(OcrProcessingRequest processingRequest){
        OcrResponse ocrResponse = new OcrResponse();
        ocrResponse.setVerificationRequestId(processingRequest.getProcessingRequestCode());
        ocrResponse.setStatus(getGeneralProcessingStatus(processingRequest));
        List<OcrResult> resultList = new ArrayList<>();
        for (OcrProcess process : processingRequest.getOcrProcesses()){
            resultList.addAll(process.getOcrResults());
        }
        ocrResponse.setData(getOcrFieldDataList(resultList));
        return ocrResponse;
    }


    private String getGeneralProcessingStatus(OcrProcessingRequest processingRequest){
        List<OcrProcess> processList = new ArrayList<>(processingRequest.getOcrProcesses());
        Collections.sort(processList, (OcrProcess p1, OcrProcess p2) ->
                p1.getOcrProcessingStatus().getId() - p2.getOcrProcessingStatus().getId());
        String status = "pending";
        if (!processList.isEmpty()){
            status = processList.get(0).getOcrProcessingStatus().getStatus();
        }
        return status;
    }


    private List<OcrFieldData> getOcrFieldDataList(List<OcrResult> resultList){
        Iterable<OcrExtractionField> extractionFieldList = ocrExtractionFieldRepository.findAll();
        List<OcrFieldData> fieldDataList = new ArrayList<>();
        for (OcrExtractionField extractionField : extractionFieldList){
            OcrFieldData fieldData = new OcrFieldData();
            fieldData.setId(extractionField.getField());
            fieldData.setValue(getOcrFieldValueListForExtractionFieldList(resultList, extractionField.getField()));
            fieldDataList.add(fieldData);
        }
        return fieldDataList;
    }


    private List<OcrFieldValue>getOcrFieldValueListForExtractionFieldList(List<OcrResult> results, String extractionField){
        List<OcrFieldValue> fieldValueList = new ArrayList<>();
        for (OcrResult result :
                results) {
            OcrFieldValue fieldValue = new OcrFieldValue();
            if (result.getResultName().contains(extractionField)) {
                fieldValue.setId(result.getResultName());
                fieldValue.setValue(result.getValue());
                if(result.getOcrConfidence() != null){
                    fieldValue.setConfidence(result.getOcrConfidence().intValue());
                }
                fieldValueList.add(fieldValue);
            }
        }
        return fieldValueList;
    }
}
