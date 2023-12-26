package com.baiyi.opscloud.service.template;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.domain.param.template.MessageTemplateParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/2 10:41 AM
 * @Version 1.0
 */
public interface MessageTemplateService {

    MessageTemplate getById(Integer id);

    MessageTemplate getByUniqueKey(String msgKey, String consumer, String msgType);

    List<MessageTemplate> getByMsgKeyAndType(String msgKey, String msgType);

    DataTable<MessageTemplate> queryPageByParam(MessageTemplateParam.MessageTemplatePageQuery pageQuery);

    void updateByPrimaryKeySelective(MessageTemplate messageTemplate);

}