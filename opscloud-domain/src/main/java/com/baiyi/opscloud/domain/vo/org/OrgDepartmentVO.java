package com.baiyi.opscloud.domain.vo.org;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/22 1:27 下午
 * @Version 1.0
 */
public class OrgDepartmentVO {
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Department {

        private Integer id;
        private String name;
        private Integer parentId;
        private Integer deptHiding;
        private Integer deptType;
        private Integer deptOrder;
        private Date createTime;
        private Date updateTime;
        private String comment;
    }
}
