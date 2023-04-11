package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApplicationSettings {

    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Map<String, Object> settings = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    @JsonIgnore
    public Object getSetting(Setting setting) {

        if (setting == null) {
            return (null);
        }

        String name = setting.toString();
        return (settings.get(name));
    }

    @JsonIgnore
    public Object getSetting(String setting) {

        if (setting == null) {
            return (null);
        }

        return (settings.get(setting));
    }

    public Object addSetting(String setting, Object value) throws GitLabApiException {

        Setting appSetting = Setting.forValue(setting);
        if (appSetting != null) {
            return (addSetting(appSetting, value));
        }

        settings.put(setting, value);
        return (value);
    }

    public Object addSetting(Setting setting, Object value) throws GitLabApiException {

        if (value instanceof JsonNode) {
            value = jsonNodeToValue((JsonNode)value);
        }

        setting.validate(value);
        settings.put(setting.toString(), value);
        return (value);
    }

    public Object removeSetting(Setting setting) {
        return settings.remove(setting.toString());
    }

    public Object removeSetting(String setting) {
        return settings.remove(setting);
    }

    public void clearSettings() {
        settings.clear();
    }

    private Object jsonNodeToValue(JsonNode node) {

        Object value = node;
        if (node instanceof NullNode) {
            value = null;
        } else if (node instanceof TextNode) {
            value = node.asText();
        } else if (node instanceof BooleanNode) {
            value = node.asBoolean();
        } else if (node instanceof IntNode) {
            value = node.asInt();
        } else if (node instanceof FloatNode) {
            value = (float)((FloatNode)node).asDouble();
        } else if (node instanceof DoubleNode) {
            value = (float)((DoubleNode)node).asDouble();
        } else if (node instanceof ArrayNode) {

            int numItems = node.size();
            String[] values = new String[numItems];
            for (int i = 0; i < numItems; i++) {
                values[i] = node.path(i).asText();
            }

            value = values;
        }

        return (value);
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
