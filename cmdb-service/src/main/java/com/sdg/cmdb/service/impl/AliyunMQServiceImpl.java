package com.sdg.cmdb.service.impl;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;

import com.aliyuncs.ons.model.v20180628.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sdg.cmdb.dao.cmdb.AliyunDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.aliyunMQ.*;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.AliyunEcsItemEnum;
import com.sdg.cmdb.service.AliyunMQService;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.DingtalkService;
import com.sdg.cmdb.util.BeanCopierUtils;
import com.sdg.cmdb.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class AliyunMQServiceImpl implements AliyunMQService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AliyunMQServiceImpl.class);

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    @Resource
    private UserDao userDao;

    @Resource
    private AliyunDao aliyunDao;

    @Autowired
    private DingtalkService dingtalkService;

    private HashMap<String, String> configMap;

    @Resource
    private ConfigCenterService configCenterService;

    static private IAcsClient client;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ALIYUN_ECS.getItemKey());
    }

    @Override
    public OnsConsumerStatusResponse.Data consumerStatus(String groupId, String regionId) {
        OnsConsumerStatusRequest request = new OnsConsumerStatusRequest();
        /**
         *ONSRegionId 是指你需要 API 访问 MQ 哪个区域的资源.
         *该值必须要根据 OnsRegionList 方法获取的列表来选择和配置，因为 OnsRegionId 是变动的，不能够写固定值
         */
        if (StringUtils.isEmpty(regionId)) {
            request.setOnsRegionId(EcsServiceImpl.regionIdCnHangzhou);
        } else {
            request.setOnsRegionId(regionId);
        }
        request.setPreventCache(System.currentTimeMillis());
        request.setAcceptFormat(FormatType.JSON);
        request.setGroupId(groupId);
        request.setDetail(true);
        request.setNeedJstack(false);
        try {
            OnsConsumerStatusResponse response = client.getAcsResponse(request);
            OnsConsumerStatusResponse.Data data = response.getData();
            List<OnsConsumerStatusResponse.Data.ConnectionDo> connectionDoList = data.getConnectionSet();
            List<OnsConsumerStatusResponse.Data.DetailInTopicDo> detailInTopicDoList = data.getDetailInTopicList();
            List<OnsConsumerStatusResponse.Data.ConsumerConnectionInfoDo> consumerConnectionInfoDoList = data.getConsumerConnectionInfoList();
            System.out.print(data.getOnline() + "  " + data.getTotalDiff() + "  " + data.getConsumeTps() + "  " +
                    data.getLastTimestamp() + "  " + data.getDelayTime() + "  " + data.getConsumeModel() +
                    "  " + data.getSubscriptionSame() + "  " + data.getRebalanceOK());
            return data;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new OnsConsumerStatusResponse.Data();
    }


    @Override
    public boolean createOnsGroup(String groupId, String remark, String regionId) {
        OnsGroupCreateRequest request = new OnsGroupCreateRequest();
        /**
         *ONSRegionId 是指你需要 API 访问 MQ 哪个区域的资源
         *该值必须要根据 OnsRegionList 方法获取的列表来选择和配置，因为 OnsRegionId 是变动的，不能够写固定值
         */
        if (StringUtils.isEmpty(regionId)) {
            request.setOnsRegionId(EcsServiceImpl.regionIdCnHangzhou);
        } else {
            request.setOnsRegionId(regionId);
        }
        request.setPreventCache(System.currentTimeMillis());
        request.setAcceptFormat(FormatType.JSON);
        request.setGroupId(groupId);
        if (StringUtils.isEmpty(remark)) {
            remark = "Created by opsCloud";
        } else {
            remark = "Created by opsCloud ; " + remark;
        }
        request.setRemark(remark);
        try {
            OnsGroupCreateResponse response = client.getAcsResponse(request);
            //System.out.println(response.getRequestId());
            return true;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void task() {
        logger.info("AliyunMQTask开始执行！");
        List<AliyunMqGroupVO> list = groupList(null);
        //  public OnsConsumerStatusResponse.Data consumerStatus(String groupId, String regionId) {
        for (AliyunMqGroupVO aliyunMqGroupVO : list) {
            try {
                OnsConsumerStatusResponse.Data consumerStatus = consumerStatus(aliyunMqGroupVO.getGroupId(), null);
                aliyunMqGroupVO.setLastDelayTime(consumerStatus.getDelayTime());
                aliyunMqGroupVO.setLastTotalDiff(consumerStatus.getTotalDiff());
                aliyunMqGroupVO.setLastOnline(consumerStatus.getOnline());
                aliyunMqGroupVO.setLastRebalanceOK(consumerStatus.getRebalanceOK());
                aliyunDao.updateAliyunMqGroup(aliyunMqGroupVO);
                // TODO 告警
                if (aliyunMqGroupVO.isNotice())
                    alarm(aliyunMqGroupVO);
            } catch (Exception e) {
                //logger.error("ConsumerStatus Error groupId = {}", aliyunMqGroupVO.getGroupId());
                //e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否满足告警并发送
     *
     * @param aliyunMqGroupVO
     */
    private void alarm(AliyunMqGroupVO aliyunMqGroupVO) {
        // TODO 判断条件是否满足
        if (!isAlarm(aliyunMqGroupVO)) return;
        dingtalkService.notifyMQAlarm(aliyunMqGroupVO);
    }

    private boolean isAlarm(AliyunMqGroupVO aliyunMqGroupVO) {
        try {
            // TODO 堆积
            if (aliyunMqGroupVO.getTotalDiff() != 0 && aliyunMqGroupVO.getTotalDiff() <= aliyunMqGroupVO.getLastTotalDiff())
                return true;
            // TODO 延迟
            if (aliyunMqGroupVO.getDelayTime() != 0 && aliyunMqGroupVO.getDelayTime() <= aliyunMqGroupVO.getLastDelayTime())
                return true;
            // TODO 是否在线
            if (aliyunMqGroupVO.isOnline())
                if (!aliyunMqGroupVO.isLastOnline())
                    return true;
            // TODO 关系是否一致
            if (aliyunMqGroupVO.isRebalanceOK())
                if (!aliyunMqGroupVO.isLastRebalanceOK())
                    return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public BusinessWrapper<Boolean> createGroup(CreateOnsGroup createOnsGroup) {
        boolean result = createOnsGroup(createOnsGroup.getGroupId(), createOnsGroup.getRemark(), null);
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        AliyunMqGroupDO aliyunMqGroupDO = new AliyunMqGroupDO(createOnsGroup.getGroupId(), createOnsGroup.getRemark(), userDO);
        aliyunDao.addAliyunMqGroup(aliyunMqGroupDO);
        return new BusinessWrapper<Boolean>(result);
    }


    @Override
    public BusinessWrapper<Boolean> createTopic(CreateTopic createTopic) {
        try {
            boolean result = createOnsTopic(createTopic.getTopic(), createTopic.getMessageType(), null, createTopic.getRemark());
            if (!result) return new BusinessWrapper<Boolean>(ErrorCode.createTopicError);
            String username = SessionUtils.getUsername();
            UserDO userDO = userDao.getUserByName(username);
            AliyunMqTopicDO aliyunMqTopicDO;
            aliyunMqTopicDO = new AliyunMqTopicDO(createTopic.getTopic(), createTopic.getRemark(), userDO);
            aliyunDao.addAliyunMqTopic(aliyunMqTopicDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(ErrorCode.serverFailure);
        }
    }

    @Override
    public boolean createOnsTopic(String topicName, int messageType, String regionId, String remark) {
        //IAcsClient iAcsClient = acqClient();
        OnsTopicCreateRequest request = new OnsTopicCreateRequest();
        request.setAcceptFormat(FormatType.JSON);
        request.setTopic(topicName);
        request.setMessageType(messageType);
        // TODO 备注，可以不填
        if (StringUtils.isEmpty(remark)) {
            remark = "Created by opsCloud";
        } else {
            remark = "Created by opsCloud ; " + remark;
        }
        request.setRemark(remark);

        /**
         *ONSRegionId 是指需要 API 访问 MQ 哪个区域的资源
         *该值必须要根据 OnsRegionList 方法获取的列表来选择和配置，因为 OnsRegionId 是变动的，不能够写固定值
         */
        if (StringUtils.isEmpty(regionId)) {
            request.setOnsRegionId(EcsServiceImpl.regionIdCnHangzhou);
        } else {
            request.setOnsRegionId(regionId);
        }
        // cn-hangzhou
        request.setPreventCache(System.currentTimeMillis());
        //request.setRegionId(EcsServiceImpl.regionIdCnHangzhou);
        try {
            OnsTopicCreateResponse response = client.getAcsResponse(request);
            //System.out.println(response.getRequestId());
            return true;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public List<OnsRegionListResponse.RegionDo> onsRegionList() {
        //IAcsClient iAcsClient = acqClient();
        OnsRegionListRequest request = new OnsRegionListRequest();
        request.setAcceptFormat(FormatType.JSON);
        request.setPreventCache(System.currentTimeMillis());
        try {
            OnsRegionListResponse response = client.getAcsResponse(request);
            List<OnsRegionListResponse.RegionDo> regionDoList = response.getData();
            return regionDoList;
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<OnsRegionListResponse.RegionDo>();
    }

    @Override
    public List<OnsTopicVO> topicList(String topic, String regionId) {
        OnsTopicListRequest request = new OnsTopicListRequest();
        /**
         *ONSRegionId 是指你需要 API 访问 MQ 哪个区域的资源.
         *该值必须要根据 OnsRegionList 方法获取的列表来选择和配置，因为 OnsRegionId 是变动的，不能够写固定值
         */
        if (StringUtils.isEmpty(regionId)) {
            request.setOnsRegionId(EcsServiceImpl.regionIdCnHangzhou);
        } else {
            request.setOnsRegionId(regionId);
        }
        request.setPreventCache(System.currentTimeMillis());
        if (!StringUtils.isEmpty(topic))
            request.setTopic(topic);
        try {
            OnsTopicListResponse response = client.getAcsResponse(request);
            List<OnsTopicListResponse.PublishInfoDo> publishInfoDoList = response.getData();
            List<OnsTopicVO> topicList = new ArrayList<>();
            for (OnsTopicListResponse.PublishInfoDo publishInfoDo : publishInfoDoList) {
                AliyunMqTopicDO aliyunMqTopicDO = aliyunDao.getAliyunMqTopicByTopic(publishInfoDo.getTopic());
                OnsTopicVO vo;
                if (aliyunMqTopicDO == null) {
                    vo = new OnsTopicVO(publishInfoDo);
                } else {
                    vo = new OnsTopicVO(publishInfoDo, aliyunMqTopicDO);
                }
                topicList.add(vo);
            }
            return topicList;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<AliyunMqGroupVO> groupList(String regionId) {
        OnsGroupListRequest request = new OnsGroupListRequest();
        if (StringUtils.isEmpty(regionId)) {
            request.setOnsRegionId(EcsServiceImpl.regionIdCnHangzhou);
        } else {
            request.setOnsRegionId(regionId);
        }
        request.setPreventCache(System.currentTimeMillis());
        request.setAcceptFormat(FormatType.JSON);
        try {
            OnsGroupListResponse response = client.getAcsResponse(request);
            List<OnsGroupListResponse.SubscribeInfoDo> subscribeInfoDoList = response.getData();
            List<AliyunMqGroupVO> groupList = new ArrayList<>();
            for (OnsGroupListResponse.SubscribeInfoDo subscribeInfoDo : subscribeInfoDoList) {
                AliyunMqGroupDO aliyunMqGroupDO = aliyunDao.getAliyunMqGroupByGroupId(subscribeInfoDo.getGroupId());
                // TODO 为空插入数据
                if (aliyunMqGroupDO == null) {
                    aliyunMqGroupDO = new AliyunMqGroupDO(subscribeInfoDo);
                    aliyunDao.addAliyunMqGroup(aliyunMqGroupDO);
                }
                AliyunMqGroupVO vo = BeanCopierUtils.copyProperties(aliyunMqGroupDO, AliyunMqGroupVO.class);
                if (subscribeInfoDo.getStatus() != null)
                    vo.setStatus(subscribeInfoDo.getStatus());
                if (subscribeInfoDo.getStatusName() != null)
                    vo.setStatusName(subscribeInfoDo.getStatusName());
                vo.setUserList(getGroupUserList(aliyunMqGroupDO.getId()));
                if (vo.getTotalDiff() != 0)
                    vo.setUibProgressbarMax(vo.getTotalDiff());
                groupList.add(vo);
            }
            groupList.sort(Comparator.naturalOrder());
            return groupList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public AliyunMqGroupVO getGroup(long id) {
        AliyunMqGroupDO aliyunMqGroupDO = aliyunDao.getAliyunMqGroup(id);
        AliyunMqGroupVO vo = BeanCopierUtils.copyProperties(aliyunMqGroupDO, AliyunMqGroupVO.class);
        vo.setUserList(getGroupUserList(aliyunMqGroupDO.getId()));
        return vo;
    }

    @Override
    public BusinessWrapper<Boolean> addGroupUser(AliyunMqGroupUserDO aliyunMqGroupUserDO) {
        try {
            aliyunDao.addAliyunMqGroupUser(aliyunMqGroupUserDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delGroupUser(long id) {
        try {
            aliyunDao.delAliyunMqGroupUser(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> saveGroup(AliyunMqGroupVO aliyunMqGroupVO) {
        try {
            aliyunDao.updateAliyunMqGroup(aliyunMqGroupVO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    private List<AliyunMqGroupUserVO> getGroupUserList(long groupId) {
        List<AliyunMqGroupUserDO> groupUserList = aliyunDao.queryAliyunMqGroupUserByGroupId(groupId);
        List<AliyunMqGroupUserVO> userList = new ArrayList<>();
        for (AliyunMqGroupUserDO groupUser : groupUserList) {
            UserDO userDO = userDao.getUserById(groupUser.getUserId());
            // TODO 用户被删除
            if (userDO == null) {
                aliyunDao.delAliyunMqGroupUser(groupUser.getId());
                continue;
            }
            AliyunMqGroupUserVO vo = BeanCopierUtils.copyProperties(groupUser, AliyunMqGroupUserVO.class);
            vo.setUserVO(new UserVO(userDO, true));
            userList.add(vo);
        }
        return userList;
    }


    private IAcsClient acqClient() {
        if (client != null) return client;
        HashMap<String, String> configMap = acqConifMap();
        String aliyunAccessKey = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_KEY.getItemKey());
        String aliyunAccessSecret = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_SECRET.getItemKey());
        //生成 IClientProfile 的对象 profile，该对象存放 Access Key ID 和 Access Key Secret 和默认的地域信息
        try {
            DefaultProfile.addEndpoint(EcsServiceImpl.regionIdCnHangzhou, EcsServiceImpl.regionIdCnHangzhou, "Ons", "ons.cn-hangzhou.aliyuncs.com");
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IClientProfile profile = DefaultProfile.getProfile(EcsServiceImpl.regionIdCnHangzhou, aliyunAccessKey, aliyunAccessSecret);
        IAcsClient iAcsClient = new DefaultAcsClient(profile);
        client = iAcsClient;
        return client;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        acqConifMap();
        acqClient();
    }


}
