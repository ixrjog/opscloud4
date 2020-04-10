package com.baiyi.opscloud.domain.vo.server;

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
public class OcServerTaskVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerTask {

        private Map<String,List<OcServerTaskMemberVO.ServerTaskMember>> memberMap;

        private Integer id;
        private Integer userId;
        private Integer taskType;
        private String comment;
        private Integer taskSize;
        private Integer finalized;
        private Integer stopType;
        private Integer exitValue;
        private String taskStatus;
        private Date createTime;
        private Date updateTime;
        private String userDetail;
        private String serverTargetDetail;

    }
}
