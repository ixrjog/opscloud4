package com.baiyi.opscloud.cloud.mq.builder;

import com.aliyuncs.ons.model.v20190214.OnsGroupListResponse;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroup;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 8:57 下午
 * @Since 1.0
 */
public class AliyunONSGroupBuilder {

    public static OcAliyunOnsGroup build(OnsGroupListResponse.SubscribeInfoDo group) {
        OcAliyunOnsGroup ocAliyunOnsGroup = new OcAliyunOnsGroup();
        ocAliyunOnsGroup.setInstanceId(group.getInstanceId());
        ocAliyunOnsGroup.setGroupId(group.getGroupId());
        ocAliyunOnsGroup.setIndependentNaming(group.getIndependentNaming());
        ocAliyunOnsGroup.setGroupType(group.getGroupType());
        ocAliyunOnsGroup.setCreateTime(group.getCreateTime());
        ocAliyunOnsGroup.setRemark(group.getRemark());
        return ocAliyunOnsGroup;
    }
}
