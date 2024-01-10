package com.baiyi.opscloud.datasource.jenkins.model;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;

@Getter
@XmlRootElement(name = "hudson.model.StringParameterDefinition")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "name", "description", "defaultValue" })
public class StringParameterDefinition {

    @XmlElement
    private String name;
    @XmlElement
    private String description;
    @XmlElement
    private String defaultValue;

    public StringParameterDefinition() {
    }

    public StringParameterDefinition(String name, String description, String defaultValue) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public StringParameterDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public StringParameterDefinition setDescription(String description) {
        this.description = description;
        return this;
    }

    public StringParameterDefinition setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

}