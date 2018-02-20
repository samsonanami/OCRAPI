package com.fintech.oracle.service.apollo.extractor.abbyy;

import com.fintech.oracle.dataabstraction.entities.OcrProcess;
import com.fintech.oracle.dataabstraction.entities.OcrResult;
import com.fintech.oracle.dataabstraction.entities.ResourceName;
import com.fintech.oracle.dataabstraction.entities.ResourceNameOcrExtractionField;
import com.fintech.oracle.dataabstraction.repository.ResourceNameOcrExtractionFieldRepository;
import com.fintech.oracle.service.apollo.extractor.ResultExtractor;
import com.fintech.oracle.service.apollo.transformer.abbyy.pojo.Document;
import com.fintech.oracle.service.apollo.transformer.abbyy.pojo.Page;
import com.fintech.oracle.service.apollo.transformer.abbyy.pojo.Text;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sasitha on 6/22/17.
 *
 */
public class AbbyyResultExtractor implements ResultExtractor<Document> {

    private List<String> roiNameListToDataExtractFromAbbyResult;


    @Autowired
    private ResourceNameOcrExtractionFieldRepository resourceNameOcrExtractionFieldRepository;


    @Override
    public List<OcrResult> extractOcrResultSet(Document document,
                                               OcrProcess ocrProcess, ResourceName resourceName,
                                               String preProcessedStatus) {

        List<ResourceNameOcrExtractionField> resourceNameOcrExtractionFieldList =
                resourceNameOcrExtractionFieldRepository.findResourceNameOcrExtractionFieldsByResourceName(resourceName);

        List<OcrResult> ocrResultList = new ArrayList<>();
        for (ResourceNameOcrExtractionField extractionField : filterOcrExtractionFieldSet(resourceNameOcrExtractionFieldList)){
            String ocrExtractionField = extractionField.getOcrExtractionField().getField();
            String extractedValue = getExtractedFieldValue(document, ocrExtractionField);
            ocrResultList.add(getOcrResultObject(ocrProcess, extractedValue,
                    resourceName, ocrExtractionField, extractionField, preProcessedStatus));
        }

        return ocrResultList;
    }

    private OcrResult getOcrResultObject(OcrProcess ocrProcess, String extractedValue,
                                         ResourceName resourceName, String ocrExtractionField,
                                         ResourceNameOcrExtractionField extractionField, String preProcessedStatus) {
        OcrResult ocrResult = new OcrResult();
        ocrResult.setOcrProcess(ocrProcess);
        ocrResult.setOcrConfidence(0.0);
        ocrResult.setValue(extractedValue);
        ocrResult.setResourceNameOcrExtractionField(extractionField);
        ocrResult.setResultName(resourceName.getName() + "##" + ocrExtractionField + "##" + preProcessedStatus);
        return ocrResult;
    }

    private String getExtractedFieldValue(Document document, String ocrExtractionField) {
        String value = "";
        if(document != null){
            Page page = document.getPage();
            for (Text text : page.getText()){
                if (text.getId().equals(ocrExtractionField)){
                    value = text.getValue();
                }
            }
        }
        return value;
    }

    List<ResourceNameOcrExtractionField> filterOcrExtractionFieldSet(List<ResourceNameOcrExtractionField> fieldList){
        return fieldList.stream()
                .filter(r -> this.roiNameListToDataExtractFromAbbyResult.contains(r.getOcrExtractionField().getField())).collect(Collectors.toList());
    }

    public void setRoiNameListToDataExtractFromAbbyResult(List<String> roiNameListToDataExtractFromAbbyResult) {
        this.roiNameListToDataExtractFromAbbyResult = roiNameListToDataExtractFromAbbyResult;
    }
}