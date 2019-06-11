package com.sdg.cmdb.domain.ci.jobParametersYaml;

import lombok.Data;

import java.io.Serializable;

@Data
public class JobParameterYaml implements Serializable {
    private static final long serialVersionUID = 8282142658655933696L;

    private String name;
    private String value;
    private String description ;

}
