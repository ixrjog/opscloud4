package com.sdg.cmdb.domain.logService.logServiceQuery;


import lombok.Data;

import java.io.Serializable;

@Data
public class LogserviceNginxQuery implements LogserviceQueryAbs, Serializable {
    private static final long serialVersionUID = 8267632330712572854L;

    private LogServiceCfgDO LogServiceCfg;


    private String queryBeginDate;
    private String queryBeginTime;
    private String queryEndDate;
    private String queryEndTime;

    private String request_time;
    private String http_x_forwarded_for;
    private String upstream_addr;
    private String uri;
    private String upstream_response_time;
    private String status;

    private int toMinutes;


    @Override
    public String acqQueryBeginDate() {
        return getQueryBeginDate() + " " + getQueryBeginTime();
    }

    @Override
    public String acqQueryEndDate() {
        return getQueryEndDate() + " " + getQueryEndTime();
    }

    @Override
    public int acqToMinutes() {
        return toMinutes;
    }

    @Override
    public int acqQueryType() {
        return 0;
    }

    @Override
    public LogServiceQueryCfg acqLogServiceQueryCfg() {
        return getLogServiceCfg();
    }


}
