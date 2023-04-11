package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the various Pipeline status values.
 */
public enum PipelineStatus {

    /**
     *
     */
    CREATED,
    WAITING_FOR_RESOURCE,
    PREPARING,
    PENDING,
    RUNNING,
    SUCCESS,
    FAILED,
    CANCELED,
    SKIPPED,
    MANUAL,
    SCHEDULED;

    private static Map<String, PipelineStatus> valuesMap = new HashMap<>(11);
    static {
        for (PipelineStatus status : PipelineStatus.values()) {
            valuesMap.put(status.toValue(), status);
        }
    }

    @JsonCreator
    public static PipelineStatus forValue(String value) {
        return valuesMap.get(value);
    }

    @JsonValue
    public String toValue() {
        return (name().toLowerCase());
    }

    @Override
    public String toString() {
        return (name().toLowerCase());
    }
}