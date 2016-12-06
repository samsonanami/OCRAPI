package com.fintech.oracle.dto.response;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * VerificationProcessResponse
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-04T10:29:17.360Z")

public class VerificationProcessResponse {
  @JsonProperty("verificationProcessCode")
  private String verificationProcessCode = null;

  public VerificationProcessResponse verificationProcessCode(String verificationProcessCode) {
    this.verificationProcessCode = verificationProcessCode;
    return this;
  }

   /**
   * Get verificationProcessCode
   * @return verificationProcessCode
  **/
  @ApiModelProperty(value = "")
  public String getVerificationProcessCode() {
    return verificationProcessCode;
  }

  public void setVerificationProcessCode(String verificationProcessCode) {
    this.verificationProcessCode = verificationProcessCode;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerificationProcessResponse verificationProcessResponse = (VerificationProcessResponse) o;
    return Objects.equals(this.verificationProcessCode, verificationProcessResponse.verificationProcessCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(verificationProcessCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerificationProcessResponse {\n");
    
    sb.append("    verificationProcessCode: ").append(toIndentedString(verificationProcessCode)).append("\n");
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

