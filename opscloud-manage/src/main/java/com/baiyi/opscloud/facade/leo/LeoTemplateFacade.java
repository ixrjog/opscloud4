package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.leo.LeoTemplateParam;
import com.baiyi.opscloud.domain.vo.leo.LeoTemplateVO;

/**
 * @Author baiyi
 * @Date 2022/11/1 16:37
 * @Version 1.0
 */
public interface LeoTemplateFacade {

    DataTable<LeoTemplateVO.Template> queryLeoTemplatePage(LeoTemplateParam.TemplatePageQuery pageQuery);

    void addLeoTemplate(LeoTemplateParam.AddTemplate template);

    void updateLeoTemplate(LeoTemplateParam.UpdateTemplate template);

    LeoTemplateVO.Template updateLeoTemplateContent(LeoTemplateParam.UpdateTemplate template);

}
