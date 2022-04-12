package com.baiyi.opscloud.datasource.jenkins.model;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "parameterDefinitions")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterDefinitions {

    @XmlElement(name = "hudson.model.StringParameterDefinition")
    List<StringParameterDefinition> stringParams;

    public ParameterDefinitions() {
        stringParams = Lists.newArrayList();
    }

    public ParameterDefinitions(List<StringParameterDefinition> stringParams) {
        this.stringParams = stringParams;
    }

    public List<StringParameterDefinition> getStringParams() {
        return stringParams;
    }

    public ParameterDefinitions setStringParams(List<StringParameterDefinition> stringParams) {
        this.stringParams = stringParams;
        return this;
    }

    public ParameterDefinitions addParam(StringParameterDefinition spd) {
        stringParams.add(spd);
        return this;
    }
}
