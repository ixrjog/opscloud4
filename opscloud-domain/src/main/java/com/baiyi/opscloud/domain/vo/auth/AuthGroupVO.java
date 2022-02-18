package com.baiyi.opscloud.domain.vo.auth;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    public static class Group extends BaseVO {

        @ApiModelProperty(value = "资源数量")
        private Integer resourceSize;

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @ApiModelProperty(value = "资源组名称")
        private String groupName;

        @ApiModelProperty(value = "基本路径")
        private String basePath;

        @ApiModelProperty(value = "资源描述")
        private String comment;

    }

}
