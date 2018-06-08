package com.sdg.cmdb.domain.keybox.keyboxStatus;

import java.io.Serializable;

public class KeyboxLoginChartsVO implements Serializable {
    private static final long serialVersionUID = -3479074582144496518L;

    private String dateCat;

    private int keyboxCnt;

    private int getwayCnt;

    public String getDateCat() {
        return dateCat;
    }

    public void setDateCat(String dateCat) {
        this.dateCat = dateCat;
    }

    public int getKeyboxCnt() {
        return keyboxCnt;
    }

    public void setKeyboxCnt(int keyboxCnt) {
        this.keyboxCnt = keyboxCnt;
    }

    public int getGetwayCnt() {
        return getwayCnt;
    }

    public void setGetwayCnt(int getwayCnt) {
        this.getwayCnt = getwayCnt;
    }
}
