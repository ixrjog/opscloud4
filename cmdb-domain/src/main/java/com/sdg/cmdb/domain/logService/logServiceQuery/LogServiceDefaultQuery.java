package com.sdg.cmdb.domain.logService.logServiceQuery;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.logService.LogServicePathDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class LogServiceDefaultQuery implements LogserviceQueryAbs, Serializable {
    private static final long serialVersionUID = -8749037710389909256L;

    public final static String SOURCE_KEY = "__source__";
    public final static String TAG_HOSTNAME_KEY = "__tag__:__hostname__";
    public final static String TAG_PATH_KEY = "__tag__:__path__";
    public final static String TOPIC_KEY = "__topic__";
    public final static String CONTENT_KEY = "content";
    private LogServiceServerGroupCfgDO logServiceServerGroupCfg;
    private LogServicePathDO logServicePath;
    public LogServicePathDO getLogServicePath() {
        return logServicePath;
    }


    private String source; // __source__
    private String tagHostname;     // __tag__:__hostname__
    private String tagPath; //  __tag__:__path__
    private String topic;     // __topic__
    private String search;   // 搜素内容

    // 完整的查询语句
    private String query;
    private String queryBeginDate;
    private String queryBeginTime;
    private String queryEndDate;
    private String queryEndTime;
    private int toMinutes;
    private int queryType;


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
        return 1;
    }

    @Override
    public LogServiceQueryCfg acqLogServiceQueryCfg() {
        return logServiceServerGroupCfg;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }



}
