package com.baiyi.opscloud.packer.leo;

import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.leo.LeoTemplateVO;
import com.baiyi.opscloud.leo.domain.model.LeoTemplateModel;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.leo.LeoJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/1 19:56
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoTemplatePacker implements IWrapper<LeoTemplateVO.Template> {

    private final DsInstancePacker dsInstancePacker;

    private final LeoJobService leoJobService;

    @Override
    @TagsWrapper
    public void wrap(LeoTemplateVO.Template template, IExtend iExtend) {
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(template.getTemplateConfig());
        // 注入版本
        String version = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getVersion)
                .orElse("0.0.0");
        template.setVersion(version);
        if (iExtend.getExtend()) {
            dsInstancePacker.wrap(template);
            // 任务数量
            template.setJobSize(leoJobService.countWithTemplateId(template.getId()));
        }
    }

}
