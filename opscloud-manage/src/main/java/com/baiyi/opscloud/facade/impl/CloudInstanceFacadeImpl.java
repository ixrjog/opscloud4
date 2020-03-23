package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcCloudInstanceTemplate;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTemplateVO;
import com.baiyi.opscloud.facade.CloudInstanceFacade;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:42 下午
 * @Version 1.0
 */
@Service
public class CloudInstanceFacadeImpl implements CloudInstanceFacade {

    @Resource
    private OcCloudInstanceTemplateService ocCloudInstanceTemplateService;

    @Override
    public DataTable<OcCloudInstanceTemplateVO.CloudInstanceTemplate> fuzzyQueryCloudInstanceTemplatePage(CloudInstanceTemplateParam.PageQuery pageQuery) {
        DataTable<OcCloudInstanceTemplate> table = ocCloudInstanceTemplateService.fuzzyQueryOcCloudInstanceTemplateByParam(pageQuery);
        List<OcCloudInstanceTemplateVO.CloudInstanceTemplate> page = BeanCopierUtils.copyListProperties(table.getData(), OcCloudInstanceTemplateVO.CloudInstanceTemplate.class);
        DataTable<OcCloudInstanceTemplateVO.CloudInstanceTemplate> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

}
