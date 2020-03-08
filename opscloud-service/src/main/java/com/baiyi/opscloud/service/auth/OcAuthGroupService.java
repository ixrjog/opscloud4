package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAuthGroup;
import com.baiyi.opscloud.domain.param.auth.GroupParam;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:07 下午
 * @Version 1.0
 */
public interface OcAuthGroupService {

    OcAuthGroup queryOcAuthGroupById(int id);

    DataTable<OcAuthGroup> queryOcAuthGroupByParam(GroupParam.PageQuery pageQuery);

    void addOcAuthGroup(OcAuthGroup ocAuthGroup);

    void updateOcAuthGroup(OcAuthGroup ocAuthGroup);

    void deleteOcAuthGroupById(int id);

}
