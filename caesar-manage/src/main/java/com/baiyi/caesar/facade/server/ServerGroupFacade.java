package com.baiyi.caesar.facade.server;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.domain.param.server.ServerGroupTypeParam;
import com.baiyi.caesar.vo.server.ServerGroupTypeVO;
import com.baiyi.caesar.vo.server.ServerGroupVO;

/**
 * @Author baiyi
 * @Date 2021/5/24 10:33 上午
 * @Version 1.0
 */
public interface ServerGroupFacade {

    DataTable<ServerGroupVO.ServerGroup> queryServerGroupPage(ServerGroupParam.ServerGroupPageQuery pageQuery);

    void addServerGroup(ServerGroupVO.ServerGroup serverGroup);

    void updateServerGroup(ServerGroupVO.ServerGroup serverGroup);

    void deleteServerGroupById(int id);

    DataTable<ServerGroupTypeVO.ServerGroupType> queryServerGroupTypePage(ServerGroupTypeParam.ServerGroupTypePageQuery pageQuery);

    void addServerGroupType(ServerGroupTypeVO.ServerGroupType serverGroupType);

    void updateServerGroupType(ServerGroupTypeVO.ServerGroupType serverGroupType);

    void deleteServerGroupTypeById(int id);
}
