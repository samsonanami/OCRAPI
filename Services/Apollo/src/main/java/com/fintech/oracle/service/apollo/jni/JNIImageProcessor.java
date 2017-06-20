package com.fintech.oracle.service.apollo.jni;

import com.fintech.oracle.dto.jni.ZvImage;
import com.fintech.oracle.service.apollo.exception.JobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sasitha on 12/7/16.
 *
 */
@Component
public class JNIImageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JNIImageProcessor.class);
    @Autowired
    private String configurationFilePath;

    @Autowired
    private String nativeLibraryLocation;

    public native int ProcessDocument(ZvImage srcImage,String documentCategory,ZvImage processedImage); //
    public native int InitializeConfigurationData(String documentCategory,String errorMsg); //load this first time

    public void initializeAgent() throws JobException {
        String error = "";
        LOGGER.debug("loading library {} ", nativeLibraryLocation);
        System.load(nativeLibraryLocation);
        this.InitializeConfigurationData(configurationFilePath, error);
        if(!error.isEmpty()){
            LOGGER.error("Error occurred while initializing native library {} ", error);
            throw new JobException(error);
        }
    }

    public ZvImage processImage(String documentCategory, ZvImage srcImage){
        ZvImage processedImage = new ZvImage();
        this.ProcessDocument(srcImage, documentCategory, processedImage);
        return processedImage;
    }
}
