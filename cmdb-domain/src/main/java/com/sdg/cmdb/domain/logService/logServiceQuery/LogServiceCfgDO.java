package com.sdg.cmdb.domain.logService.logServiceQuery;


import lombok.Data;

import java.io.Serializable;

@Data
public class LogServiceCfgDO implements LogServiceQueryCfg, Serializable {
    private static final long serialVersionUID = 7626416951004095434L;

    private long id;

    private String serverName;
    private String content;
    private String project;
    private String logstore;
    private String logPath;
    private String topic;
    private String gmtCreate;
    private String gmtModify;

    @Override
    public String acqProject() {
        return project;
    }

    @Override
    public String acqLogstore() {
        return logstore;
    }

    @Override
    public String acqTopic() {
        return topic;
    }


}
