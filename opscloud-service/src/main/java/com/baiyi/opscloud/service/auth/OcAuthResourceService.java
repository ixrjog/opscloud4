package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcAuthResource;
import com.baiyi.opscloud.domain.param.auth.ResourceParam;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:05 下午
 * @Version 1.0
 */
public interface OcAuthResourceService {

    OcAuthResource queryOcAuthResourceByName(String resourceName);

    DataTable<OcAuthResource> queryOcAuthResourceByParam(ResourceParam.PageQuery pageQuery);

    void addOcAuthResource(OcAuthResource ocAuthResource);

    void updateOcAuthResource(OcAuthResource ocAuthResource);

    void deleteOcAuthResourceById(int id);

    OcAuthResource queryOcAuthResourceById(int id);

}