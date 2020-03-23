package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcCloudInstanceTemplate;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;
import com.baiyi.opscloud.mapper.opscloud.OcCloudInstanceTemplateMapper;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:43 下午
 * @Version 1.0
 */
@Service
public class OcCloudInstanceTemplateServiceImpl implements OcCloudInstanceTemplateService {

    @Resource
    private OcCloudInstanceTemplateMapper ocCloudInstanceTemplateMapper;

    @Override
    public DataTable<OcCloudInstanceTemplate> fuzzyQueryOcCloudInstanceTemplateByParam(CloudInstanceTemplateParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcCloudInstanceTemplate> list = ocCloudInstanceTemplateMapper.fuzzyQueryOcCloudInstanceTemplateByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }
}
