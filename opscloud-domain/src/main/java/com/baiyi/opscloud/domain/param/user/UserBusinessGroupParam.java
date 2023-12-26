package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:50 下午
 * @Version 1.0
 */
public class UserBusinessGroupParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class PageQuery extends PageParam {

        @Schema(description = "用户组名称")
        private String name;

        @Schema(description = "用户组类型", example = "1")
        private Integer grpType;

        @Schema(description = "允许工单申请", example = "true")
        private Boolean allowOrder;

        @Schema(description = "扩展属性", example = "0")
        private Integer extend;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserUserGroupPageQuery extends PageParam {

        @Schema(description = "用户ID")
        private Integer userId;

        @Schema(description = "查询名称")
        private String queryName;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserServerGroupPageQuery extends PageParam {

        @Schema(description = "用户ID")
        private Integer userId;

        @Schema(description = "查询名称")
        private String queryName;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UserUserGroupPermission {

        @Schema(description = "用户ID")
        private Integer userId;

        @Schema(description = "用户组ID")
        private Integer userGroupId;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserTicketOcUserGroupQuery extends PageParam {

        @Schema(description = "查询名称")
        private String queryName;

        private Integer userId;

        @Schema(description = "工单票据ID")
        private Integer workorderTicketId;

    }

}