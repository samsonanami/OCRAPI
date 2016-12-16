package com.fintech.oracle.dataabstraction.entities;
// Generated Dec 8, 2016 4:58:28 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * ResourceName generated by hbm2java
 */
@Entity
@Table(name="resource_name"
)
public class ResourceName  implements java.io.Serializable {


     private int id;
     private String name;
     private Set<Resource> resources = new HashSet<Resource>(0);
     private Set<ResourceNameOcrExtractionField> resourceNameOcrExtractionFields = new HashSet<ResourceNameOcrExtractionField>(0);

    public ResourceName() {
    }

	
    public ResourceName(int id) {
        this.id = id;
    }
    public ResourceName(int id, String name, Set<Resource> resources, Set<ResourceNameOcrExtractionField> resourceNameOcrExtractionFields) {
       this.id = id;
       this.name = name;
       this.resources = resources;
       this.resourceNameOcrExtractionFields = resourceNameOcrExtractionFields;
    }
   
     @Id 

    
    @Column(name="ID", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    
    @Column(name="NAME", length=50)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="resourceName")
    public Set<Resource> getResources() {
        return this.resources;
    }
    
    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="resourceName")
    public Set<ResourceNameOcrExtractionField> getResourceNameOcrExtractionFields() {
        return this.resourceNameOcrExtractionFields;
    }
    
    public void setResourceNameOcrExtractionFields(Set<ResourceNameOcrExtractionField> resourceNameOcrExtractionFields) {
        this.resourceNameOcrExtractionFields = resourceNameOcrExtractionFields;
    }


    @Override
    public String toString() {
        return "Resource [ resourceId = "+ id + ", resourceName " + name +"]";
    }
}


