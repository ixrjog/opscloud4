package com.baiyi.opscloud.packer.template;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Template;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.template.TemplateVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.template.BusinessTemplateService;
import com.baiyi.opscloud.service.template.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/12/6 11:21 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TemplatePacker implements IWrapper<TemplateVO.Template> {

    private final BusinessTemplateService bizTemplateService;

    private final TemplateService templateService;

    @Override
    @EnvWrapper()
    public void wrap(TemplateVO.Template template, IExtend iExtend) {
        template.setBizTemplateSize(bizTemplateService.countByTemplateId(template.getId()));
    }

    public void wrap(TemplateVO.ITemplate iTemplate) {
        Template template = templateService.getById(iTemplate.getTemplateId());
        if (template == null) {
            return;
        }
        TemplateVO.Template vo = BeanCopierUtil.copyProperties(template, TemplateVO.Template.class);
        // envPacker.wrap(vo);
        iTemplate.setTemplate(vo);
    }

}
