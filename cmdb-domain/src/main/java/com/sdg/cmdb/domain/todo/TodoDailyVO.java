package com.sdg.cmdb.domain.todo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 16/10/11.
 */
public class TodoDailyVO implements Serializable {
    private static final long serialVersionUID = 5899569598041403686L;

    private long id;

    /*
    一级类目
     */
    private TodoConfigDO levelOne;

    /*
    二级类目
     */
    private TodoConfigDO levelTwo;

    /*
    工单发起人
     */
    private String sponsor;

    /*
    0：公开；1：私密
     */
    private int privacy;

    /*
    紧急程度。0：一般；1：重要；2：紧急
     */
    private int urgents;

    /*
    工单内容
     */
    private String contents;

    /*
    反馈内容
     */
    private String feedbackContent;

    /*
    工单状态。0：处理中；1：完成；2：待反馈
     */
    private int todoStatus;

    /*
    是否确认。0：未确认；1：发起人确认；2：系统确认。
     */
    private int hasConfirm;

    private String gmtCreate;

    private String gmtModify;

    private List<TodoDailyLogDO> dailyLogDOList;

    public TodoDailyVO(TodoDailyDO dailyDO, TodoConfigDO levelOne, TodoConfigDO levelTwo) {
        this.id = dailyDO.getId();
        this.levelOne = levelOne;
        this.levelTwo = levelTwo;
        this.sponsor = dailyDO.getSponsor();
        this.privacy = dailyDO.getPrivacy();
        this.urgents = dailyDO.getUrgents();
        this.contents = dailyDO.getContents();
        this.feedbackContent = dailyDO.getFeedbackContent();
        this.todoStatus = dailyDO.getTodoStatus();
        this.hasConfirm = dailyDO.getHasConfirm();
        this.gmtCreate = dailyDO.getGmtCreate();
        this.gmtModify = dailyDO.getGmtModify();
    }

    public TodoDailyVO(TodoDailyDO dailyDO, TodoConfigDO levelOne, TodoConfigDO levelTwo, List<TodoDailyLogDO> dailyLogDOList) {
        this.id = dailyDO.getId();
        this.levelOne = levelOne;
        this.levelTwo = levelTwo;
        this.sponsor = dailyDO.getSponsor();
        this.privacy = dailyDO.getPrivacy();
        this.urgents = dailyDO.getUrgents();
        this.contents = dailyDO.getContents();
        this.feedbackContent = dailyDO.getFeedbackContent();
        this.todoStatus = dailyDO.getTodoStatus();
        this.hasConfirm = dailyDO.getHasConfirm();
        this.gmtCreate = dailyDO.getGmtCreate();
        this.gmtModify = dailyDO.getGmtModify();
        this.dailyLogDOList = dailyLogDOList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TodoConfigDO getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(TodoConfigDO levelOne) {
        this.levelOne = levelOne;
    }

    public TodoConfigDO getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(TodoConfigDO levelTwo) {
        this.levelTwo = levelTwo;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
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

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public int getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(int todoStatus) {
        this.todoStatus = todoStatus;
    }

    public int getHasConfirm() {
        return hasConfirm;
    }

    public void setHasConfirm(int hasConfirm) {
        this.hasConfirm = hasConfirm;
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

    public List<TodoDailyLogDO> getDailyLogDOList() {
        return dailyLogDOList;
    }

    public void setDailyLogDOList(List<TodoDailyLogDO> dailyLogDOList) {
        this.dailyLogDOList = dailyLogDOList;
    }

    @Override
    public String toString() {
        return "TodoDailyVO{" +
                "id=" + id +
                ", levelOne=" + levelOne +
                ", levelTwo=" + levelTwo +
                ", sponsor='" + sponsor + '\'' +
                ", privacy=" + privacy +
                ", urgents=" + urgents +
                ", contents='" + contents + '\'' +
                ", feedbackContent='" + feedbackContent + '\'' +
                ", todoStatus=" + todoStatus +
                ", hasConfirm=" + hasConfirm +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", dailyLogDOList=" + dailyLogDOList +
                '}';
    }
}
