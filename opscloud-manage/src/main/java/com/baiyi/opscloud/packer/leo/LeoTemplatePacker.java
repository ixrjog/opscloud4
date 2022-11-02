package com.baiyi.opscloud.packer.leo;

import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.leo.LeoTemplateVO;
import com.baiyi.opscloud.packer.IWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/11/1 19:56
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoTemplatePacker implements IWrapper<LeoTemplateVO.Template> {

    private final DsInstancePacker dsInstancePacker;

    @Override
    @TagsWrapper
    public void wrap(LeoTemplateVO.Template template, IExtend iExtend) {
        if (iExtend.getExtend())
            dsInstancePacker.wrap(template);
    }

}
