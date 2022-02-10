package com.baiyi.opscloud.packer.template;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/12/6 11:16 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class BusinessTemplatePacker {

    private final TemplatePacker templatePacker;

    private final DsInstancePacker dsInstancePacker;

    private final DsAssetPacker dsAssetPacker;

    @EnvWrapper
    public void wrap(BusinessTemplateVO.BusinessTemplate businessTemplate, IExtend iExtend) {
        if (iExtend.getExtend()) {
            templatePacker.wrap(businessTemplate);
            dsInstancePacker.wrap(businessTemplate);
            dsAssetPacker.wrap(businessTemplate);
        }
    }

}
