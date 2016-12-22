package com.fintech.oracle.service.apollo.jni;

import com.fintech.oracle.dto.jni.ZvImage;
import com.fintech.oracle.service.apollo.exception.JobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by sasitha on 12/7/16.
 *
 */
@Component
public class JNIImageProcessor {

    @Autowired
    private String configurationFilePath;

    public native int ProcessDocument(ZvImage srcImage,String documentCategory,ZvImage processedImage); //
    public native int InitializeConfigurationData(String documentCategory,String errorMsg); //load this first time

    public void initializeAgent() throws JobException {
        String error = "";
        System.loadLibrary("DocumentDataExtractor");
        this.InitializeConfigurationData(configurationFilePath, error);
        if(!error.isEmpty()){
            throw new JobException(error);
        }
    }

    public ZvImage processDocument(String documentCategory, ZvImage srcImage){
        ZvImage processedImage = new ZvImage();
        this.ProcessDocument(srcImage, documentCategory, processedImage);
        return processedImage;
    }
}
