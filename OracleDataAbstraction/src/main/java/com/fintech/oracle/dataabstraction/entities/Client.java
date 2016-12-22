package com.fintech.oracle.dataabstraction.entities;
// Generated Dec 8, 2016 4:58:28 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Client generated by hbm2java
 */
@Entity
@Table(name="client"
    , uniqueConstraints = {@UniqueConstraint(columnNames="EMAIL"), @UniqueConstraint(columnNames="USER_NAME")} 
)
public class Client  implements java.io.Serializable {


     private Integer id;
     private String email;
     private Date registeredOn;
     private String userName;
     private String password;
     private boolean enabled;
     private Set<License> licenses = new HashSet<License>(0);
     private Set<ProcessingRequest> processingRequests = new HashSet<ProcessingRequest>(0);
     private Set<Resource> resources = new HashSet<Resource>(0);

    public Client() {
    }

	
    public Client(String email, Date registeredOn, String userName, String password, boolean enabled) {
        this.email = email;
        this.registeredOn = registeredOn;
        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
    }
    public Client(String email, Date registeredOn, String userName, String password, boolean enabled, Set<License> licenses, Set<ProcessingRequest> processingRequests, Set<Resource> resources) {
       this.email = email;
       this.registeredOn = registeredOn;
       this.userName = userName;
       this.password = password;
       this.enabled = enabled;
       this.licenses = licenses;
       this.processingRequests = processingRequests;
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

    
    @Column(name="EMAIL", unique=true, nullable=false, length=256)
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="REGISTERED_ON", nullable=false, length=10)
    public Date getRegisteredOn() {
        return this.registeredOn;
    }
    
    public void setRegisteredOn(Date registeredOn) {
        this.registeredOn = registeredOn;
    }

    
    @Column(name="USER_NAME", unique=true, nullable=false, length=45)
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    
    @Column(name="PASSWORD", nullable=false, length=256)
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    
    @Column(name="ENABLED", nullable=false)
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="client")
    public Set<License> getLicenses() {
        return this.licenses;
    }
    
    public void setLicenses(Set<License> licenses) {
        this.licenses = licenses;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="client")
    public Set<ProcessingRequest> getProcessingRequests() {
        return this.processingRequests;
    }
    
    public void setProcessingRequests(Set<ProcessingRequest> processingRequests) {
        this.processingRequests = processingRequests;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="client")
    public Set<Resource> getResources() {
        return this.resources;
    }
    
    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }




}


