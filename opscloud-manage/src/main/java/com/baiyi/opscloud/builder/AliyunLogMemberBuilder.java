package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.bo.AliyunLogMemberBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunLogMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogMemberParam;

/**
 * @Author baiyi
 * @Date 2020/6/15 9:35 上午
 * @Version 1.0
 */
public class AliyunLogMemberBuilder {

    public static OcAliyunLogMember build(AliyunLogMemberParam.AddLogMember addLogMember, OcServerGroup ocServerGroup) {
        AliyunLogMemberBO bo = AliyunLogMemberBO.builder()
                .logId(addLogMember.getLogId())
                .serverGroupId(ocServerGroup.getId())
                .serverGroupName(ocServerGroup.getName())
                .comment(addLogMember.getComment())
                .topic(addLogMember.getTopic())
                .build();
        return covert(bo);
    }

    private static OcAliyunLogMember covert(AliyunLogMemberBO bo) {
        return BeanCopierUtils.copyProperties(bo, OcAliyunLogMember.class);
    }
}
