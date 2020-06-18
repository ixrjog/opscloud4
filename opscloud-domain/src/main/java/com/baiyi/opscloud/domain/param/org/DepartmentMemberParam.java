package com.baiyi.opscloud.domain.param.org;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/4/21 4:08 下午
 * @Version 1.0
 */
public class DepartmentMemberParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "部门id", example = "1")
        private Integer departmentId;

        @ApiModelProperty(value = "用户组名称")
        private String queryName;

        @ApiModelProperty(value = "部门类型", example = "1")
        private Integer memberType;

        @ApiModelProperty(value = "经理", example = "1")
        private Integer isLeader;

        @ApiModelProperty(value = "审批", example = "1")
        private Integer isApprovalAuthority;

    }
}
