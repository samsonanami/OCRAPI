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
 * OcrProcess generated by hbm2java
 */
@Entity
@Table(name="ocr_process"
)
public class OcrProcess  implements java.io.Serializable {


     private Integer id;
     private OcrProcessingRequest ocrProcessingRequest;
     private OcrProcessingStatus ocrProcessingStatus;
     private OcrProcessType ocrProcessType;
     private Set<OcrResult> ocrResults = new HashSet<OcrResult>(0);
     private Set<Resource> resources = new HashSet<Resource>(0);

    public OcrProcess() {
    }

    public OcrProcess(OcrProcessingRequest ocrProcessingRequest, OcrProcessingStatus ocrProcessingStatus, OcrProcessType ocrProcessType, Set<OcrResult> ocrResults, Set<Resource> resources) {
       this.ocrProcessingRequest = ocrProcessingRequest;
       this.ocrProcessingStatus = ocrProcessingStatus;
       this.ocrProcessType = ocrProcessType;
       this.ocrResults = ocrResults;
       this.resources = resources;
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
    @JoinColumn(name="PROCESSING_REQUEST")
    public OcrProcessingRequest getOcrProcessingRequest() {
        return this.ocrProcessingRequest;
    }
    
    public void setOcrProcessingRequest(OcrProcessingRequest ocrProcessingRequest) {
        this.ocrProcessingRequest = ocrProcessingRequest;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROCESSING_STATUS")
    public OcrProcessingStatus getOcrProcessingStatus() {
        return this.ocrProcessingStatus;
    }
    
    public void setOcrProcessingStatus(OcrProcessingStatus ocrProcessingStatus) {
        this.ocrProcessingStatus = ocrProcessingStatus;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROCESS_TYPE")
    public OcrProcessType getOcrProcessType() {
        return this.ocrProcessType;
    }
    
    public void setOcrProcessType(OcrProcessType ocrProcessType) {
        this.ocrProcessType = ocrProcessType;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="ocrProcess")
    public Set<OcrResult> getOcrResults() {
        return this.ocrResults;
    }
    
    public void setOcrResults(Set<OcrResult> ocrResults) {
        this.ocrResults = ocrResults;
    }


@OneToMany(fetch=FetchType.LAZY, mappedBy="ocrProcess")
    public Set<Resource> getResources() {
        return this.resources;
    }
    
    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }




}


