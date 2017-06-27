package com.fintech.oracle.service.apollo.extractor.local;

import com.fintech.oracle.dataabstraction.entities.OcrProcess;
import com.fintech.oracle.dataabstraction.entities.OcrResult;
import com.fintech.oracle.dataabstraction.entities.ResourceName;
import com.fintech.oracle.dataabstraction.entities.ResourceNameOcrExtractionField;
import com.fintech.oracle.dataabstraction.repository.ResourceNameOcrExtractionFieldRepository;
import com.fintech.oracle.service.apollo.extractor.ResultExtractor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by sasitha on 6/23/17.
 *
 */
public class LocalResultExtractor implements ResultExtractor<String> {

    private List<String> roiNameListToExtractResults;

    @Autowired
    private ResourceNameOcrExtractionFieldRepository resourceNameOcrExtractionFieldRepository;


    @Override
    public List<OcrResult> extractOcrResultSet(String resultString, OcrProcess ocrProcess, ResourceName resourceName, String preProcessedStatus) {

        List<ResourceNameOcrExtractionField> resourceNameOcrExtractionFieldList =
                resourceNameOcrExtractionFieldRepository.findResourceNameOcrExtractionFieldsByResourceName(resourceName);

        List<OcrResult> ocrResultList = new ArrayList<>();
        for (ResourceNameOcrExtractionField extractionField : filterOcrExtractionFieldSet(resourceNameOcrExtractionFieldList)){
            String ocrExtractionField = extractionField.getOcrExtractionField().getField();
            String fieldValueConfidenceString = getFieldValueConfidenceFromResultString(resultString, ocrExtractionField);
            String extractedValue = getExtractedFieldValue(fieldValueConfidenceString);
            String ocrConfidenceValue = getExtractedFieldOcrConfidence(fieldValueConfidenceString);
            if(ocrConfidenceValue.isEmpty()){
                ocrConfidenceValue = "0";
            }
            ocrResultList.add(getOcrResultObject(ocrProcess, extractedValue,
                    Double.valueOf(ocrConfidenceValue), resourceName, ocrExtractionField, extractionField));
        }

        return ocrResultList;
    }

    List<ResourceNameOcrExtractionField> filterOcrExtractionFieldSet(List<ResourceNameOcrExtractionField> fieldList){
        return fieldList.stream()
            .filter(r -> this.roiNameListToExtractResults.contains(r.getOcrExtractionField().getField())).collect(Collectors.toList());
    }

    private String getExtractedFieldValue(String fieldValueConfidenceString){
        String pattern = "##(.*?)##";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fieldValueConfidenceString.trim());
        String result = "";
        if(m.find()){
            result = m.group(0);
        }
        result = result.replace("##", "");
        return result;
    }

    private String getExtractedFieldOcrConfidence(String fieldValueConfidenceString){
        String pattern = "##([0-9]+\\.?[0-9]+)%%";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fieldValueConfidenceString.trim());
        String result = "";
        if(m.find()){
            result = m.group(0);
        }
        result = result.replace("%%", "");
        result = result.replace("##", "");
        return result;
    }

    private String getFieldValueConfidenceFromResultString(String resultString, String field){
        String pattern = "(?i)%%("+ field.replace("_", " ") +")(.*?)%%";

        Pattern r = Pattern.compile(pattern);
        String trimmedResultString = resultString.trim();
        Matcher m = r.matcher("%%"+trimmedResultString);
        String result = "";
        if(m.find()){
            result = m.group(0);
        }
        return result;
    }
    private OcrResult getOcrResultObject(OcrProcess ocrProcess, String value, Double confidence,
                                         ResourceName resourceName, String extractionField,
                                         ResourceNameOcrExtractionField resourceNameOcrExtractionField){
        OcrResult ocrResult = new OcrResult();
        ocrResult.setOcrProcess(ocrProcess);
        ocrResult.setOcrConfidence(confidence);
        ocrResult.setValue(value);
        ocrResult.setResourceNameOcrExtractionField(resourceNameOcrExtractionField);
        ocrResult.setResultName(resourceName.getName() + "##" + extractionField);
        return ocrResult;
    }

    public void setRoiNameListToExtractResults(List<String> roiNameListToExtractResults) {
        this.roiNameListToExtractResults = roiNameListToExtractResults;
    }
}
