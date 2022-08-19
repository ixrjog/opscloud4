package com.baiyi.opscloud.domain.param.workevent;

import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.vo.workevent.WorkEventVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        private Integer workRoleId;

        private List<Integer> workItemIdList;

        private Date workEventStartTime;

        private Date workEventEndTime;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class WorkItemQuery extends PageParam {

        @NotNull(message = "parentId 不能为空")
        private Integer parentId;

        @NotNull(message = "角色 id 不能为空")
        private Integer workRoleId;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class AddWorkEvent {

        private List<WorkEventVO.WorkEvent> workEventList;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class UpdateWorkEvent {

        private WorkEventVO.WorkEvent workEvent;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class WorkItemTreeQuery {

        @NotNull
        private Integer workRoleId;

    }

}
