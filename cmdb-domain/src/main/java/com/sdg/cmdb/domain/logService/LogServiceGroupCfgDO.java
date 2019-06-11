package com.sdg.cmdb.domain.logService;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class LogServiceGroupCfgDO implements Serializable {
    private static final long serialVersionUID = -1440589566997218453L;

    private long id;
    private String project;
    private String logstore;
    private String config;
    private String content;
    private String gmtCreate;
    private String gmtModify;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
