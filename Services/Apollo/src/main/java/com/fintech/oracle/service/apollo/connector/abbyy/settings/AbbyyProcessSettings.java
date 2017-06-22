package com.fintech.oracle.service.apollo.connector.abbyy.settings;

import com.fintech.oracle.service.apollo.connector.abbyy.config.OutputFormat;

/**
 * Created by sasitha on 6/20/17.
 * 
 */
public class AbbyyProcessSettings {
    
    private OutputFormat outputFormat;
    private String language;

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
