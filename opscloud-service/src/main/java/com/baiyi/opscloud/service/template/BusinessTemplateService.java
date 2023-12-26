package com.baiyi.opscloud.service.template;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTemplate;
import com.baiyi.opscloud.domain.param.template.BusinessTemplateParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/6 11:03 AM
 * @Version 1.0
 */
public interface BusinessTemplateService {

    DataTable<BusinessTemplate> queryPageByParam(BusinessTemplateParam.BusinessTemplatePageQuery pageQuery);

    void add(BusinessTemplate businessTemplate);

    void update(BusinessTemplate businessTemplate);

    List<BusinessTemplate> queryByBusinessId(int businessId);

    List<BusinessTemplate> queryByInstanceUuid(String instanceUuid);

    List<BusinessTemplate> queryByTemplateId(int templateId);

    BusinessTemplate getById(Integer id);

    void deleteById(int id);

    int countByTemplateId(int templateId);

}