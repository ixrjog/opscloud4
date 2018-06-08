package com.sdg.cmdb.domain.logService;

import java.io.Serializable;

public class LogHistogramsVO extends LogHistogramsDO implements Serializable {
    private static final long serialVersionUID = 3901363552171962569L;

    private String gmtFrom;

    private String gmtTo;

    public LogHistogramsVO() {
    }

    public LogHistogramsVO(LogHistogramsDO logHistogramsDO, String gmtFrom, String gmtTo) {
        this.setId(logHistogramsDO.getId());
        this.setLogServiceId(logHistogramsDO.getLogServiceId());
        this.setTimeFrom(logHistogramsDO.getTimeFrom());
        this.setTimeTo(logHistogramsDO.getTimeTo());
        this.setTotalCount(logHistogramsDO.getTotalCount());
        this.gmtFrom = gmtFrom;
        this.gmtTo = gmtTo;
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
}
