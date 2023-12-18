package com.baiyi.opscloud.domain.vo.workorder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 1:42 PM
 * @Version 1.0
 */
public class WorkOrderViewVO {

    @Builder
    @Data
    @Schema
    public static class View implements Serializable {
        @Serial
        private static final long serialVersionUID = -69191171719476360L;
        private List<WorkOrderVO.Group> workOrderGroups;
    }

}