package com.fintech.oracle.service.apollo.connector.abbyy.config.roi.factory;

import com.fintech.oracle.service.common.exception.ConfigurationDataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by sasitha on 6/21/17.
 *
 */
@Component
public class RoiConfigurationFileFactory {

    @Resource(name = "configurationFileMap")
    Map<String,String> configurationFileMap;

    @Autowired
    private String roiConfigurationBasePath;


    public String getConfigurationFilePath(String templateName) throws ConfigurationDataNotFoundException {
        String configurationFilePath = configurationFileMap.get(templateName);
        if (configurationFilePath == null){
            throw new ConfigurationDataNotFoundException("No ROI Configuration file was found for template name : " + templateName);
        }
        return roiConfigurationBasePath + "/" + configurationFilePath;
    }
}
