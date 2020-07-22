package com.baiyi.opscloud.domain.vo.server;

import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/7 5:08 下午
 * @Version 1.0
 */
public class ServerTaskVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerTask {

        private Map<String, List<ServerTaskMemberVO.ServerTaskMember>> memberMap;

        private String ago;

        private Map<String, String>  serverTarget;

        private UserVO.User user;

        // Statistics
        private ServerTastStatistics taskStatistics;

        private Integer id;
        private String executorParam;
        private Object executorParamDetail;
        private Integer userId;
        private Integer taskType;
        private Integer systemType;
        private String comment;
        private Integer taskSize;
        private Integer finalized;
        private Integer stopType;
        private Integer exitValue;
        private String taskStatus;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
        private Date updateTime;
        private String userDetail;
        private String serverTargetDetail;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerTastStatistics {

        private Integer total;
        private Integer successfulCount;
        private Integer failedCount;
        private Integer errorCount;

    }
}
