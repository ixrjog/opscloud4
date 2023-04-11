package org.gitlab4j.api.models;

public class CustomAttribute {
    private String key;
    private String value;

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


    public CustomAttribute withKey(String key) {
        this.key = key;
        return this;
    }

    public CustomAttribute withValue(String value) {
        this.value = value;
        return this;
    }
}
