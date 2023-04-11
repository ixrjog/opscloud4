package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Map;

public class HealthCheckItem {
    private HealthCheckStatus status;
    private Map<String, String> labels;
    private String message;

    public HealthCheckStatus getStatus() {
        return this.status;
    }

    public void setStatus(HealthCheckStatus status) {
        this.status = status;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
