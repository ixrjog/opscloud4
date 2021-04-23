package com.baiyi.opscloud.bo.aliyun;

import com.aliyuncs.ons.model.v20190214.OnsConsumerStatusResponse;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/13 5:32 下午
 * @Since 1.0
 */
public class AliyunOnsGroupAlarmBO {

    @Data
    @Builder
    public static class HealthCheck {
        // 1为需要告警的groupId, -1为查询阿里云堆积信息失败的groupId
        private Integer status;
        private OnsConsumerStatusResponse.Data onsGroupStatus;
        private String groupId;
        private String instanceId;
        private String instanceName;
        private List<OcUser> userList;
    }
}
