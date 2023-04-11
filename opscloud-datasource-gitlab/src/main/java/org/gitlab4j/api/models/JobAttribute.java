package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class JobAttribute {

  private String key;
  private String value;

  public JobAttribute(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return (JacksonJson.toJsonString(this));
  }
}
