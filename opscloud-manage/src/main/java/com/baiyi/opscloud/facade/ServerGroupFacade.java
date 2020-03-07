package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.domain.vo.server.OcServerAttributeVO;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupTypeVO;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/21 11:00 上午
 * @Version 1.0
 */
public interface ServerGroupFacade {

    DataTable<OcServerGroupVO.ServerGroup> queryServerGroupPage(ServerGroupParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addServerGroup(OcServerGroupVO.ServerGroup serverGroup);

    BusinessWrapper<Boolean> updateServerGroup(OcServerGroupVO.ServerGroup serverGroup);

    BusinessWrapper<Boolean> deleteServerGroupById(int id);

    DataTable<OcServerGroupTypeVO.ServerGroupType> queryServerGroupTypePage(ServerGroupTypeParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addServerGroupType(OcServerGroupTypeVO.ServerGroupType serverGroupType);

    BusinessWrapper<Boolean> updateServerGroupType(OcServerGroupTypeVO.ServerGroupType serverGroupType);

    BusinessWrapper<Boolean> deleteServerGroupTypeById(int id);

    DataTable<OcServerGroupVO.ServerGroup> queryUserIncludeServerGroupPage(ServerGroupParam.UserServerGroupPageQuery pageQuery);

    DataTable<OcServerGroupVO.ServerGroup> queryUserExcludeServerGroupPage(ServerGroupParam.UserServerGroupPageQuery pageQuery);

    BusinessWrapper<Boolean> grantUserServerGroup(ServerGroupParam.UserServerGroupPermission userServerGroupPermission);

    BusinessWrapper<Boolean> revokeUserServerGroup(ServerGroupParam.UserServerGroupPermission userServerGroupPermission);

    List<OcServerAttributeVO.ServerAttribute> queryServerGroupAttribute(int id);

    BusinessWrapper<Boolean> saveServerGroupAttribute(OcServerAttributeVO.ServerAttribute serverAttribute);

}
