package com.baiyi.opscloud.service.message.impl;

import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.mapper.opscloud.MessageTemplateMapper;
import com.baiyi.opscloud.service.message.MessageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/2 10:42 AM
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class MessageTemplateServiceImpl implements MessageTemplateService {

    private final MessageTemplateMapper messageTemplateMapper;

    @Override
    public MessageTemplate getById(Integer id) {
        return messageTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public MessageTemplate getByUniqueKey(String msgKey, String consumer, String msgType) {
        Example example = new Example(MessageTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("msgKey", msgKey)
                .andEqualTo("msgType", msgType)
                .andEqualTo("consumer", consumer);
        return messageTemplateMapper.selectOneByExample(example);
    }

    @Override
    public List<MessageTemplate> getByMsgKeyAndType(String msgKey, String msgType) {
        Example example = new Example(MessageTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("msgKey", msgKey)
                .andEqualTo("msgType", msgType);
        return messageTemplateMapper.selectByExample(example);
    }

}
