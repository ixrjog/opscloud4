package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcServerGroup;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;

/**
 * @Author baiyi
 * @Date 2020/1/8 10:12 上午
 * @Version 1.0
 */
public interface OcServerGroupService {

    int countByGrpType(int grpType);

    OcServerGroup queryOcServerGroupById(Integer id);

    OcServerGroup queryOcServerGroupByName(String name);

    DataTable<OcServerGroup> queryOcServerGroupByParam(ServerGroupParam.PageQuery pageQuery);

    void addOcServerGroup(OcServerGroup ocServerGroup);

    void updateOcServerGroup(OcServerGroup ocServerGroup);

    void deleteOcServerGroupById(int id);
}
