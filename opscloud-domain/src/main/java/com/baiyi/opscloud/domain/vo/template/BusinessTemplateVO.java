package com.baiyi.opscloud.domain.vo.template;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import io.swagger.annotations.ApiModel;
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
    @ApiModel
    public static class BusinessTemplate extends BaseVO implements EnvVO.IEnv,
            TemplateVO.ITempate,
            BaseBusiness.IBusiness,
            DsInstanceVO.IDsInstance {

        private DsInstanceVO.Instance instance;

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

        private String content;

        private String comment;
    }

}
