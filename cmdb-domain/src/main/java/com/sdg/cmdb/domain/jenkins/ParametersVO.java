package com.sdg.cmdb.domain.jenkins;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ParametersVO implements Serializable {

    private String id;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
