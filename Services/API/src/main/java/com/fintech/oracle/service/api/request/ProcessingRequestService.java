package com.fintech.oracle.service.api.request;

import com.fintech.oracle.dataabstraction.entities.*;
import com.fintech.oracle.dataabstraction.repository.*;
import com.fintech.oracle.dto.messaging.JobResource;
import com.fintech.oracle.dto.request.Resource;
import com.fintech.oracle.dto.request.VerificationProcess;
import com.fintech.oracle.dto.request.VerificationRequest;
import com.fintech.oracle.dto.response.OcrFieldData;
import com.fintech.oracle.dto.response.OcrFieldValue;
import com.fintech.oracle.dto.response.OcrResponse;
import com.fintech.oracle.dto.response.VerificationProcessResponse;
import com.fintech.oracle.dto.messaging.ProcessingJobMessage;
import com.fintech.oracle.jobchanel.producer.MessageProducer;
import com.fintech.oracle.service.common.exception.ConfigurationDataNotFoundException;
import com.fintech.oracle.service.common.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by sasitha on 12/4/16.
 *
 */
@Service
public class ProcessingRequestService implements ProcessingRequestServiceInterface{

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

    @Override
    public VerificationProcessResponse saveProcessingRequest(VerificationRequest verificationRequest) throws ConfigurationDataNotFoundException, DataNotFoundException {
        VerificationProcessResponse response = new VerificationProcessResponse();
        OcrProcessingRequest ocrProcessingRequest = createNewOcrProcessingRequest();

        for (VerificationProcess v : verificationRequest.getVerificationProcesses()){
            createNewOcrProcess(v, ocrProcessingRequest);
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

    @Transactional
    private OcrProcessingRequest createNewOcrProcessingRequest(){
        OcrProcessingRequest ocrProcessingRequest = new OcrProcessingRequest();
        ocrProcessingRequest.setProcessingRequestCode(UUID.randomUUID().toString());
        ocrProcessingRequest.setReceivedOn(new Date());
        ocrProcessingRequestRepository.save(ocrProcessingRequest);
        return ocrProcessingRequest;
    }

    @Transactional
    private void createNewOcrProcess(VerificationProcess verificationProcess, OcrProcessingRequest processingRequest) throws ConfigurationDataNotFoundException, DataNotFoundException {
        OcrProcess ocrProcess = new OcrProcess();
        ocrProcess.setOcrProcessingRequest(processingRequest);
        ocrProcess.setOcrProcessingStatus(getProcessingStatus("pending"));
        ocrProcess.setOcrProcessType(getOcrProcessType(verificationProcess.getVerificationProcessType()));
        ocrProcessRepository.save(ocrProcess);
        updateResources(verificationProcess, ocrProcess);
        sendJob(ocrProcess.getId(), verificationProcess);
    }


    @Transactional
    private void sendJob(Integer processId, VerificationProcess verificationProcess){
        ProcessingJobMessage jobMessage = new ProcessingJobMessage();
        jobMessage.setOcrProcessId(String.valueOf(processId));
        try {
            jobMessage.setResources(getResources( verificationProcess));
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageProducer.sendMessage(jobMessage, jmsTemplate);
    }

    @Transactional
    private ArrayList<JobResource> getResources(VerificationProcess verificationProcess) throws IOException {
        ArrayList<JobResource> resourceList = new ArrayList<>();
        for (Resource resource : verificationProcess.getResources()){
            com.fintech.oracle.dataabstraction.entities.Resource resourceEntity = resourceRepository.findResourcesByResourceIdentificationCode(resource.getResourceId()).get(0);
            JobResource jobResource = new JobResource();
            jobResource.setResourceId(resource.getResourceId());
            jobResource.setResourceName(resource.getResourceName());
            jobResource.setData(getRawBytesFromFile(resourceFileBasePath + resourceEntity.getLocation()));
            resourceList.add(jobResource);
        }
        return resourceList;
    }

    @Transactional
    private static byte[] getRawBytesFromFile(String path) throws IOException {

        byte[] image;
        File file = new File(path);
        image = new byte[(int)file.length()];

        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(image);

        return image;
    }

    @Transactional
    private OcrProcessType getOcrProcessType(String type) throws ConfigurationDataNotFoundException {
        List<OcrProcessType> processTypeList = ocrProcessTypeRepository.findOcrProcessTypesByType(type);
        if(processTypeList.size() <= 0){
            throw new ConfigurationDataNotFoundException("No ocr processing type entry found in the table ocr_processing_type for type : " + type);
        }
        return processTypeList.get(0);
    }

    @Transactional
    private OcrProcessingStatus getProcessingStatus(String status) throws ConfigurationDataNotFoundException {
        List<OcrProcessingStatus> processingStatusList = ocrProcessingStatusRepository.findOcrProcessingStatusByStatus(status);
        if(processingStatusList.size() <= 0){
            throw new ConfigurationDataNotFoundException("No ocr processing status entry found in the table ocr_processing_status for status : " + status);
        }
        return processingStatusList.get(0);
    }

    @Transactional
    private void updateResources(VerificationProcess verificationProcess, OcrProcess ocrProcess) throws DataNotFoundException, ConfigurationDataNotFoundException {
        for (Resource r : verificationProcess.getResources()){
            List<com.fintech.oracle.dataabstraction.entities.Resource> resourceList = resourceRepository.findResourcesByResourceIdentificationCode(r.getResourceId());
            ResourceName resourceName = resourceNameRepository.findResourceNameByName(r.getResourceName());
            if(resourceList.isEmpty()){
                throw new DataNotFoundException("No resource found with resource identification code : " + r.getResourceId());
            }
            if(resourceName == null){
                throw new ConfigurationDataNotFoundException("No configurations were found for resource name : " + r.getResourceName());
            }
            com.fintech.oracle.dataabstraction.entities.Resource resource = resourceList.get(0);
            resource.setOcrProcess(ocrProcess);
            resource.setResourceName(resourceName);
            resourceRepository.save(resource);
        }
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
        Collections.sort(processList, new Comparator<OcrProcess>() {
            @Override
            public int compare(OcrProcess o1, OcrProcess o2) {
                return o1.getOcrProcessingStatus().getId() - o2.getOcrProcessingStatus().getId();
            }
        });
        return processList.get(0).getOcrProcessingStatus().getStatus();
    }

    @Transactional
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

    @Transactional
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
