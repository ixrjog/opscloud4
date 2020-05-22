package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.domain.param.user.UserServerTreeParam;
import com.baiyi.opscloud.domain.vo.server.ServerAttributeVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupTypeVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/21 11:00 上午
 * @Version 1.0
 */
public interface ServerGroupFacade {

    DataTable<ServerGroupVO.ServerGroup> queryServerGroupPage(ServerGroupParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addServerGroup(ServerGroupVO.ServerGroup serverGroup);

    BusinessWrapper<Boolean> updateServerGroup(ServerGroupVO.ServerGroup serverGroup);

    BusinessWrapper<Boolean> deleteServerGroupById(int id);

    DataTable<ServerGroupTypeVO.ServerGroupType> queryServerGroupTypePage(ServerGroupTypeParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addServerGroupType(ServerGroupTypeVO.ServerGroupType serverGroupType);

    BusinessWrapper<Boolean> updateServerGroupType(ServerGroupTypeVO.ServerGroupType serverGroupType);

    BusinessWrapper<Boolean> deleteServerGroupTypeById(int id);

    DataTable<ServerGroupVO.ServerGroup> queryUserIncludeServerGroupPage(ServerGroupParam.UserServerGroupPageQuery pageQuery);

    DataTable<ServerGroupVO.ServerGroup> queryUserExcludeServerGroupPage(ServerGroupParam.UserServerGroupPageQuery pageQuery);

    BusinessWrapper<Boolean> grantUserServerGroup(ServerGroupParam.UserServerGroupPermission userServerGroupPermission);

    BusinessWrapper<Boolean> revokeUserServerGroup(ServerGroupParam.UserServerGroupPermission userServerGroupPermission);

    List<ServerAttributeVO.ServerAttribute> queryServerGroupAttribute(int id);

    BusinessWrapper<Boolean> saveServerGroupAttribute(ServerAttributeVO.ServerAttribute serverAttribute);

    ServerTreeVO.MyServerTree queryUserServerTree(UserServerTreeParam.UserServerTreeQuery userServerTreeQuery, OcUser ocUser);

    BusinessWrapper<Boolean> getServerTreeHostPatternMap(String uuid, OcUser ocUser);

}
