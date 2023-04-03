package com.baiyi.opscloud.domain.param.server;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:48 下午
 * @Version 1.0
 */
public class ServerParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ServerPageQuery extends SuperPageParam implements IExtend {

        @Schema(name = "关键字查询")
        private String queryName;

//        @Schema(name = "查询ip")
//        private String queryIp;

        @Schema(name = "服务器组ID")
        private Integer serverGroupId;

        @Schema(name = "环境类型")
        private Integer envType;

        @Schema(name = "有效")
        private Boolean isActive;

        @Schema(name = "状态")
        private Integer serverStatus;

        @Schema(name = "监控状态")
        private Integer monitorStatus;

        @Schema(name = "标签ID")
        private Integer tagId;

        private final Integer businessType = BusinessTypeEnum.SERVER.getType();

        private Boolean extend;

    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UserRemoteServerPageQuery extends PageParam implements IExtend {

        private Integer userId;

        @Schema(name = "关键字查询")
        private String queryName;

        @Schema(name = "服务器组id")
        private Integer serverGroupId;

        @Schema(name = "环境类型")
        private Integer envType;

        @Schema(name = "有效")
        private Boolean isActive;

        @Schema(name = "状态")
        private Integer serverStatus;

        @Schema(name = "标签id")
        private Integer tagId;

        private final Integer businessType = BusinessTypeEnum.SERVERGROUP.getType();

        private Boolean extend;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UserPermissionServerPageQuery extends SuperPageParam implements IExtend {

        @Schema(name = "用户id")
        @NotNull
        private Integer userId;

        @Schema(name = "服务器名")
        private String name;

        @Schema(name = "查询ip")
        private String queryIp;

        private final Integer businessType = BusinessTypeEnum.SERVERGROUP.getType();

        private Boolean extend;

    }

}
