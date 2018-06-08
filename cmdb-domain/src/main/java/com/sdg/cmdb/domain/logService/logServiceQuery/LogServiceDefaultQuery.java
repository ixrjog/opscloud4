package com.sdg.cmdb.domain.logService.logServiceQuery;

import com.sdg.cmdb.domain.logService.LogServicePathDO;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceQuery;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceQueryCfg;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceServerGroupCfgDO;

import java.io.Serializable;

public class LogServiceDefaultQuery implements LogServiceQuery, Serializable {
    private static final long serialVersionUID = -8749037710389909256L;

    public final static String SOURCE_KEY = "__source__";

    public final static String TAG_HOSTNAME_KEY = "__tag__:__hostname__";

    public final static String TAG_PATH_KEY = "__tag__:__path__";

    public final static String TOPIC_KEY = "__topic__";

    public final static String CONTENT_KEY = "content";


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


    private LogServiceServerGroupCfgDO logServiceServerGroupCfg;

    private LogServicePathDO logServicePath;

    public LogServicePathDO getLogServicePath() {
        return logServicePath;
    }

    public void setLogServicePath(LogServicePathDO logServicePath) {
        this.logServicePath = logServicePath;
    }

    // __source__
    private String source;


    // __tag__:__hostname__
    private String tagHostname;

    //  __tag__:__path__
    private String tagPath;


    // __topic__
    private String topic;

    // 搜素内容
    private String search;

    // 完整的查询语句
    private String query;

    private String queryBeginDate;

    private String queryBeginTime;

    private String queryEndDate;

    private String queryEndTime;

    private int toMinutes;

    private int queryType;


    @Override
    public String toString() {
        return "LogServiceDefaultQuery{" + "\n" +
                SOURCE_KEY + ":" + source + "\n" +
                TAG_HOSTNAME_KEY + ":" + tagHostname + "\n" +
                TAG_PATH_KEY + ":" + tagPath + "\n" +
                TOPIC_KEY + ":" + topic + "\n" +
                "search=" + search + "\n" +
                "query=" + query +
                '}';
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTagHostname() {
        return tagHostname;
    }

    public void setTagHostname(String tagHostname) {
        this.tagHostname = tagHostname;
    }

    public String getTagPath() {
        return tagPath;
    }

    public void setTagPath(String tagPath) {
        this.tagPath = tagPath;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public int getToMinutes() {
        return toMinutes;
    }

    public void setToMinutes(int toMinutes) {
        this.toMinutes = toMinutes;
    }

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public LogServiceServerGroupCfgDO getLogServiceServerGroupCfg() {
        return logServiceServerGroupCfg;
    }

    public void setLogServiceServerGroupCfg(LogServiceServerGroupCfgDO logServiceServerGroupCfg) {
        this.logServiceServerGroupCfg = logServiceServerGroupCfg;
    }
}
