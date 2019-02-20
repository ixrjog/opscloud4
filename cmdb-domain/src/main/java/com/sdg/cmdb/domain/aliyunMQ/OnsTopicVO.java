package com.sdg.cmdb.domain.aliyunMQ;


import com.aliyuncs.ons.model.v20180628.OnsTopicListResponse;
import com.sdg.cmdb.domain.aliyunMQ.AliyunMqTopicDO;

public class OnsTopicVO extends OnsTopicListResponse.PublishInfoDo {

    private long userId;

    private String displayName;

    public OnsTopicVO() {

    }


    public OnsTopicVO(OnsTopicListResponse.PublishInfoDo publishInfoDo) {

        setTopic(publishInfoDo.getTopic());
        setOwner(publishInfoDo.getOwner());
        setRelation(publishInfoDo.getRelation());
        setStatus(publishInfoDo.getStatus());
        setStatusName(publishInfoDo.getStatusName());
        setRemark(publishInfoDo.getRemark());
        setCreateTime(publishInfoDo.getCreateTime());
        setUpdateTime(publishInfoDo.getUpdateTime());
        setRelationName(publishInfoDo.getRelationName());
        setAppkey(publishInfoDo.getAppkey());
        setMessageType(publishInfoDo.getMessageType());
        setInstanceId(publishInfoDo.getInstanceId());
    }

    public OnsTopicVO(OnsTopicListResponse.PublishInfoDo publishInfoDo, AliyunMqTopicDO aliyunMqTopicDO) {
        setTopic(publishInfoDo.getTopic());
        setOwner(publishInfoDo.getOwner());
        setRelation(publishInfoDo.getRelation());
        setStatus(publishInfoDo.getStatus());
        setStatusName(publishInfoDo.getStatusName());
        setRemark(publishInfoDo.getRemark());
        setCreateTime(publishInfoDo.getCreateTime());
        setUpdateTime(publishInfoDo.getUpdateTime());
        setRelationName(publishInfoDo.getRelationName());
        setAppkey(publishInfoDo.getAppkey());
        setMessageType(publishInfoDo.getMessageType());
        setInstanceId(publishInfoDo.getInstanceId());
        this.userId = aliyunMqTopicDO.getUserId();
        this.displayName = aliyunMqTopicDO.getDisplayName();
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
