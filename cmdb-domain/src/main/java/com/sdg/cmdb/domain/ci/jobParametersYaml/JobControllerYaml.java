package com.sdg.cmdb.domain.ci.jobParametersYaml;

import lombok.Data;

import java.io.Serializable;

@Data
public class JobControllerYaml implements Serializable{

    private String name;
    private String value;
    private String description ;
}
