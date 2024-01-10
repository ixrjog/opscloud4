package com.baiyi.opscloud.service.template.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.domain.param.template.MessageTemplateParam;
import com.baiyi.opscloud.mapper.MessageTemplateMapper;
import com.baiyi.opscloud.service.template.MessageTemplateService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/2 10:42 AM
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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

    @Override
    public DataTable<MessageTemplate> queryPageByParam(MessageTemplateParam.MessageTemplatePageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(MessageTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            criteria.andLike("name", SQLUtil.toLike(pageQuery.getQueryName()))
                    .orLike("msgKey", SQLUtil.toLike(pageQuery.getQueryName()));
        }
        List<MessageTemplate> data = messageTemplateMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void updateByPrimaryKeySelective(MessageTemplate messageTemplate) {
        messageTemplateMapper.updateByPrimaryKeySelective(messageTemplate);
    }

}