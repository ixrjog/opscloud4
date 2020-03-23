package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTemplateVO;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:41 下午
 * @Version 1.0
 */
public interface CloudInstanceFacade {

    DataTable<OcCloudInstanceTemplateVO.CloudInstanceTemplate> fuzzyQueryCloudInstanceTemplatePage(CloudInstanceTemplateParam.PageQuery pageQuery);
}
