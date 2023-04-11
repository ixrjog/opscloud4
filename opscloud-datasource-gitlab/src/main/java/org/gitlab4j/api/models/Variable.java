package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.gitlab4j.api.utils.JacksonJson;
import org.gitlab4j.api.utils.JacksonJsonEnumHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Variable {

    /**
     * Enum for the various Commit build status values.
     */
    public enum Type {

        ENV_VAR, FILE;

        private static JacksonJsonEnumHelper<Type> enumHelper = new JacksonJsonEnumHelper<>(Type.class);

        @JsonCreator
        public static Type forValue(String value) {
            return enumHelper.forValue(value);
        }

        @JsonValue
        public String toValue() {
            return (enumHelper.toString(this));
        }

        @Override
        public String toString() {
            return (enumHelper.toString(this));
        }
    }

    private String key;
    private String value;
    private Type variableType;
    @JsonProperty("protected")
    private Boolean isProtected;
    @JsonProperty("masked")
    private Boolean isMasked;
    private String environmentScope;

    public Variable() {
    }

    public Variable(String key, String value) {
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

    public Type getVariableType() {
        return variableType;
    }

    public void setVariableType(Type variableType) {
        this.variableType = variableType;
    }

    public Boolean getProtected() {
        return isProtected;
    }

    public void setProtected(Boolean isProtected) {
        this.isProtected = isProtected;
    }

    public Boolean getMasked() {
        return isMasked;
    }

    public void setMasked(Boolean masked) {
        this.isMasked = masked;
    }

    public String getEnvironmentScope() {
        return environmentScope;
    }

    public void setEnvironmentScope(String environmentScope) {
        this.environmentScope = environmentScope;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }

    /**
     * Create a List of Variable from the provided Map.
     *
     * @param variables the Map to convert to a List of Variable
     * @return the List of Variable containing the keys and values from the Map, or null if the Map is null
     */
    public static final List<Variable> convertMapToList(Map<String, String> variables) {

        if (variables == null) {
            return null;
        }

        List<Variable> varList = new ArrayList<>(variables.size());
        variables.forEach((k, v) -> varList.add(new Variable(k, v)));
        return varList;
    }
}
