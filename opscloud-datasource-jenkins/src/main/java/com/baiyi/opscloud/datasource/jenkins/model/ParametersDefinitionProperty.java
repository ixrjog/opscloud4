package com.baiyi.opscloud.datasource.jenkins.model;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;

@Getter
@XmlRootElement(name = "hudson.model.ParametersDefinitionProperty")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
public class ParametersDefinitionProperty {

    @XmlElement(name = "parameterDefinitions")
    private ParameterDefinitions pd;

    public ParametersDefinitionProperty() {
    }

    public ParametersDefinitionProperty(ParameterDefinitions pd) {
        this.pd = pd;
    }

    public ParametersDefinitionProperty setPd(ParameterDefinitions pd) {
        this.pd = pd;
        return this;
    }

}