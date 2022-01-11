package com.baiyi.opscloud.domain.vo.workorder;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

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
    @ApiModel
    public static class View implements Serializable {
        private static final long serialVersionUID = -69191171719476360L;
        private List<WorkOrderVO.Group> workOrderGroups;
    }

}
