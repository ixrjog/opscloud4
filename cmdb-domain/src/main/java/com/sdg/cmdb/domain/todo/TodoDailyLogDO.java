package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/10/11.
 */
public class TodoDailyLogDO implements Serializable {
    private static final long serialVersionUID = -904132817224789449L;

    public static final int notSend = 0;    //未发送
    public static final int hasSend = 1;    //发送

    public static final int statusProcess = 0; //处理中
    public static final int statusFinish = 1;  //完成
    public static final int statusFeedback = 2;    //待反馈

    private long id;

    /*
    日常工单id
     */
    private long dailyId;

    /*
    处理人
     */
    private String processUser;

    /*
    工单内容
     */
    private String dailyContent;

    /*
    工单反馈内容
     */
    private String dailyFeedbackContent;

    /*
    工单状态。0：处理中；1：完成；2：待反馈
     */
    private int todoStatus;

    /*
    是否发送通知，默认邮件。0：未发送；1：发送
     */
    private int sendNotify;

    private String gmtCreate;

    private String gmtModify;

    /**
     * 构建日常工单日志
     * @param dailyDO
     * @param processUser
     * @return
     */
    public static TodoDailyLogDO buildDailyLogDO(TodoDailyDO dailyDO, String processUser) {
        TodoDailyLogDO dailyLogDO = new TodoDailyLogDO();
        dailyLogDO.setDailyId(dailyDO.getId());
        dailyLogDO.setDailyContent(dailyDO.getContents());
        dailyLogDO.setDailyFeedbackContent(dailyDO.getFeedbackContent());
        dailyLogDO.setProcessUser(processUser);
        dailyLogDO.setTodoStatus(dailyDO.getTodoStatus());
        dailyLogDO.setSendNotify(TodoDailyLogDO.hasSend);

        return dailyLogDO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDailyId() {
        return dailyId;
    }

    public void setDailyId(long dailyId) {
        this.dailyId = dailyId;
    }

    public String getProcessUser() {
        return processUser;
    }

    public void setProcessUser(String processUser) {
        this.processUser = processUser;
    }

    public String getDailyContent() {
        return dailyContent;
    }

    public void setDailyContent(String dailyContent) {
        this.dailyContent = dailyContent;
    }

    public String getDailyFeedbackContent() {
        return dailyFeedbackContent;
    }

    public void setDailyFeedbackContent(String dailyFeedbackContent) {
        this.dailyFeedbackContent = dailyFeedbackContent;
    }

    public int getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(int todoStatus) {
        this.todoStatus = todoStatus;
    }

    public int getSendNotify() {
        return sendNotify;
    }

    public void setSendNotify(int sendNotify) {
        this.sendNotify = sendNotify;
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

    @Override
    public String toString() {
        return "TodoDailyLogDO{" +
                "id=" + id +
                ", dailyId=" + dailyId +
                ", processUser='" + processUser + '\'' +
                ", dailyContent='" + dailyContent + '\'' +
                ", dailyFeedbackContent='" + dailyFeedbackContent + '\'' +
                ", todoStatus=" + todoStatus +
                ", sendNotify=" + sendNotify +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
