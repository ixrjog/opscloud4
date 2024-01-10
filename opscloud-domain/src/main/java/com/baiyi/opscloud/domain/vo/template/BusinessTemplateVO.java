package com.baiyi.opscloud.domain.vo.template;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/12/6 10:44 AM
 * @Version 1.0
 */
public class BusinessTemplateVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class BusinessTemplate extends BaseVO implements EnvVO.IEnv,
            TemplateVO.ITemplate,
            BaseBusiness.IBusiness,
            DsInstanceVO.IDsInstance,
            DsAssetVO.IDsAsset {

        @Schema(description = "前端按钮使用", example = "false")
        @Builder.Default
        private final Boolean creating = false;

        private DsInstanceVO.Instance instance;

        private DsAssetVO.Asset asset;

        private TemplateVO.Template template;

        private EnvVO.Env env;

        private Integer id;

        private String name;

        private String instanceUuid;

        private Integer businessType;

        private Integer businessId;

        private Integer templateId;

        private Integer envType;

        private String vars;

        private String kind;

        private String content;

        private String comment;

        @Override
        public Integer getAssetId() {
            if (BusinessTypeEnum.ASSET.getType() == businessType) {
                return businessId;
            }
            return 0;
        }

    }

}