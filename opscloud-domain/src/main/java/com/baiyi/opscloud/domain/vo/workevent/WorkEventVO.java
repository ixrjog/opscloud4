package com.baiyi.opscloud.domain.vo.workevent;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkItem;
import com.baiyi.opscloud.domain.generator.opscloud.WorkRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author 修远
 * @Date 2022/8/12 10:25 AM
 * @Since 1.0
 */
public class WorkEventVO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class WorkEvent {

        private Integer id;

        @ApiModelProperty(value = "工作角色id", example = "1")
        private Integer workRoleId;

        private WorkRole workRole;

        @ApiModelProperty(value = "工作类目id", example = "1")
        private Integer workItemId;

        private WorkItem workItem;

        @ApiModelProperty(value = "用户名")
        private String username;

        private User user;

        @ApiModelProperty(value = "时间")
        private Date workEventTime;

        @ApiModelProperty(value = "yyyy 年 WW 周")
        private String weeks;

        @ApiModelProperty(value = "次数")
        private Integer workEventCnt;

        private String comment;
    }
}
