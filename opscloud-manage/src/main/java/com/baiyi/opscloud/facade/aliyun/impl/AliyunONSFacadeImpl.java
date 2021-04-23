package com.baiyi.opscloud.facade.aliyun.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aliyuncs.ons.model.v20190214.*;
import com.baiyi.opscloud.bo.aliyun.AliyunOnsGroupAlarmBO;
import com.baiyi.opscloud.cloud.mq.AliyunONSGroupCenter;
import com.baiyi.opscloud.cloud.mq.AliyunONSInstanceCenter;
import com.baiyi.opscloud.cloud.mq.AliyunONSTopicCenter;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.BeetlUtils;
import com.baiyi.opscloud.common.util.ObjectUtils;
import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.convert.aliyun.ons.AliyunONSGroupConvert;
import com.baiyi.opscloud.convert.aliyun.ons.AliyunONSTopicConvert;
import com.baiyi.opscloud.decorator.aliyun.ons.AliyunONSGroupDecorator;
import com.baiyi.opscloud.decorator.aliyun.ons.AliyunONSInstanceDecorator;
import com.baiyi.opscloud.decorator.aliyun.ons.AliyunONSTopicDecorator;
import com.baiyi.opscloud.dingtalk.DingtalkService;
import com.baiyi.opscloud.dingtalk.content.DingtalkContent;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunONSVO;
import com.baiyi.opscloud.facade.aliyun.AliyunONSFacade;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsGroupAlarmService;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsGroupService;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsInstanceService;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsTopicService;
import com.baiyi.opscloud.service.dingtalk.OcDingtalkService;
import com.baiyi.opscloud.service.file.OcFileTemplateService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 3:21 下午
 * @Since 1.0
 */

@Slf4j
@Component("AliyunONSFacade")
public class AliyunONSFacadeImpl implements AliyunONSFacade {

    private static final String REMARK_PREFIX = "create by opscloud;";

    private static final String ONS_GROUP_ALARM = "ONS_GROUP_ALARM";

    private static final String ONS_GROUP_ERR_ALARM = "ONS_GROUP_ERR_ALARM";

    private static final String XIUYUAN = "xiuyuan";

    private static final int DEFAULT = 0;

    @Resource
    private AliyunONSInstanceCenter aliyunONSInstanceCenter;

    @Resource
    private AliyunONSTopicCenter aliyunONSTopicCenter;

    @Resource
    private AliyunONSGroupCenter aliyunONSGroupCenter;

    @Resource
    private OcAliyunOnsInstanceService ocAliyunOnsInstanceService;

    @Resource
    private OcAliyunOnsTopicService ocAliyunOnsTopicService;

    @Resource
    private OcAliyunOnsGroupService ocAliyunOnsGroupService;

    @Resource
    private AliyunONSInstanceDecorator aliyunONSInstanceDecorator;

    @Resource
    private AliyunONSTopicDecorator aliyunONSTopicDecorator;

    @Resource
    private AliyunONSGroupDecorator aliyunONSGroupDecorator;

    @Resource
    private OcAliyunOnsGroupAlarmService ocAliyunOnsGroupAlarmService;

    @Resource
    private DingtalkService dingtalkService;

    @Resource
    private OcFileTemplateService ocFileTemplateService;

    @Resource
    private OcDingtalkService ocDingtalkService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private OcUserService ocUserService;

    @Override
    public BusinessWrapper<Boolean> syncONSInstance(String regionId) {
        return new BusinessWrapper<>(aliyunONSInstanceCenter.syncONSInstance(regionId));
    }

    @Override
    public BusinessWrapper<Boolean> syncONSTopic(AliyunONSParam.QueryTopicList param) {
        return new BusinessWrapper<>(aliyunONSTopicCenter.syncONSTopic(param));
    }

    @Override
    public BusinessWrapper<Boolean> syncONSGroup(AliyunONSParam.QueryGroupList param) {
        return new BusinessWrapper<>(aliyunONSGroupCenter.syncONSGroup(param));
    }

    @Override
    public BusinessWrapper<AliyunONSVO.InstanceStatistics> onsInstanceStatistics() {
        AliyunONSVO.InstanceStatistics instanceStatistics = new AliyunONSVO.InstanceStatistics();
        instanceStatistics.setInstanceTotal(ocAliyunOnsInstanceService.countOcAliyunOnsInstance());
        instanceStatistics.setTopicTotal(ocAliyunOnsTopicService.countOcAliyunOnsTopic());
        instanceStatistics.setGroupTotal(ocAliyunOnsGroupService.countOcAliyunOnsGroup());
        return new BusinessWrapper<>(instanceStatistics);
    }

    @Override
    public BusinessWrapper<List<AliyunONSVO.Instance>> queryONSInstanceList(String regionId) {
        List<OcAliyunOnsInstance> instanceList = ocAliyunOnsInstanceService.queryOcAliyunOnsInstanceByRegionId(regionId);
        List<AliyunONSVO.Instance> instanceVOList = aliyunONSInstanceDecorator.decoratorVOList(instanceList);
        return new BusinessWrapper<>(instanceVOList);
    }

    @Override
    public BusinessWrapper<List<OcAliyunOnsInstance>> queryONSInstanceAll() {
        List<OcAliyunOnsInstance> instanceList = ocAliyunOnsInstanceService.queryOcAliyunOnsInstanceAll();
        return new BusinessWrapper<>(instanceList);
    }

    @Override
    public BusinessWrapper<Boolean> refreshONSInstanceDetail(AliyunONSParam.QueryInstanceDetail param) {
        Boolean result = aliyunONSInstanceCenter.updateONSInstanceDetail(param);
        return new BusinessWrapper<>(result);
    }

    @Override
    public DataTable<AliyunONSVO.Topic> queryONSTopicPage(AliyunONSParam.TopicPageQuery pageQuery) {
        DataTable<OcAliyunOnsTopic> table = ocAliyunOnsTopicService.queryOcAliyunOnsTopicByInstanceId(pageQuery);
        List<AliyunONSVO.Topic> page = AliyunONSTopicConvert.toVOList(table.getData());
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public DataTable<AliyunONSVO.Group> queryONSGroupPage(AliyunONSParam.GroupPageQuery pageQuery) {
        DataTable<OcAliyunOnsGroup> table = ocAliyunOnsGroupService.queryONSGroupPage(pageQuery);
        List<AliyunONSVO.Group> page = aliyunONSGroupDecorator.decoratorVOList(table.getData());
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<AliyunONSVO.TopicSubDetail> queryOnsTopicSubDetail(AliyunONSParam.QueryTopicSubDetail param) {
        List<OnsTopicSubDetailResponse.Data.SubscriptionDataListItem> subList = aliyunONSTopicCenter.queryOnsTopicSubDetail(param);
        OnsTopicStatusResponse.Data data = aliyunONSTopicCenter.queryOnsTopicStatus(param);
        AliyunONSVO.TopicSubDetail topicSubDetail = AliyunONSTopicConvert.toSubDetailVO(subList, data);
        return new BusinessWrapper<>(topicSubDetail);
    }

    @Override
    public BusinessWrapper<AliyunONSVO.GroupSubDetail> queryOnsGroupSubDetail(AliyunONSParam.QueryGroupSubDetail param) {
        OnsGroupSubDetailResponse.Data data = aliyunONSGroupCenter.queryOnsGroupSubDetail(param);
        AliyunONSVO.GroupSubDetail groupSubDetail = AliyunONSGroupConvert.toSubDetailVO(data);
        return new BusinessWrapper<>(groupSubDetail);
    }

    @Override
    public BusinessWrapper<Boolean> onsTopicCreate(AliyunONSParam.TopicCreate param) {
        if (!checkOnsTopicNotExist(param.getInstanceId(), param.getTopic()))
            return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_TOPIC_EXIST);
        if (!param.getRemark().startsWith(REMARK_PREFIX)) {
            param.setRemark(REMARK_PREFIX + param.getRemark());
        }
        if (aliyunONSTopicCenter.onsTopicCreate(param)) {
            AliyunONSParam.QueryTopic queryParam = new AliyunONSParam.QueryTopic();
            queryParam.setRegionId(param.getRegionId());
            queryParam.setInstanceId(param.getInstanceId());
            queryParam.setTopic(param.getTopic());
            return new BusinessWrapper<>(aliyunONSTopicCenter.saveTopic(queryParam));
        }
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_TOPIC_CREATE_FAIL);
    }

    @Override
    public BusinessWrapper<Boolean> onsGroupCreate(AliyunONSParam.GroupCreate param) {
        if (!checkOnsGroupNotExist(param.getInstanceId(), param.getGroupId()))
            return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_GROUP_EXIST);
        if (!param.getRemark().startsWith(REMARK_PREFIX)) {
            param.setRemark(REMARK_PREFIX + param.getRemark());
        }
        if (aliyunONSGroupCenter.onsGroupCreate(param)) {
            AliyunONSParam.QueryGroup queryParam = new AliyunONSParam.QueryGroup();
            queryParam.setRegionId(param.getRegionId());
            queryParam.setInstanceId(param.getInstanceId());
            queryParam.setGroupId(param.getGroupId());
            queryParam.setGroupType(param.getGroupType());
            return new BusinessWrapper<>(aliyunONSGroupCenter.saveGroup(queryParam));
        }
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_GROUP_CREATE_FAIL);
    }

    private Boolean checkOnsTopicNotExist(String instanceId, String topic) {
        OcAliyunOnsTopic ocAliyunOnsTopic = ocAliyunOnsTopicService.queryOcAliyunOnsTopicByInstanceIdAndTopic(instanceId, topic);
        return ocAliyunOnsTopic == null;
    }

    private Boolean checkOnsGroupNotExist(String instanceId, String groupId) {
        OcAliyunOnsGroup ocAliyunOnsGroup = ocAliyunOnsGroupService.queryOcAliyunOnsGroupByInstanceIdAndGroupId(instanceId, groupId);
        return ocAliyunOnsGroup == null;
    }

    @Override
    public BusinessWrapper<Boolean> onsTopicCheck(AliyunONSParam.TopicCheck param) {
        if (RegexUtils.isAliyunONSTopicRule(param.getTopic()))
            return BusinessWrapper.SUCCESS;
        if (!checkOnsTopicNotExist(param.getInstanceId(), param.getTopic()))
            return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_TOPIC_EXIST);
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_TOPIC_NAME_ERR);
    }

    @Override
    public BusinessWrapper<Boolean> onsGroupCheck(AliyunONSParam.GroupCheck param) {
        if (RegexUtils.isAliyunONSGroupRule(param.getGroupId()))
            return BusinessWrapper.SUCCESS;
        if (!checkOnsGroupNotExist(param.getInstanceId(), param.getGroupId()))
            return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_GROUP_EXIST);
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_GROUP_NAME_ERR);
    }

    @Override
    public BusinessWrapper<Boolean> onsTopicCheckV2(String topic) {
        if (RegexUtils.isAliyunONSTopicRule(topic))
            return BusinessWrapper.SUCCESS;
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_TOPIC_NAME_ERR);
    }

    @Override
    public BusinessWrapper<Boolean> onsGroupCheckV2(String groupId) {
        if (RegexUtils.isAliyunONSGroupRule(groupId))
            return BusinessWrapper.SUCCESS;
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_GROUP_NAME_ERR);
    }

    @Override
    public BusinessWrapper<AliyunONSVO.GroupStatus> onsGroupStatus(AliyunONSParam.QueryGroupSubDetail param) {
        OnsConsumerStatusResponse.Data data = aliyunONSGroupCenter.onsGroupStatus(param, false);
        AliyunONSVO.GroupStatus groupStatus = aliyunONSGroupDecorator.decoratorStatusVO(data);
        return new BusinessWrapper<>(groupStatus);
    }

    @Override
    public void onsGroupTask() {
        List<OcAliyunOnsGroupAlarm> alarmList = ocAliyunOnsGroupAlarmService.queryOcAliyunOnsGroupAlarmByStatus(1);
        List<AliyunOnsGroupAlarmBO.HealthCheck> healthCheckList = Lists.newArrayList();
        alarmList.forEach(ocAliyunOnsGroupAlarm -> healthCheckList.add(onsGroupHealthCheck(ocAliyunOnsGroupAlarm)));
        if (!CollectionUtils.isEmpty(healthCheckList)) {
            healthCheckList.forEach(healthCheck -> {
                if (healthCheck != null) {
                    String msg = getONSGroupAlarmTemplate(healthCheck);
                    if (!Strings.isEmpty(msg)) {
                        doNotify(getDingtalkContent(msg));
                    }
                }
            });
        }
    }

    private AliyunOnsGroupAlarmBO.HealthCheck onsGroupHealthCheck(OcAliyunOnsGroupAlarm ocAliyunOnsGroupAlarm) {
        OcAliyunOnsGroup ocAliyunOnsGroup = ocAliyunOnsGroupService.queryOcAliyunOnsGroupById(ocAliyunOnsGroupAlarm.getOnsGroupId());
        if (ocAliyunOnsGroup == null) {
            log.error("ONS GroupId 告警异常,onsGroupId:{}", ocAliyunOnsGroupAlarm.getOnsGroupId());
            ocAliyunOnsGroupAlarm.setAlarmStatus(-1);
            ocAliyunOnsGroupAlarmService.updateOcAliyunOnsGroupAlarm(ocAliyunOnsGroupAlarm);
        }
        if (isSilent(ocAliyunOnsGroupAlarm))
            return null;
        AliyunONSParam.QueryGroupSubDetail param = new AliyunONSParam.QueryGroupSubDetail();
        param.setRegionId(ocAliyunOnsGroupAlarm.getRegionId());
        param.setGroupId(ocAliyunOnsGroup.getGroupId());
        param.setInstanceId(ocAliyunOnsGroup.getInstanceId());
        OnsConsumerStatusResponse.Data data = aliyunONSGroupCenter.onsGroupStatus(param, false);
        if (data == null) {
            return AliyunOnsGroupAlarmBO.HealthCheck.builder()
                    .status(-1)
                    .groupId(ocAliyunOnsGroup.getGroupId())
                    .instanceId(ocAliyunOnsGroup.getInstanceId())
                    .build();
        }
        if (isAlarm(data, ocAliyunOnsGroupAlarm)) {
            OcAliyunOnsInstance ocAliyunOnsInstance = ocAliyunOnsInstanceService.queryOcAliyunOnsInstanceByInstanceId(ocAliyunOnsGroup.getInstanceId());
            List<OcUser> userList = ocUserService.queryOcUserByIdList(JSONArray.parseArray(ocAliyunOnsGroupAlarm.getAlarmUserList(), Integer.class));
            List<OcUser> activeUserList = userList.stream().filter(OcUser::getIsActive).collect(Collectors.toList());
            if (userList.size() != activeUserList.size() && !CollectionUtils.isEmpty(activeUserList)) {
                List<Integer> userIdList = activeUserList.stream().map(OcUser::getId).collect(Collectors.toList());
                ocAliyunOnsGroupAlarm.setAlarmUserList(JSON.toJSONString(userIdList));
                ocAliyunOnsGroupAlarmService.updateOcAliyunOnsGroupAlarm(ocAliyunOnsGroupAlarm);
            }
            if (CollectionUtils.isEmpty(activeUserList)) {
                activeUserList.add(ocUserService.queryOcUserByUsername(XIUYUAN));
            }
            return AliyunOnsGroupAlarmBO.HealthCheck.builder()
                    .status(1)
                    .groupId(ocAliyunOnsGroup.getGroupId())
                    .instanceName(ocAliyunOnsInstance.getInstanceName())
                    .onsGroupStatus(data)
                    .userList(activeUserList)
                    .build();
        }
        return null;
    }

    private Boolean isSilent(OcAliyunOnsGroupAlarm ocAliyunOnsGroupAlarm) {
        String key = Joiner.on("_").join(OcAliyunOnsGroupAlarm.class.getName(), ocAliyunOnsGroupAlarm.getRegionId(), ocAliyunOnsGroupAlarm.getOnsGroupId());
        if (redisUtil.hasKey(key)) {
            return true;
        }
        redisUtil.set(key, true, 60 * ocAliyunOnsGroupAlarm.getAlarmSilentTime());
        return false;
    }

    private Boolean isAlarm(OnsConsumerStatusResponse.Data data, OcAliyunOnsGroupAlarm ocAliyunOnsGroupAlarm) {
        if (ocAliyunOnsGroupAlarm.getOnline() && !data.getOnline())
            return true;
        if (ocAliyunOnsGroupAlarm.getSubscriptionSame() && !data.getSubscriptionSame())
            return true;
        if (ocAliyunOnsGroupAlarm.getRebalanceOk() && !data.getRebalanceOK())
            return true;
        if (ocAliyunOnsGroupAlarm.getTotalDiff() != 0 && data.getTotalDiff() > ocAliyunOnsGroupAlarm.getTotalDiff())
            return true;
        if (ocAliyunOnsGroupAlarm.getDelayTime() != 0 && data.getDelayTime() > ocAliyunOnsGroupAlarm.getDelayTime() * 1000)
            return true;
        return false;
    }

    private String getONSGroupAlarmTemplate(AliyunOnsGroupAlarmBO.HealthCheck healthCheck) {
        Map<String, Object> contentMap = ObjectUtils.objectToMap(healthCheck);
        OcFileTemplate template = healthCheck.getStatus().equals(-1) ? ocFileTemplateService.queryOcFileTemplateByUniqueKey(ONS_GROUP_ERR_ALARM, DEFAULT)
                : ocFileTemplateService.queryOcFileTemplateByUniqueKey(ONS_GROUP_ALARM, DEFAULT);
        try {
            return BeetlUtils.renderTemplate(template.getContent(), contentMap);
        } catch (IOException e) {
            log.error("渲染ONS GroupId 告警模板失败，GroupId:{}", healthCheck.getGroupId(), e);
            return Strings.EMPTY;
        }
    }

    private void doNotify(DingtalkContent content) {
        dingtalkService.doNotify(content);
    }

    private DingtalkContent getDingtalkContent(String msg) {
        OcDingtalk ocDingtalk = ocDingtalkService.queryOcDingtalkByKey(ONS_GROUP_ALARM);
        return dingtalkService.getDingtalkContent(ocDingtalk.getDingtalkToken(), msg);
    }

    @Override
    public BusinessWrapper<Boolean> saveONSGroupAlarm(AliyunONSVO.GroupAlarm groupAlarm) {
        OcAliyunOnsGroupAlarm aliyunOnsGroupAlarm = ocAliyunOnsGroupAlarmService.queryOcAliyunOnsGroupByOnsGroupId(groupAlarm.getOnsGroupId());
        OcAliyunOnsGroupAlarm newAliyunOnsGroupAlarm = BeanCopierUtils.copyProperties(groupAlarm, OcAliyunOnsGroupAlarm.class);
        newAliyunOnsGroupAlarm.setAlarmUserList(JSON.toJSONString(groupAlarm.getUserIdList()));
        if (aliyunOnsGroupAlarm == null) {
            try {
                ocAliyunOnsGroupAlarmService.addOcAliyunOnsGroupAlarm(newAliyunOnsGroupAlarm);
                return BusinessWrapper.SUCCESS;
            } catch (Exception e) {
                log.error("保存ONSGroupId告警配置失败，groupId:{}", groupAlarm.getGroupId(), e);
                return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_GROUP_ALARM_CREATE_FAIL);
            }
        } else {
            newAliyunOnsGroupAlarm.setId(aliyunOnsGroupAlarm.getId());
            ocAliyunOnsGroupAlarmService.updateOcAliyunOnsGroupAlarm(newAliyunOnsGroupAlarm);
            return BusinessWrapper.SUCCESS;
        }
    }

    @Override
    public BusinessWrapper<AliyunONSVO.GroupAlarm> queryONSGroupAlarm(Integer onsGroupId) {
        OcAliyunOnsGroupAlarm aliyunOnsGroupAlarm = ocAliyunOnsGroupAlarmService.queryOcAliyunOnsGroupByOnsGroupId(onsGroupId);
        if (aliyunOnsGroupAlarm == null)
            return new BusinessWrapper<>(new AliyunONSVO.GroupAlarm());
        AliyunONSVO.GroupAlarm groupAlarm = BeanCopierUtils.copyProperties(aliyunOnsGroupAlarm, AliyunONSVO.GroupAlarm.class);
        List<OcUser> userList = ocUserService.queryOcUserByIdList(JSONArray.parseArray(aliyunOnsGroupAlarm.getAlarmUserList(), Integer.class));
        groupAlarm.setAlarmUserList(userList);
        groupAlarm.setUserIdList(JSONArray.parseArray(aliyunOnsGroupAlarm.getAlarmUserList(), Integer.class));
        return new BusinessWrapper<>(groupAlarm);
    }

    @Override
    public BusinessWrapper<AliyunONSVO.TopicMessage> queryOnsMessage(AliyunONSParam.QueryTopicMsg param) {
        OnsMessageGetByMsgIdResponse.Data data = aliyunONSTopicCenter.queryOnsMessage(param);
        AliyunONSVO.TopicMessage topicMessage = data == null ? null : AliyunONSTopicConvert.toMessageVO(data);
        return new BusinessWrapper<>(topicMessage);
    }

    @Override
    public BusinessWrapper<List<AliyunONSVO.TopicMessage>> onsMessagePageQuery(AliyunONSParam.QueryTopicSubDetail param) {
        OnsMessagePageQueryByTopicResponse.MsgFoundDo data = aliyunONSTopicCenter.onsMessagePageQuery(param);
        List<AliyunONSVO.TopicMessage> messageList = AliyunONSTopicConvert.toMessageVOList(data);
        return new BusinessWrapper<>(messageList);
    }

    @Override
    public BusinessWrapper<AliyunONSVO.TopicMessageTraceMap> queryOnsTrace(AliyunONSParam.QueryTrace param) {
        OnsTraceGetResultResponse.TraceData traceData = aliyunONSTopicCenter.queryOnsTrace(param);
        if (traceData == null)
            return new BusinessWrapper<>(ErrorEnum.ALIYUN_ONS_TOPIC_MSG_TRACE_QUERY_FAIL);
        if (!CollectionUtils.isEmpty(traceData.getTraceList())) {
            AliyunONSVO.TopicMessageTraceMap traceMap = aliyunONSTopicDecorator.decoratorTraceMapVO(traceData.getTraceList().get(0));
            return new BusinessWrapper<>(traceMap);
        }
        return new BusinessWrapper<>(null);
    }


    @Override
    public BusinessWrapper<AliyunONSVO.applyInstance> queryOnsInstanceByTopic(String topic) {
        AliyunONSVO.applyInstance applyInstance = new AliyunONSVO.applyInstance();
        List<OcAliyunOnsInstance> instanceAll = ocAliyunOnsInstanceService.queryOcAliyunOnsInstanceAll();
        List<OcAliyunOnsTopic> ocAliyunOnsTopicList = ocAliyunOnsTopicService.queryOcAliyunOnsTopicByTopic(topic);
        List<String> instanceIdList = ocAliyunOnsTopicList.stream().map(OcAliyunOnsTopic::getInstanceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(instanceIdList)) {
            applyInstance.setNowInstanceList(Collections.emptyList());
            applyInstance.setSelectInstanceList(instanceAll);
            return new BusinessWrapper<>(applyInstance);
        }
        List<OcAliyunOnsInstance> instanceList = ocAliyunOnsInstanceService.queryOcAliyunOnsInstanceByInstanceIdList(instanceIdList);
        List<OcAliyunOnsInstance> selectInstanceList = Lists.newArrayListWithCapacity(instanceAll.size() - instanceList.size());
        List<Integer> nowIdList = instanceList.stream().map(OcAliyunOnsInstance::getId).collect(Collectors.toList());
        instanceAll.forEach(x -> {
            if (!nowIdList.contains(x.getId()))
                selectInstanceList.add(x);
        });
        applyInstance.setNowInstanceList(instanceList);
        applyInstance.setSelectInstanceList(selectInstanceList);
        applyInstance.setTopic(ocAliyunOnsTopicList.get(0));
        return new BusinessWrapper<>(applyInstance);
    }

    @Override
    public BusinessWrapper<AliyunONSVO.applyInstance> queryOcInstanceByGroupId(String groupId) {
        AliyunONSVO.applyInstance applyInstance = new AliyunONSVO.applyInstance();
        List<OcAliyunOnsInstance> instanceAll = ocAliyunOnsInstanceService.queryOcAliyunOnsInstanceAll();
        List<OcAliyunOnsGroup> ocAliyunOnsGroupList = ocAliyunOnsGroupService.queryOcAliyunOnsGroupByGroupId(groupId);
        List<String> instanceIdList = ocAliyunOnsGroupList.stream().map(OcAliyunOnsGroup::getInstanceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(instanceIdList)) {
            applyInstance.setNowInstanceList(Collections.emptyList());
            applyInstance.setSelectInstanceList(instanceAll);
            return new BusinessWrapper<>(applyInstance);
        }
        List<OcAliyunOnsInstance> instanceList = ocAliyunOnsInstanceService.queryOcAliyunOnsInstanceByInstanceIdList(instanceIdList);
        List<OcAliyunOnsInstance> selectInstanceList = Lists.newArrayListWithCapacity(instanceAll.size() - instanceList.size());
        List<Integer> nowIdList = instanceList.stream().map(OcAliyunOnsInstance::getId).collect(Collectors.toList());
        instanceAll.forEach(x -> {
            if (!nowIdList.contains(x.getId()))
                selectInstanceList.add(x);
        });
        applyInstance.setNowInstanceList(instanceList);
        applyInstance.setSelectInstanceList(selectInstanceList);
        applyInstance.setGroup(ocAliyunOnsGroupList.get(0));
        return new BusinessWrapper<>(applyInstance);
    }
}
