package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/10/11.
 */
public class TodoDailyQueryDO implements Serializable {
    private static final long serialVersionUID = 44394247239093303L;

    /*
    工单主类别
     */
    private long levelOne;

    /*
    工单具体类别
     */
    private long levelTwo;

    /*
    工单发起人   all:所有;now:当前
     */
    private String sponsor;

    /*
    当前操作人
     */
    private String nowUser;

    /*
    0：公开；1：私密
     */
    private int privacy;

    /*
    紧急程度。0：一般；1：重要；2：紧急
     */
    private int urgents;

    /*
    工单状态。0：处理中；1：完成；2：待反馈
     */
    private int todoStatus;

    public long getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(long levelOne) {
        this.levelOne = levelOne;
    }

    public long getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(long levelTwo) {
        this.levelTwo = levelTwo;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getNowUser() {
        return nowUser;
    }

    public void setNowUser(String nowUser) {
        this.nowUser = nowUser;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public int getUrgents() {
        return urgents;
    }

    public void setUrgents(int urgents) {
        this.urgents = urgents;
    }

    public int getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(int todoStatus) {
        this.todoStatus = todoStatus;
    }

    @Override
    public String toString() {
        return "TodoDailyQueryDO{" +
                "levelOne=" + levelOne +
                ", levelTwo=" + levelTwo +
                ", sponsor='" + sponsor + '\'' +
                ", nowUser='" + nowUser + '\'' +
                ", privacy=" + privacy +
                ", urgents=" + urgents +
                ", todoStatus=" + todoStatus +
                '}';
    }
}
