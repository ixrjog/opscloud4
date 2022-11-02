package com.baiyi.opscloud.packer.leo;

import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.leo.LeoTemplateVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.packer.ServerPackerDelegate;
import com.baiyi.opscloud.packer.business.BusinessPropertyPacker;
import com.baiyi.opscloud.packer.server.ServerAccountPacker;
import com.baiyi.opscloud.packer.server.ServerGroupPacker;
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

    private final ServerAccountPacker accountPacker;

    private final ServerGroupPacker serverGroupPacker;

    private final BusinessPropertyPacker businessPropertyPacker;

    private final ServerPackerDelegate serverPackerDelegate;

    @Override
    @TagsWrapper
    public void wrap(LeoTemplateVO.Template template, IExtend iExtend) {
    }

}
