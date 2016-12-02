package com.fintech.oracle.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * VerificationRequest
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-02T04:32:55.820Z")

public class VerificationRequest   {
  @JsonProperty("verificationProcesses")
  private List<VerificationProcess> verificationProcesses = new ArrayList<VerificationProcess>();

  public VerificationRequest verificationProcesses(List<VerificationProcess> verificationProcesses) {
    this.verificationProcesses = verificationProcesses;
    return this;
  }

  public VerificationRequest addVerificationProcessesItem(VerificationProcess verificationProcessesItem) {
    this.verificationProcesses.add(verificationProcessesItem);
    return this;
  }

   /**
   * Get verificationProcesses
   * @return verificationProcesses
  **/
  @ApiModelProperty(value = "")
  public List<VerificationProcess> getVerificationProcesses() {
    return verificationProcesses;
  }

  public void setVerificationProcesses(List<VerificationProcess> verificationProcesses) {
    this.verificationProcesses = verificationProcesses;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerificationRequest verificationRequest = (VerificationRequest) o;
    return Objects.equals(this.verificationProcesses, verificationRequest.verificationProcesses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(verificationProcesses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerificationRequest {\n");

    sb.append("    verificationProcesses: ").append(toIndentedString(verificationProcesses)).append("\n");
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

