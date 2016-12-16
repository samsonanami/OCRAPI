package com.fintech.oracle.apollo.listner;

import com.fintech.oracle.dataabstraction.entities.OcrProcess;
import com.fintech.oracle.dto.messaging.ProcessingJobMessage;
import com.fintech.oracle.service.apollo.exception.JobException;
import com.fintech.oracle.service.apollo.job.Job;
import com.fintech.oracle.service.apollo.job.JobDetailService;
import com.fintech.oracle.service.apollo.job.JobFactory;
import com.fintech.oracle.jobchanel.consumer.MessageDelegate;
import com.fintech.oracle.service.common.exception.DataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Created by sasitha on 12/7/16.
 *
 */
public class MessageListener implements MessageDelegate{

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    JobFactory jobFactory;

    @Autowired
    JobDetailService jobDetailService;

    @Override
    @Transactional
    public void delegateMessage(Serializable message) {
        LOGGER.debug("Received message from job chanel {}", message);
        ProcessingJobMessage jobMessage = (ProcessingJobMessage)message;
        try {
            OcrProcess ocrProcess = jobDetailService.getOcrProcessDetails(jobMessage.getOcrProcessId());
            Job job = jobFactory.getJob(ocrProcess.getOcrProcessType().getType());
            job.doJob(jobMessage);
        } catch (DataNotFoundException e) {
            LOGGER.error("No Data found for the received job  ",e);
        } catch (JobException e) {
            LOGGER.error("Exception occurred while processing the job {} {}" , message , e);
        }
    }
}
