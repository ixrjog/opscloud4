package com.baiyi.opscloud.service.cloud;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTemplate;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:43 下午
 * @Version 1.0
 */
public interface OcCloudInstanceTemplateService {

    DataTable<OcCloudInstanceTemplate> fuzzyQueryOcCloudInstanceTemplateByParam(CloudInstanceTemplateParam.PageQuery pageQuery);

    void addOcCloudInstanceTemplate(OcCloudInstanceTemplate ocCloudInstanceTemplate);

    void updateOcCloudInstanceTemplate(OcCloudInstanceTemplate ocCloudInstanceTemplate);

    OcCloudInstanceTemplate queryOcCloudInstanceTemplateById(int id);

    void deleteOcCloudInstanceTemplateById(int id);
}
