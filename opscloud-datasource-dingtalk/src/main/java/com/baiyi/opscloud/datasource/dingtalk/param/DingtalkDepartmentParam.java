package com.baiyi.opscloud.datasource.dingtalk.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/11/29 5:55 下午
 * @Version 1.0
 */
public class DingtalkDepartmentParam {

    /**
     * https://developers.dingtalk.com/document/app/obtain-a-sub-department-id-list-v2
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ListSubDepartmentId {

        @Builder.Default
        @JsonProperty("dept_id")
        private Long deptId = 1L;

    }

    /**
     * https://developers.dingtalk.com/document/app/query-department-details0-v2
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class GetDepartment {

        @Builder.Default
        @JsonProperty("dept_id")
        private Long deptId = 1L;

        @Builder.Default
        private String language ="zh_CN";

    }

}