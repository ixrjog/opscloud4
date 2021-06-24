package com.baiyi.caesar.service.server;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.ServerGroupType;
import com.baiyi.caesar.domain.param.server.ServerGroupTypeParam;

/**
 * @Author baiyi
 * @Date 2021/5/24 10:44 上午
 * @Version 1.0
 */
public interface ServerGroupTypeService {

    DataTable<ServerGroupType> queryPageByParam(ServerGroupTypeParam.ServerGroupTypePageQuery pageQuery);

    void update(ServerGroupType serverGroupType);

    void add(ServerGroupType serverGroupType);

    void deleteById(Integer id);

    ServerGroupType getById(Integer id);

    ServerGroupType getByName(String name);
}
