package com.baiyi.opscloud.domain.vo.profile;

import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:02 上午
 * @Version 1.0
 */
public class ProfileSubscriptionVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ProfileSubscription {

        // List<OcServer>
        private List<ServerVO.Server> servers;
        private ServerGroupVO.ServerGroup serverGroup;

        // 脚本
        private AnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook;

        private Integer id;
        private String subscriptionType;
        private String name;
        private Integer serverGroupId;
        private Integer scriptId;
        private String hostPattern;
        private String comment;
        private Integer serverTaskId;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date executionTime;
        private Date createTime;
        private Date updateTime;
        private String vars;

    }
}
