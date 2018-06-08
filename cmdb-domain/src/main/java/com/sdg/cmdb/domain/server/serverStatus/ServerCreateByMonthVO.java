package com.sdg.cmdb.domain.server.serverStatus;

import java.io.Serializable;

public class ServerCreateByMonthVO implements Serializable {
    private static final long serialVersionUID = -6998858053421873291L;

    private String dateCat;

    private int vmCnt;

    private int ecsCnt;

    public String getDateCat() {
        return dateCat;
    }

    public void setDateCat(String dateCat) {
        this.dateCat = dateCat;
    }

    public int getVmCnt() {
        return vmCnt;
    }

    public void setVmCnt(int vmCnt) {
        this.vmCnt = vmCnt;
    }

    public int getEcsCnt() {
        return ecsCnt;
    }

    public void setEcsCnt(int ecsCnt) {
        this.ecsCnt = ecsCnt;
    }
}
