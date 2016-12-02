package com.fintech.oracle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * OcrResponse
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-02T04:51:04.641Z")

public class OcrResponse {
  @JsonProperty("verificationRequestId")
  private String verificationRequestId = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("data")
  private List<OcrFieldData> data = new ArrayList<OcrFieldData>();

  public OcrResponse verificationRequestId(String verificationRequestId) {
    this.verificationRequestId = verificationRequestId;
    return this;
  }

   /**
   * Get verificationRequestId
   * @return verificationRequestId
  **/
  @ApiModelProperty(value = "")
  public String getVerificationRequestId() {
    return verificationRequestId;
  }

  public void setVerificationRequestId(String verificationRequestId) {
    this.verificationRequestId = verificationRequestId;
  }

  public OcrResponse status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public OcrResponse data(List<OcrFieldData> data) {
    this.data = data;
    return this;
  }

  public OcrResponse addDataItem(OcrFieldData dataItem) {
    this.data.add(dataItem);
    return this;
  }

   /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(value = "")
  public List<OcrFieldData> getData() {
    return data;
  }

  public void setData(List<OcrFieldData> data) {
    this.data = data;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OcrResponse ocrResponse = (OcrResponse) o;
    return Objects.equals(this.verificationRequestId, ocrResponse.verificationRequestId) &&
        Objects.equals(this.status, ocrResponse.status) &&
        Objects.equals(this.data, ocrResponse.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(verificationRequestId, status, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OcrResponse {\n");

    sb.append("    verificationRequestId: ").append(toIndentedString(verificationRequestId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

