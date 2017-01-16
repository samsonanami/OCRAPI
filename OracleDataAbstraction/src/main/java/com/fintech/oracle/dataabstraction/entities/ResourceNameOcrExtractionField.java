package com.fintech.oracle.dataabstraction.entities;
// Generated Dec 25, 2016 11:01:37 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * ResourceNameOcrExtractionField generated by hbm2java
 */
@Entity
@Table(name="resource_name_ocr_extraction_field"
)
public class ResourceNameOcrExtractionField  implements java.io.Serializable {


     private Integer id;
     private OcrExtractionField ocrExtractionField;
     private ResourceName resourceName;
     private Set<OcrResult> ocrResults = new HashSet<OcrResult>(0);

    public ResourceNameOcrExtractionField() {
    }

    public ResourceNameOcrExtractionField(OcrExtractionField ocrExtractionField, ResourceName resourceName, Set<OcrResult> ocrResults) {
       this.ocrExtractionField = ocrExtractionField;
       this.resourceName = resourceName;
       this.ocrResults = ocrResults;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="ID", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ocr_extraction_field_ID")
    public OcrExtractionField getOcrExtractionField() {
        return this.ocrExtractionField;
    }
    
    public void setOcrExtractionField(OcrExtractionField ocrExtractionField) {
        this.ocrExtractionField = ocrExtractionField;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="resource_name_ID")
    public ResourceName getResourceName() {
        return this.resourceName;
    }
    
    public void setResourceName(ResourceName resourceName) {
        this.resourceName = resourceName;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="resourceNameOcrExtractionField")
    public Set<OcrResult> getOcrResults() {
        return this.ocrResults;
    }
    
    public void setOcrResults(Set<OcrResult> ocrResults) {
        this.ocrResults = ocrResults;
    }


}


