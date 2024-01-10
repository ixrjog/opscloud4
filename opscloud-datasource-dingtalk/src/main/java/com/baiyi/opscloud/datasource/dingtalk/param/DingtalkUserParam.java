package com.baiyi.opscloud.datasource.dingtalk.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/11/29 4:12 下午
 * @Version 1.0
 */
public class DingtalkUserParam {

    /**
     * https://developers.dingtalk.com/document/app/queries-the-complete-information-of-a-department-user
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class QueryUserPage {
        // 分页查询的游标，最开始传0，后续传返回参数中的next_cursor值。
        @Builder.Default
        private Integer cursor = 0;
        // 是否返回访问受限的员工。
        @Builder.Default
        @JsonProperty("contain_access_limit")
        private Boolean containAccessLimit = false;
        @Builder.Default
        private Integer size = 100;
        /**
         * 部门成员的排序规则，默认不传是按自定义排序（custom）：
         * <p>
         * entry_asc：代表按照进入部门的时间升序
         * entry_desc：代表按照进入部门的时间降序
         * modify_asc：代表按照部门信息修改时间升序
         * modify_desc：代表按照部门信息修改时间降序
         * custom：代表用户定义(未定义时按照拼音)排序
         */
        @Builder.Default
        @JsonProperty("order_field")
        private String orderField = "modify_desc";
        @Builder.Default
        private String language = "zh_CN";
        @Builder.Default
        @JsonProperty("dept_id")
        private Long deptId = 1L;
    }

}