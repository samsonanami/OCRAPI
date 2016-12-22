package com.fintech.oracle.service.apollo.job;

import com.fintech.oracle.dataabstraction.entities.*;
import com.fintech.oracle.dataabstraction.entities.Process;
import com.fintech.oracle.dataabstraction.repository.OcrExtractionFieldRepository;
import com.fintech.oracle.dataabstraction.repository.OcrProcessingStatusRepository;
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
public class IdVerificationJob extends GeneralJob implements Job{

    private static final Logger LOGGER = LoggerFactory.getLogger(IdVerificationJob.class);

    @Override
    public void doJob(Serializable message) throws JobException {
        startProcessing(message);
    }

}
