package com.baiyi.opscloud.facade.server;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.domain.vo.server.ServerGroupTypeVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/24 10:33 上午
 * @Version 1.0
 */
public interface ServerGroupFacade {

    DataTable<ServerGroupVO.ServerGroup> queryServerGroupPage(ServerGroupParam.ServerGroupPageQuery pageQuery);

    void addServerGroup(ServerGroupParam.AddServerGroup addServerGroup);

    void updateServerGroup(ServerGroupParam.UpdateServerGroup updateServerGroup);

    void deleteServerGroupById(int id);

    DataTable<ServerGroupTypeVO.ServerGroupType> queryServerGroupTypePage(ServerGroupTypeParam.ServerGroupTypePageQuery pageQuery);

    void addServerGroupType(ServerGroupTypeParam.AddServerGroupType addServerGroupType);

    void updateServerGroupType(ServerGroupTypeParam.UpdateServerGroupType updateServerGroupType);

    void deleteServerGroupTypeById(int id);

    ServerTreeVO.ServerTree queryServerTree(ServerGroupParam.UserServerTreeQuery queryParam, User user);

    Map<String, List<Server>> queryServerGroupHostPatternByEnv(ServerGroupParam.ServerGroupEnvHostPatternQuery query);

}
