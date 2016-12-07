package com.fintech.oracle.service.api.request;

import com.fintech.oracle.dataabstraction.entities.*;
import com.fintech.oracle.dataabstraction.repository.*;
import com.fintech.oracle.dto.request.Resource;
import com.fintech.oracle.dto.request.VerificationProcess;
import com.fintech.oracle.dto.request.VerificationRequest;
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

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private MessageProducer messageProducer;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    @Transactional
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
        sendJob(ocrProcess.getId());
    }

    @Transactional
    private void sendJob(Integer processId){
        ProcessingJobMessage jobMessage = new ProcessingJobMessage();
        jobMessage.setOcrProcessId(String.valueOf(processId));
        messageProducer.sendMessage(jobMessage, jmsTemplate);
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
    private void updateResources(VerificationProcess verificationProcess, OcrProcess ocrProcess) throws DataNotFoundException {
        for (Resource r : verificationProcess.getResources()){
            List<com.fintech.oracle.dataabstraction.entities.Resource> resourceList = resourceRepository.findResourcesByResourceIdentificationCode(r.getResourceId());
            if(resourceList.isEmpty()){
                throw new DataNotFoundException("No resource found with resource identification code : " + r.getResourceId());
            }
            com.fintech.oracle.dataabstraction.entities.Resource resource = resourceList.get(0);
            resource.setOcrProcess(ocrProcess);
            resourceRepository.save(resource);
        }
    }

    private OcrResponse getOcrResponseObject(OcrProcessingRequest processingRequest){
        OcrResponse ocrResponse = new OcrResponse();
        ocrResponse.setVerificationRequestId(processingRequest.getProcessingRequestCode());
        ocrResponse.setStatus(getGeneralProccessingStatus(processingRequest));
        return ocrResponse;
    }

    private String getGeneralProccessingStatus(OcrProcessingRequest processingRequest){
        String status = "Success";
        for (OcrProcess process : processingRequest.getOcrProcesses()){
            String processStatus = process.getOcrProcessingStatus().getStatus();
            if(processStatus.isEmpty() || !processStatus.equalsIgnoreCase("Complete")){
                status = "Failed";
            }
        }
        return status;
    }
}
