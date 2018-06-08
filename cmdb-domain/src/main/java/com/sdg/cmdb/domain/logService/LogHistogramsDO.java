package com.sdg.cmdb.domain.logService;

import com.aliyun.openservices.log.common.Histogram;

import java.io.Serializable;

public class LogHistogramsDO implements Serializable {

    private static final long serialVersionUID = -5717182100354115514L;
    private long id;

    private long logServiceId;

    private int timeFrom;

    private int timeTo;

    // 日志条目
    private int totalCount;

    private String gmtCreate;

    private String gmtModify;

    public LogHistogramsDO() {
    }

    public LogHistogramsDO(Histogram ht, long logServiceId) {
        this.logServiceId = logServiceId;
        this.timeFrom = ht.GetFrom();
        this.timeTo = ht.GetTo();
        this.totalCount = (int) ht.GetCount();
    }

    @Override
    public String toString() {
        return "LogHistogramsDO{" +
                "id=" + id +
                ", logServiceId=" + logServiceId +
                ", timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", totalCount=" + totalCount +
                ", gmtCreate='" + gmtCreate + '\'' +
                ",  gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLogServiceId() {
        return logServiceId;
    }

    public void setLogServiceId(long logServiceId) {
        this.logServiceId = logServiceId;
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
}
