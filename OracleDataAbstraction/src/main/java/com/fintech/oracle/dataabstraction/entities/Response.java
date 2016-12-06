package com.fintech.oracle.dataabstraction.entities;
// Generated Dec 5, 2016 6:07:33 PM by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Response generated by hbm2java
 */
@Entity
@Table(name="response"
)
public class Response  implements java.io.Serializable {


     private int processId;
     private Process process;
     private String rawJson;
     private String extractedJson;

    public Response() {
    }

	
    public Response(Process process) {
        this.process = process;
    }
    public Response(Process process, String rawJson, String extractedJson) {
       this.process = process;
       this.rawJson = rawJson;
       this.extractedJson = extractedJson;
    }
   
     @GenericGenerator(name="generator", strategy="foreign", parameters=@Parameter(name="property", value="process"))@Id @GeneratedValue(generator="generator")

    
    @Column(name="PROCESS_ID", unique=true, nullable=false)
    public int getProcessId() {
        return this.processId;
    }
    
    public void setProcessId(int processId) {
        this.processId = processId;
    }

@OneToOne(fetch=FetchType.LAZY)@PrimaryKeyJoinColumn
    public Process getProcess() {
        return this.process;
    }
    
    public void setProcess(Process process) {
        this.process = process;
    }

    
    @Column(name="RAW_JSON")
    public String getRawJson() {
        return this.rawJson;
    }
    
    public void setRawJson(String rawJson) {
        this.rawJson = rawJson;
    }

    
    @Column(name="EXTRACTED_JSON")
    public String getExtractedJson() {
        return this.extractedJson;
    }
    
    public void setExtractedJson(String extractedJson) {
        this.extractedJson = extractedJson;
    }




}


