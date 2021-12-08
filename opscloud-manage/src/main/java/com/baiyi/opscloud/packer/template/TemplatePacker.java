package com.baiyi.opscloud.packer.template;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Template;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.template.TemplateVO;
import com.baiyi.opscloud.packer.sys.EnvPacker;
import com.baiyi.opscloud.service.template.BusinessTemplateService;
import com.baiyi.opscloud.service.template.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/12/6 11:21 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TemplatePacker {

    private final BusinessTemplateService bizTemplateService;

    private final TemplateService templateService;

    private final EnvPacker envPacker;

    public List<TemplateVO.Template> wrapVOList(List<Template> data, IExtend iExtend) {
        return data.stream().map(e -> wrapVO(e, iExtend)).collect(Collectors.toList());
    }

    public TemplateVO.Template wrapVO(Template template, IExtend iExtend) {
        TemplateVO.Template vo = BeanCopierUtil.copyProperties(template, TemplateVO.Template.class);
        if (iExtend.getExtend()) {
            envPacker.wrap(vo);
            vo.setBizTemplateSize(bizTemplateService.countByTemplateId(template.getId()));
        }
        return vo;
    }

    public void wrap(TemplateVO.ITempate iTempate) {
        Template template = templateService.getById(iTempate.getTemplateId());
        if (template == null) return;
        TemplateVO.Template vo = BeanCopierUtil.copyProperties(template, TemplateVO.Template.class);
        envPacker.wrap(vo);
        iTempate.setTemplate(vo);
    }

}
