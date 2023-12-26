package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.baiyi.opscloud.domain.param.leo.LeoTemplateParam;

/**
 * @Author baiyi
 * @Date 2022/11/1 14:16
 * @Version 1.0
 */
public interface LeoTemplateService {

    DataTable<LeoTemplate> queryTemplatePage(LeoTemplateParam.TemplatePageQuery pageQuery);

    void add(LeoTemplate leoTemplate);

    void update(LeoTemplate leoTemplate);

    void updateByPrimaryKeySelective(LeoTemplate leoTemplate);

    LeoTemplate getById(Integer id);

    void deleteById(Integer id);

}