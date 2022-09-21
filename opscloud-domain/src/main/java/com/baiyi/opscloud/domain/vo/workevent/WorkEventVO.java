package com.baiyi.opscloud.domain.vo.workevent;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkEventProperty;
import com.baiyi.opscloud.domain.generator.opscloud.WorkItem;
import com.baiyi.opscloud.domain.generator.opscloud.WorkRole;
import com.baiyi.opscloud.domain.vo.base.ShowTime;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public static class WorkEvent implements ShowTime.IAgo, TagVO.ITags {

        private final Integer businessType = BusinessTypeEnum.WORK_EVENT.getType();

        private Integer id;

        @ApiModelProperty(value = "工作角色id", example = "1")
        private Integer workRoleId;

        private WorkRole workRole;

        @ApiModelProperty(value = "工作类目id", example = "1")
        private Integer workItemId;

        private WorkItem workItem;

        private String workItemTree;

        @ApiModelProperty(value = "用户名")
        private String username;

        private User user;

        @ApiModelProperty(value = "时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date workEventTime;

        private String ago;

        @ApiModelProperty(value = "次数")
        private Integer workEventCnt;

        private String comment;

        private List<TagVO.Tag> tags;

        private List<WorkEventProperty> propertyList;

        private Map<String, String> property;

        @Override
        public Integer getBusinessId() {
            return this.id;
        }

        @Override
        public Date getAgoTime() {
            return this.workEventTime;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class Item {

        private Integer id;

        private Integer workRoleId;

        private Integer parentId;

        private String workItemName;

        private String workItemKey;

        private String color;

        private String comment;

        /**
         * 默认内容
         */
        private String content;

        public void setContent(String content) {
            this.comment = content;
        }

    }
}
