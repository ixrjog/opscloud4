package com.baiyi.opscloud.datasource.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/29 6:03 下午
 * @Version 1.0
 */
public class DingtalkDepartment {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DepartmentSubIdResponse extends DingtalkResponse.Query implements Serializable {

        private static final long serialVersionUID = -1645875807947138110L;
        private Result result;
    }

    @Data
    public static class Result implements Serializable {

        private static final long serialVersionUID = -3578724346220243981L;
        @JsonProperty("dept_id_list")
        private List<Long> deptIdList;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetDepartmentResponse extends DingtalkResponse.Query implements Serializable {
        private static final long serialVersionUID = 1232648774708157947L;
        private Department result;
    }

    /**
     * https://developers.dingtalk.com/document/app/query-department-details0-v2
     */
    @Data
    public static class Department implements Serializable {

        private static final long serialVersionUID = 8992563745168682046L;
        @JsonProperty("dept_id")
        private Long deptId;
        private String name;
        @JsonProperty("parent_id")
        private Long parentId;
        @JsonProperty("hide_dept")
        private Boolean hideDept; // 是否隐藏本部门

    }

}
