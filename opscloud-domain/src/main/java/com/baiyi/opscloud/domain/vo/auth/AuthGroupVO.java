package com.baiyi.opscloud.domain.vo.auth;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/15 5:23 下午
 * @Version 1.0
 */
public class AuthGroupVO {

    public interface IAuthGroup {

        Integer getGroupId();

        void setGroup(Group group);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Group extends BaseVO {

        @Schema(description = "资源数量")
        private Integer resourceSize;

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "资源组名称")
        private String groupName;

        @Schema(description = "基本路径")
        private String basePath;

        @Schema(description = "资源描述")
        private String comment;

    }

}