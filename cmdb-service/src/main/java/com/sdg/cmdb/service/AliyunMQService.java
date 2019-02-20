package com.sdg.cmdb.service;


import com.aliyuncs.ons.model.v20180628.OnsConsumerStatusResponse;
import com.aliyuncs.ons.model.v20180628.OnsRegionListResponse;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.aliyunMQ.*;

import java.util.List;

public interface AliyunMQService {

    OnsConsumerStatusResponse.Data consumerStatus(String groupId, String regionId);

    boolean createOnsGroup(String groupId, String remark, String regionId);

    /**
     * 定时任务（用于采集MQGroup的采样数据）
     */
    void task();

    BusinessWrapper<Boolean> createGroup(CreateOnsGroup createOnsGroup);


    /**
     * 创建MQ Topic
     *
     * @param topicName
     * @param messageType 消息类型（0 普通消息、1 分区顺序消息、2 全局顺序消息、4 事务消息、5 定时/延时消息）
     * @param regionId
     * @param remark
     * @return
     */
    boolean createOnsTopic(String topicName, int messageType, String regionId, String remark);

    /**
     * 创建Topic Controller调用
     *
     * @param createTopic
     * @return
     */
    BusinessWrapper<Boolean> createTopic(CreateTopic createTopic);

    List<OnsTopicVO> topicList(String topic, String regionId);

    List<AliyunMqGroupVO> groupList(String regionId);

    AliyunMqGroupVO getGroup(long id);

    BusinessWrapper<Boolean> addGroupUser(AliyunMqGroupUserDO aliyunMqGroupUserDO);

    BusinessWrapper<Boolean> delGroupUser(long id);

    BusinessWrapper<Boolean> saveGroup(AliyunMqGroupVO aliyunMqGroupVO);


    /**
     * 查询Ons Region
     *
     * @return
     */
    List<OnsRegionListResponse.RegionDo> onsRegionList();


}
