package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:24 下午
 * @Version 1.0
 */
public class UserGroupParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UserGroup implements BusinessAssetRelationVO.IBusinessAssetRelation {

        private final Integer businessType = BusinessTypeEnum.USERGROUP.getType();

        @Override
        public String getBusinessUniqueKey() {
            return name;
        }

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "资产ID")
        private Integer assetId;

        @Schema(description = "主键")
        @Builder.Default
        private Integer id = 0;

        @NotBlank(message = "用户组名称不能为空")
        @Schema(description = "用户组名称")
        private String name;

        @Schema(description = "用户组类型")
        @Builder.Default
        private Integer groupType = 0;

        @Schema(description = "允许工单申请")
        private Boolean allowOrder;

        @Schema(description = "数据源")
        private String source;

        private String comment;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserGroupPageQuery extends PageParam implements IExtend {

        @Schema(description = "模糊查询")
        private String queryName;

        @Schema(description = "展开")
        private Boolean extend;

        @Schema(description = "是否激活")
        private Boolean isActive;
    }

}