package com.fintech.oracle.dataabstraction.entities;
// Generated Dec 25, 2016 11:01:37 AM by Hibernate Tools 4.3.1


import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ProcessingRequest generated by hbm2java
 */
@Entity
@Table(name="processing_request"
    ,catalog="idapi"
)
public class ProcessingRequest  implements java.io.Serializable {


     private Integer id;
     private Client client;
     private Date receivedOn;
     private String processingRequestIdentificationCode;
     private Set<Process> processes = new HashSet<Process>(0);

    public ProcessingRequest() {
    }

    public ProcessingRequest(Client client, Date receivedOn, String processingRequestIdentificationCode, Set<Process> processes) {
       this.client = client;
       this.receivedOn = receivedOn;
       this.processingRequestIdentificationCode = processingRequestIdentificationCode;
       this.processes = processes;
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
    @JoinColumn(name="CLIENT")
    public Client getClient() {
        return this.client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="RECEIVED_ON", length=19)
    public Date getReceivedOn() {
        return this.receivedOn;
    }
    
    public void setReceivedOn(Date receivedOn) {
        this.receivedOn = receivedOn;
    }

    
    @Column(name="PROCESSING_REQUEST_IDENTIFICATION_CODE", length=40)
    public String getProcessingRequestIdentificationCode() {
        return this.processingRequestIdentificationCode;
    }
    
    public void setProcessingRequestIdentificationCode(String processingRequestIdentificationCode) {
        this.processingRequestIdentificationCode = processingRequestIdentificationCode;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="processingRequest")
    public Set<Process> getProcesses() {
        return this.processes;
    }
    
    public void setProcesses(Set<Process> processes) {
        this.processes = processes;
    }





}


