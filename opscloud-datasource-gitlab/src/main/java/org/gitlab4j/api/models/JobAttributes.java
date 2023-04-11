package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.List;

public class JobAttributes {

  @JsonProperty("job_variables_attributes")
  private List<JobAttribute> jobAttributes;

  public JobAttributes(List<JobAttribute> jobAttributes) {
    this.jobAttributes = jobAttributes;
  }

  public List<JobAttribute> getJobAttributes() {
    return jobAttributes;
  }

  public void setJobAttributes(List<JobAttribute> jobAttributes) {
    this.jobAttributes = jobAttributes;
  }

  @Override
  public String toString() {
    return (JacksonJson.toJsonString(this));
  }
}
