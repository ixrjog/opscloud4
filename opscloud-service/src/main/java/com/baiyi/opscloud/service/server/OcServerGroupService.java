package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.user.UserServerTreeParam;

import java.util.List;

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

    /**
     * 查询用户授权的服务器组信息
     *
     * @param userId
     * @return
     */
    List<OcServerGroup> queryUserPermissionOcServerGroupByUserId(int userId);

    DataTable<OcServerGroup> queryUserIncludeOcServerGroupByParam(ServerGroupParam.UserServerGroupPageQuery pageQuery);

    DataTable<OcServerGroup> queryUserExcludeOcServerGroupByParam(ServerGroupParam.UserServerGroupPageQuery pageQuery);

    List<OcServerGroup> queryUserPermissionOcServerGroupByParam(UserServerTreeParam.UserServerTreeQuery pageQuery);

    List<OcServerGroup> queryUserTicketOcServerGroupByParam(ServerGroupParam.UserTicketOcServerGroupQuery pageQuery);

    List<OcServerGroup> queryLogMemberOcServerGroupByParam(ServerGroupParam.LogMemberServerGroupQuery pageQuery);

    List<OcServerGroup> queryAll();
}
