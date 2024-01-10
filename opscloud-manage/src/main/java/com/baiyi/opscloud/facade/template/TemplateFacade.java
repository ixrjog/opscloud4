package com.baiyi.opscloud.facade.template;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.template.BusinessTemplateParam;
import com.baiyi.opscloud.domain.param.template.MessageTemplateParam;
import com.baiyi.opscloud.domain.param.template.TemplateParam;
import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
import com.baiyi.opscloud.domain.vo.template.MessageTemplateVO;
import com.baiyi.opscloud.domain.vo.template.TemplateVO;

/**
 * @Author baiyi
 * @Date 2021/12/6 10:58 AM
 * @Version 1.0
 */
public interface TemplateFacade {

    DataTable<TemplateVO.Template> queryTemplatePage(TemplateParam.TemplatePageQuery pageQuery);

    TemplateVO.Template addTemplate(TemplateParam.Template template);

    TemplateVO.Template updateTemplate(TemplateParam.Template template);

    void deleteTemplateById(int id);

    /**
     * 分页查询业务模板详情
     *
     * @param pageQuery
     * @return
     */
    DataTable<BusinessTemplateVO.BusinessTemplate> queryBusinessTemplatePage(BusinessTemplateParam.BusinessTemplatePageQuery pageQuery);

    BusinessTemplateVO.BusinessTemplate addBusinessTemplate(BusinessTemplateParam.BusinessTemplate businessTemplate);

    BusinessTemplateVO.BusinessTemplate updateBusinessTemplate(BusinessTemplateParam.BusinessTemplate businessTemplate);

    BusinessTemplateVO.BusinessTemplate createAssetByBusinessTemplate(int id);

    void scanBusinessTemplateByInstanceUuid(String instanceUuid);

    void deleteBusinessTemplateById(int id);

    /**
     * 分页查询消息模板详情
     *
     * @param pageQuery
     * @return
     */
    DataTable<MessageTemplateVO.MessageTemplate> queryMessageTemplatePage(MessageTemplateParam.MessageTemplatePageQuery pageQuery);

    void updateMessageTemplate(MessageTemplateParam.UpdateMessageTemplate messageTemplate);

    OptionsVO.Options getKindOptions();

}