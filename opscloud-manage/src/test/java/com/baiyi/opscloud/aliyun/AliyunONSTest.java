package com.baiyi.opscloud.aliyun;

import com.aliyuncs.ons.model.v20190214.OnsGroupSubDetailResponse;
import com.aliyuncs.ons.model.v20190214.OnsTopicSubDetailResponse;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloud.mq.AliyunONSGroupCenter;
import com.baiyi.opscloud.cloud.mq.AliyunONSTopicCenter;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsTopic;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsGroupService;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsTopicService;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/11 2:31 下午
 * @Since 1.0
 */
public class AliyunONSTest extends BaseUnit {

    @Resource
    private AliyunONSTopicCenter aliyunONSTopicCenter;

    @Resource
    private OcAliyunOnsTopicService ocAliyunOnsTopicService;

    @Resource
    private AliyunONSGroupCenter aliyunONSGroupCenter;

    @Resource
    private OcAliyunOnsGroupService ocAliyunOnsGroupService;


    String devInstance = "MQ_INST_1820460406678463_BbA88Abo";
    String dailyInstance = "MQ_INST_1820460406678463_Bbz6lNww";
    String grayInstance = "MQ_INST_1820460406678463_Bb1V3yVY";
    String prodInstance = "MQ_INST_1820460406678463_BbzYxDpI";
    String regionId1 = "cn-hangzhou";
    String regionId2 = "mq-internet-access";


    @Test
    void topic() {
        List<OcAliyunOnsTopic> topicList = ocAliyunOnsTopicService.queryOcAliyunOnsTopicByInstanceId(prodInstance);  // daily
        topicList.forEach(topic -> {
            AliyunONSParam.QueryTopicSubDetail parma = new AliyunONSParam.QueryTopicSubDetail();
            parma.setRegionId(regionId1);
            parma.setInstanceId(prodInstance);
            parma.setTopic(topic.getTopic());
            List<OnsTopicSubDetailResponse.Data.SubscriptionDataListItem> list = aliyunONSTopicCenter.queryOnsTopicSubDetail(parma);
            if (CollectionUtils.isEmpty(list))
                System.err.println(topic.getTopic());
        });
    }

    @Test
    void groupId() {
        List<OcAliyunOnsGroup> groupList = ocAliyunOnsGroupService.queryOcAliyunOnsGroupByInstanceId(devInstance);  // daily
        groupList.forEach(group -> {
            AliyunONSParam.QueryGroupSubDetail param = new AliyunONSParam.QueryGroupSubDetail();
            param.setRegionId(regionId2);
            param.setInstanceId(devInstance);
            param.setGroupId(group.getGroupId());
            List<OnsGroupSubDetailResponse.Data.SubscriptionDataListItem> list = aliyunONSGroupCenter.queryOnsGroupSubDetail(param).getSubscriptionDataList();
            if (CollectionUtils.isEmpty(list))
                System.err.println(group.getGroupId());
        });
    }
}
