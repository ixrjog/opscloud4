package com.baiyi.opscloud.packer.template;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTemplate;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import com.baiyi.opscloud.packer.sys.EnvPacker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    private final EnvPacker envPacker;

    private final DsAssetPacker dsAssetPacker;

    public List<BusinessTemplateVO.BusinessTemplate> wrapVOList(List<BusinessTemplate> data, IExtend iExtend) {
        return data.stream().map(e -> wrapVO(e, iExtend)).collect(Collectors.toList());
    }

    public BusinessTemplateVO.BusinessTemplate wrapVO(BusinessTemplate businessTemplate, IExtend iExtend) {
        BusinessTemplateVO.BusinessTemplate vo = BeanCopierUtil.copyProperties(businessTemplate, BusinessTemplateVO.BusinessTemplate.class);
        envPacker.wrap(vo);
        if (iExtend.getExtend()) {
            templatePacker.wrap(vo);
            dsInstancePacker.wrap(vo);
            dsAssetPacker.wrap(vo);
        }
        return vo;
    }

}
