package com.sdg.cmdb.domain.ci.jobParametersYaml;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JobParametersYaml implements Serializable {
    private static final long serialVersionUID = 5832978334684318505L;

    private String appName;
    private String env;
    private String version;
    // 参数列表
    private List<JobParameterYaml> parameters;
    // 控制器列表
    private List<JobControllerYaml> controllers;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
