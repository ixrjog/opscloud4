package com.sdg.cmdb.domain.keybox.keyboxStatus;

import com.sdg.cmdb.domain.keybox.KeyboxLoginStatusVO;

import java.io.Serializable;
import java.util.List;

public class KeyboxStatusVO implements Serializable {
    private static final long serialVersionUID = -7858611848296550596L;

    private List<KeyboxServerVO> topServerList;

    private List<KeyboxUserVO> topUserList;

    private List<KeyboxLoginStatusVO> lastLoginByGetway;

    private List<KeyboxLoginStatusVO> lastLoginByKeybox;


    private List<KeyboxLoginChartsVO> keyboxLoginCharts;

    private int authedUserCnt = 0;

    private int keyboxLoginCnt = 0;

    public List<KeyboxServerVO> getTopServerList() {
        return topServerList;
    }

    public void setTopServerList(List<KeyboxServerVO> topServerList) {
        this.topServerList = topServerList;
    }

    public List<KeyboxUserVO> getTopUserList() {
        return topUserList;
    }

    public void setTopUserList(List<KeyboxUserVO> topUserList) {
        this.topUserList = topUserList;
    }

    public int getAuthedUserCnt() {
        return authedUserCnt;
    }

    public void setAuthedUserCnt(int authedUserCnt) {
        this.authedUserCnt = authedUserCnt;
    }

    public int getKeyboxLoginCnt() {
        return keyboxLoginCnt;
    }

    public void setKeyboxLoginCnt(int keyboxLoginCnt) {
        this.keyboxLoginCnt = keyboxLoginCnt;
    }

    public List<KeyboxLoginStatusVO> getLastLoginByGetway() {
        return lastLoginByGetway;
    }

    public void setLastLoginByGetway(List<KeyboxLoginStatusVO> lastLoginByGetway) {
        this.lastLoginByGetway = lastLoginByGetway;
    }

    public List<KeyboxLoginStatusVO> getLastLoginByKeybox() {
        return lastLoginByKeybox;
    }

    public void setLastLoginByKeybox(List<KeyboxLoginStatusVO> lastLoginByKeybox) {
        this.lastLoginByKeybox = lastLoginByKeybox;
    }

    public List<KeyboxLoginChartsVO> getKeyboxLoginCharts() {
        return keyboxLoginCharts;
    }

    public void setKeyboxLoginCharts(List<KeyboxLoginChartsVO> keyboxLoginCharts) {
        this.keyboxLoginCharts = keyboxLoginCharts;
    }
}
