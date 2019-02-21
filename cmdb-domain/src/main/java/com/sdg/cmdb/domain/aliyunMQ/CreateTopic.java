package com.sdg.cmdb.domain.aliyunMQ;

import java.io.Serializable;

public class CreateTopic implements Serializable{
    private static final long serialVersionUID = -7039711457271060475L;

    private String topic;

    private String remark;

    private String authUid;

    private int messageType;


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAuthUid() {
        return authUid;
    }

    public void setAuthUid(String authUid) {
        this.authUid = authUid;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
