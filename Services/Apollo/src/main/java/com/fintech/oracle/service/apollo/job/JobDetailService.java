package com.fintech.oracle.service.apollo.job;

import com.fintech.oracle.dataabstraction.entities.*;
import com.fintech.oracle.dataabstraction.entities.Process;
import com.fintech.oracle.dataabstraction.repository.OcrProcessRepository;
import com.fintech.oracle.dataabstraction.repository.OcrProcessingStatusRepository;
import com.fintech.oracle.dataabstraction.repository.OcrResultRepository;
import com.fintech.oracle.dataabstraction.repository.ResourceRepository;
import com.fintech.oracle.service.common.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by sasitha on 12/7/16.
 *
 */
@Service
public class JobDetailService implements JobDetailServiceInterface{

    @Autowired
    private OcrProcessRepository ocrProcessRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private OcrResultRepository ocrResultRepository;

    @Autowired
    private OcrProcessingStatusRepository ocrProcessingStatusRepository;

    @Override
    @Transactional
    public OcrProcess getOcrProcessDetails(String jobId) throws DataNotFoundException {
        OcrProcess ocrProcess =  ocrProcessRepository.findOne(Integer.parseInt(jobId));
        if(ocrProcess == null){
            throw new DataNotFoundException("No ocr process data found for the id : " + jobId );
        }
        return ocrProcess;
    }

    @Override
    @Transactional
    public Resource getResourceDetails(Integer resourceId) throws DataNotFoundException {
        Resource resource = resourceRepository.findOne(resourceId);
        if(resource == null){
            throw new DataNotFoundException("No resource found for the resource id : "+ String.valueOf(resourceId));
        }
        return resource;
    }

    @Override
    public Resource getResourceDetails(String resourceIdentificationCode) throws DataNotFoundException {
        Resource resource = resourceRepository.findResourcesByResourceIdentificationCode(resourceIdentificationCode);
        if (resource == null){
            throw new DataNotFoundException("No resource found for the resource with resource identification code : " +
                    resourceIdentificationCode);
        }
        return resource;
    }

    @Override
    @Transactional
    public void saveOcrResults(OcrResult results) {
        ocrResultRepository.save(results);
    }

    @Override
    @Transactional
    public void updateOcrProcessStatus(OcrProcess process, String status) {
        OcrProcessingStatus processingStatus = ocrProcessingStatusRepository.findOcrProcessingStatusByStatus(status).get(0);
        process.setOcrProcessingStatus(processingStatus);
        ocrProcessRepository.save(process);
    }

    @Override
    @Transactional
    public void updateOcrProcessStatus(Resource resource, String status) {
        OcrProcessingStatus processingStatus = ocrProcessingStatusRepository.findOcrProcessingStatusByStatus(status).get(0);
        resource.setOcrProcessingStatus(processingStatus);
        resourceRepository.save(resource);
    }
}
