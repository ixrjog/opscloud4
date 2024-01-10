package com.baiyi.opscloud.domain.vo.workevent;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.FrontEndTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.WorkEventProperty;
import com.baiyi.opscloud.domain.generator.opscloud.WorkItem;
import com.baiyi.opscloud.domain.generator.opscloud.WorkRole;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
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
    @Schema
    public static class WorkEvent implements ReadableTime.IAgo, TagVO.ITags, UserVO.IUser, Serializable {

        @Serial
        private static final long serialVersionUID = 5241869056680855688L;

        private final Integer businessType = BusinessTypeEnum.WORK_EVENT.getType();

        private Integer id;

        @Schema(description = "工作角色ID", example = "1")
        private Integer workRoleId;

        private WorkRole workRole;

        @Schema(description = "工作类目ID", example = "1")
        private Integer workItemId;

        private WorkItem workItem;

        private String workItemTree;

        @Schema(description = "用户名")
        private String username;

        private UserVO.User user;

        @Schema(description = "时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date workEventTime;

        private String ago;

        @Schema(description = "次数")
        private Integer workEventCnt;

        private String comment;

        private List<TagVO.Tag> tags;

        private List<WorkEventProperty> propertyList;

        private List<EventProperty> properties;

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
    @Schema
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

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class EventProperty implements Serializable {

        @Serial
        private static final long serialVersionUID = 8869809924735578200L;

        // 故障
        public static final EventProperty FAULT = EventProperty.builder()
                .feType(FrontEndTypeConstants.DANGER.getType())
                .feName("故障")
                .build();

        // 拦截
        public static final EventProperty INTERCEPT = EventProperty.builder()
                .feType(FrontEndTypeConstants.SUCCESS.getType())
                .feName("拦截")
                .build();

        public static final EventProperty NOT_INTERCEPT = EventProperty.builder()
                .feType(FrontEndTypeConstants.WARNING.getType())
                .feName("未拦截")
                .build();

        // 处理中
        public static final EventProperty SOLVE = EventProperty.builder()
                .feType(FrontEndTypeConstants.INFO.getType())
                .feName("处理中")
                .build();

        // 不显示
        public static final EventProperty NO_SHOW = WorkEventVO.EventProperty.builder()
                .isShow(false)
                .build();

        @Schema(description = "前端类型")
        private String feType;

        @Schema(description = "前端名称")
        private String feName;

        @Builder.Default
        private Boolean isShow = true;

    }

}