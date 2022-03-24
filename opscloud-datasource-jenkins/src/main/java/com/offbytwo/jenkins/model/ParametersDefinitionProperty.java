package com.offbytwo.jenkins.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "hudson.model.ParametersDefinitionProperty")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
public class ParametersDefinitionProperty {

    @XmlElement(name = "parameterDefinitions")
    private com.offbytwo.jenkins.model.ParameterDefinitions pd;

    public ParametersDefinitionProperty() {
    }

    public ParametersDefinitionProperty(com.offbytwo.jenkins.model.ParameterDefinitions pd) {
        this.pd = pd;
    }

    public com.offbytwo.jenkins.model.ParameterDefinitions getPd() {
        return pd;
    }

    public ParametersDefinitionProperty setPd(ParameterDefinitions pd) {
        this.pd = pd;
        return this;
    }
}
