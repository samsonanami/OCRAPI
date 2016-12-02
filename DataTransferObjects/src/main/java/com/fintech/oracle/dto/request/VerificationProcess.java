package com.fintech.oracle.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * VerificationProcess
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-02T04:32:55.820Z")

public class VerificationProcess   {
  @JsonProperty("verificationProcessType")
  private String verificationProcessType = null;

  @JsonProperty("resources")
  private List<Resource> resources = new ArrayList<Resource>();

  public VerificationProcess verificationProcessType(String verificationProcessType) {
    this.verificationProcessType = verificationProcessType;
    return this;
  }

   /**
   * Get verificationProcessType
   * @return verificationProcessType
  **/
  @ApiModelProperty(value = "")
  public String getVerificationProcessType() {
    return verificationProcessType;
  }

  public void setVerificationProcessType(String verificationProcessType) {
    this.verificationProcessType = verificationProcessType;
  }

  public VerificationProcess resources(List<Resource> resources) {
    this.resources = resources;
    return this;
  }

  public VerificationProcess addResourcesItem(Resource resourcesItem) {
    this.resources.add(resourcesItem);
    return this;
  }

   /**
   * Get resources
   * @return resources
  **/
  @ApiModelProperty(value = "")
  public List<Resource> getResources() {
    return resources;
  }

  public void setResources(List<Resource> resources) {
    this.resources = resources;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerificationProcess verificationProcess = (VerificationProcess) o;
    return Objects.equals(this.verificationProcessType, verificationProcess.verificationProcessType) &&
        Objects.equals(this.resources, verificationProcess.resources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(verificationProcessType, resources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerificationProcess {\n");

    sb.append("    verificationProcessType: ").append(toIndentedString(verificationProcessType)).append("\n");
    sb.append("    resources: ").append(toIndentedString(resources)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

