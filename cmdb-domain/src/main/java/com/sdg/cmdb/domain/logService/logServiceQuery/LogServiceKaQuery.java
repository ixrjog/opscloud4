package com.sdg.cmdb.domain.logService.logServiceQuery;

import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceCfgDO;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceQuery;

import java.io.Serializable;

public class LogServiceKaQuery implements LogServiceQuery, Serializable {
    private static final long serialVersionUID = 8267632330712572854L;

    private LogServiceCfgDO LogServiceCfg;

    private String args;

    private String mobile;

    private String queryBeginDate;

    private String queryBeginTime;

    private String queryEndDate;

    private String queryEndTime;

    private String requestTime;

    private String sourceIp;

    private String status;

    private int toMinutes;

    private String uri;

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


    public LogServiceCfgDO getLogServiceCfg() {
        return LogServiceCfg;
    }

    public void setLogServiceCfg(LogServiceCfgDO logServiceCfg) {
        LogServiceCfg = logServiceCfg;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQueryBeginDate() {
        return queryBeginDate;
    }

    public void setQueryBeginDate(String queryBeginDate) {
        this.queryBeginDate = queryBeginDate;
    }

    public String getQueryBeginTime() {
        return queryBeginTime;
    }

    public void setQueryBeginTime(String queryBeginTime) {
        this.queryBeginTime = queryBeginTime;
    }

    public String getQueryEndDate() {
        return queryEndDate;
    }

    public void setQueryEndDate(String queryEndDate) {
        this.queryEndDate = queryEndDate;
    }

    public String getQueryEndTime() {
        return queryEndTime;
    }

    public void setQueryEndTime(String queryEndTime) {
        this.queryEndTime = queryEndTime;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getToMinutes() {
        return toMinutes;
    }

    public void setToMinutes(int toMinutes) {
        this.toMinutes = toMinutes;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
