package com.baiyi.opscloud.domain.vo.cloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/6/15 10:19 上午
 * @Version 1.0
 */
public class AliyunLogMemberVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LogMember {

        private AliyunLogVO.Log log;
        private OcServerGroup serverGroup;

        private String ago;

        private ArrayList<String>  machineList;

        private Integer id;
        private Integer logId;
        private Integer serverGroupId;
        private String serverGroupName;
        private String topic;
        private String comment;
        private Date lastPushTime;
        private Date createTime;
        private Date updateTime;

    }
}
