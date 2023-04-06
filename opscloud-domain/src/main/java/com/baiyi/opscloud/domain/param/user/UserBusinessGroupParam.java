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

        @Schema(name = "用户组名称")
        private String name;

        @Schema(name = "用户组类型", example = "1")
        private Integer grpType;

        @Schema(name = "允许工单申请", example = "true")
        private Boolean allowOrder;

        @Schema(name = "扩展属性", example = "0")
        private Integer extend;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserUserGroupPageQuery extends PageParam {

        @Schema(name = "用户id")
        private Integer userId;

        @Schema(name = "查询名称")
        private String queryName;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserServerGroupPageQuery extends PageParam {

        @Schema(name = "用户id")
        private Integer userId;

        @Schema(name = "查询名称")
        private String queryName;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UserUserGroupPermission {

        @Schema(name = "用户id")
        private Integer userId;

        @Schema(name = "用户组id")
        private Integer userGroupId;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserTicketOcUserGroupQuery extends PageParam {

        @Schema(name = "查询名称")
        private String queryName;

        private Integer userId;

        @Schema(name = "工单票据id")
        private Integer workorderTicketId;

    }

}
