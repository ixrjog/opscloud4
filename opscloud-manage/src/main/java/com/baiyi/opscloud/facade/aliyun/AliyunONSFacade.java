package com.baiyi.opscloud.facade.aliyun;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsInstance;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunONSVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 3:20 下午
 * @Since 1.0
 */
public interface AliyunONSFacade {

    BusinessWrapper<Boolean> syncONSInstance(String regionId);

    BusinessWrapper<Boolean> syncONSTopic(AliyunONSParam.QueryTopicList param);

    BusinessWrapper<Boolean> syncONSGroup(AliyunONSParam.QueryGroupList param);

    BusinessWrapper<AliyunONSVO.InstanceStatistics> onsInstanceStatistics();

    BusinessWrapper<List<AliyunONSVO.Instance>> queryONSInstanceList(String regionId);

    BusinessWrapper<List<OcAliyunOnsInstance>> queryONSInstanceAll();

    BusinessWrapper<Boolean> refreshONSInstanceDetail(AliyunONSParam.QueryInstanceDetail param);

    DataTable<AliyunONSVO.Topic> queryONSTopicPage(AliyunONSParam.TopicPageQuery pageQuery);

    DataTable<AliyunONSVO.Group> queryONSGroupPage(AliyunONSParam.GroupPageQuery pageQuery);

    BusinessWrapper<AliyunONSVO.TopicSubDetail> queryOnsTopicSubDetail(AliyunONSParam.QueryTopicSubDetail parma);

    BusinessWrapper<AliyunONSVO.GroupSubDetail> queryOnsGroupSubDetail(AliyunONSParam.QueryGroupSubDetail param);

    BusinessWrapper<Boolean> onsTopicCreate(AliyunONSParam.TopicCreate param);

    BusinessWrapper<Boolean> onsGroupCreate(AliyunONSParam.GroupCreate param);

    BusinessWrapper<Boolean> onsTopicCheck(AliyunONSParam.TopicCheck param);

    BusinessWrapper<Boolean> onsGroupCheck(AliyunONSParam.GroupCheck param);

    BusinessWrapper<Boolean> onsTopicCheckV2(String topic);

    BusinessWrapper<Boolean> onsGroupCheckV2(String groupId);

    BusinessWrapper<AliyunONSVO.GroupStatus> onsGroupStatus(AliyunONSParam.QueryGroupSubDetail param);

    void onsGroupTask();

    BusinessWrapper<Boolean> saveONSGroupAlarm(AliyunONSVO.GroupAlarm groupAlarm);

    BusinessWrapper<AliyunONSVO.GroupAlarm> queryONSGroupAlarm(Integer onsGroupId);

    BusinessWrapper<AliyunONSVO.TopicMessage> queryOnsMessage(AliyunONSParam.QueryTopicMsg param);

    BusinessWrapper<List<AliyunONSVO.TopicMessage>> onsMessagePageQuery(AliyunONSParam.QueryTopicSubDetail param);

    BusinessWrapper<AliyunONSVO.TopicMessageTraceMap> queryOnsTrace(AliyunONSParam.QueryTrace param);

    BusinessWrapper<AliyunONSVO.applyInstance> queryOnsInstanceByTopic(String topic);

    BusinessWrapper<AliyunONSVO.applyInstance> queryOcInstanceByGroupId(String groupId);
}
