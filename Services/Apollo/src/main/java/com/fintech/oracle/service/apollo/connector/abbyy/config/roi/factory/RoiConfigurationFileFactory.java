package com.fintech.oracle.service.apollo.connector.abbyy.config.roi.factory;

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


    public String getConfigurationFilePath(String templateName){
        return configurationFileMap.get(templateName);
    }
}
