package com.baiyi.opscloud.domain.vo.task;

import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/18 3:43 下午
 * @Version 1.0
 */
public class ServerTaskVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ServerTask extends BaseVO implements ServerTaskMemberVO.IServerTaskMembers,
            AnsiblePlaybookVO.IPlaybook,
            UserVO.IUser,
            ReadableTime.IAgo,
            ReadableTime.IDuration {

        private List<ServerTaskMemberVO.Member> serverTaskMembers; // 任务成员

        @Override
        public Integer getServerTaskId() {
            return id;
        }

        private AnsiblePlaybookVO.Playbook playbook;

        private UserVO.User user;

        private Integer id;

        private String taskUuid;

        private String username;

        private Integer memberSize;

        private Integer ansiblePlaybookId;

        private String taskType;

        private String taskName; // 剧本名称

        private Boolean finalized;

        private Integer stopType;

        private String taskStatus;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;

        private String vars;

        private String tags;

        @Override
        public Date getAgoTime() {
            return startTime;
        }

        private String ago;

        private String duration;

    }

}