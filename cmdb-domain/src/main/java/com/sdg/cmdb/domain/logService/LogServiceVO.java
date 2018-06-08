package com.sdg.cmdb.domain.logService;

import java.io.Serializable;
import java.util.List;

public class LogServiceVO implements Serializable {
    private static final long serialVersionUID = 8285083271655906752L;

    private long id;

    private long userId;

    private String username;

    private String project;

    private String logstore;

    private String topic;

    private String query;

    private int timeFrom;

    private int timeTo;

    private String gmtFrom;

    private String gmtTo;

    // 历时显示
    private String timeView;

    // 日志条目
    private int totalCount;

    private String gmtCreate;

    private String gmtModify;

    private int histogramsCnt = 0;
    public int getHistogramsCnt() {
        return histogramsCnt;
    }

    public void setHistogramsCnt(int histogramsCnt) {
        this.histogramsCnt = histogramsCnt;
    }



    private List<LogHistogramsVO> histogramsVOList;

    public LogServiceVO() {

    }

    public LogServiceVO(LogServiceDO logServiceDO, String gmtFrom, String gmtTo) {
        this.id = logServiceDO.getId();
        this.project = logServiceDO.getProject();
        this.logstore = logServiceDO.getLogstore();
        this.topic = logServiceDO.getTopic();
        this.query = logServiceDO.getQuery();
        this.totalCount = logServiceDO.getTotalCount();
        this.gmtFrom = gmtFrom;
        this.gmtTo = gmtTo;
    }

    public LogServiceVO(LogServiceDO logServiceDO, List<LogHistogramsVO> histogramsVOList, String gmtFrom, String gmtTo) {
        this.id = logServiceDO.getId();
        this.project = logServiceDO.getProject();
        this.logstore = logServiceDO.getLogstore();
        this.topic = logServiceDO.getTopic();
        this.query = logServiceDO.getQuery();
        this.totalCount = logServiceDO.getTotalCount();
        this.histogramsVOList = histogramsVOList;
        this.gmtFrom = gmtFrom;
        this.gmtTo = gmtTo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getLogstore() {
        return logstore;
    }

    public void setLogstore(String logstore) {
        this.logstore = logstore;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(int timeFrom) {
        this.timeFrom = timeFrom;
    }

    public int getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(int timeTo) {
        this.timeTo = timeTo;
    }

    public String getGmtFrom() {
        return gmtFrom;
    }

    public void setGmtFrom(String gmtFrom) {
        this.gmtFrom = gmtFrom;
    }

    public String getGmtTo() {
        return gmtTo;
    }

    public void setGmtTo(String gmtTo) {
        this.gmtTo = gmtTo;
    }

    public String getTimeView() {
        return timeView;
    }

    public void setTimeView(String timeView) {
        this.timeView = timeView;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public List<LogHistogramsVO> getHistogramsVOList() {
        return histogramsVOList;
    }

    public void setHistogramsVOList(List<LogHistogramsVO> histogramsVOList) {
        this.histogramsVOList = histogramsVOList;
    }

}
