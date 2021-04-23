package com.baiyi.opscloud.convert.aliyun.ons;

import com.aliyuncs.ons.model.v20190214.OnsGroupSubDetailResponse;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.vo.cloud.AliyunONSVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/10 10:45 上午
 * @Since 1.0
 */
public class AliyunONSGroupConvert {

    private static AliyunONSVO.GroupSub toSubVO(OnsGroupSubDetailResponse.Data.SubscriptionDataListItem subscriptionDataListItem) {
        return BeanCopierUtils.copyProperties(subscriptionDataListItem, AliyunONSVO.GroupSub.class);
    }

    public static AliyunONSVO.GroupSubDetail toSubDetailVO(OnsGroupSubDetailResponse.Data data) {
        AliyunONSVO.GroupSubDetail groupSubDetail = new AliyunONSVO.GroupSubDetail();
        groupSubDetail.setOnline(data.getOnline());
        groupSubDetail.setMessageModel(data.getMessageModel());
        List<AliyunONSVO.GroupSub> groupSubList = Lists.newArrayListWithCapacity(data.getSubscriptionDataList().size());
        data.getSubscriptionDataList().forEach(groupSub -> groupSubList.add(toSubVO(groupSub)));
        groupSubDetail.setSubList(groupSubList);
        return groupSubDetail;
    }

}
