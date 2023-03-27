package com.baiyi.opscloud.packer.template;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.template.MessageTemplateVO;
import com.baiyi.opscloud.packer.IWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/3/27 13:25
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class MessageTemplatePacker implements IWrapper<MessageTemplateVO.MessageTemplate> {

    @Override
    @EnvWrapper
    public void wrap(MessageTemplateVO.MessageTemplate messageTemplate, IExtend iExtend) {
        if (iExtend.getExtend()) {

        }
    }

}
