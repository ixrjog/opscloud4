package com.baiyi.opscloud.domain.param.workevent;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.vo.workevent.WorkEventVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author 修远
 * @Date 2022/8/9 3:24 PM
 * @Since 1.0
 */
public class WorkEventParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class WorkEventPageQuery extends PageParam {

        private final int businessType = BusinessTypeEnum.WORK_EVENT.getType();

        @Schema(description = "模糊查询")
        private String queryName;

        private Integer workRoleId;

        private String username;

        private List<Integer> workItemIdList;

        private Date workEventStartTime;

        private Date workEventEndTime;

        private Integer tagId;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class WorkItemQuery extends PageParam {

        @NotNull(message = "parentId不能为空")
        private Integer parentId;

        @NotNull(message = "角色ID不能为空")
        private Integer workRoleId;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddWorkEvent {

        private List<WorkEventVO.WorkEvent> workEventList;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UpdateWorkEvent {

        private WorkEventVO.WorkEvent workEvent;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class WorkItemTreeQuery {

        @NotNull
        private Integer workRoleId;

    }

}