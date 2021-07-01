package com.baiyi.caesar.jenkins.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/4/13 10:55 上午
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PipelineStep implements Serializable {
    private static final long serialVersionUID = 5247723012038357544L;

    private String displayDescription;
    private String displayName;

    private String id;
    private String result;
    private String state;
    private String type;

    private String log;
}
