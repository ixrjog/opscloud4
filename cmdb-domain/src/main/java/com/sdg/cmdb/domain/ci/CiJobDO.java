package com.sdg.cmdb.domain.ci;

import lombok.Data;

import java.io.Serializable;

@Data
public class CiJobDO implements Serializable {
    private static final long serialVersionUID = 5899629540817517884L;

    private long id;
    private String name;
    private String content;
    private long appId;
    private int ciType;
    // 构建任务是否通知所有人
    private boolean atAll;
    /**
     * 发布分支，留空可选
     */
    private String branch;
    /**
     * 组机分组，可选参数
     */
    private String hostPattern;
    private int envType;
    private String jobName;
    private String jobTemplate;
    private int jobVersion;
    private String deployJobName;
    private String deployJobTemplate;
    private int deployJobVersion;
    private int rollbackType;
    private String paramsYaml;
    private boolean autoBuild;
    private String gmtCreate;
    private String gmtModify;

}
