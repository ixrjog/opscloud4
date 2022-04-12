package com.baiyi.opscloud.datasource.jenkins.model;

import javax.xml.bind.annotation.*;

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

    public String getName() {
        return name;
    }

    public StringParameterDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public StringParameterDefinition setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public StringParameterDefinition setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
}
