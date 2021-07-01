package com.baiyi.opscloud.facade.server.impl;

import com.baiyi.opscloud.algorithm.ServerAlgorithm;
import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.RegexUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.annotation.TagClear;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroupType;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.server.ServerGroupTypeVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.event.handler.ServerGroupEventHandler;
import com.baiyi.opscloud.event.param.ServerGroupEventParam;
import com.baiyi.opscloud.facade.server.ServerGroupFacade;
import com.baiyi.opscloud.facade.user.UserPermissionFacade;
import com.baiyi.opscloud.facade.user.base.IUserBusinessPermissionPageQuery;
import com.baiyi.opscloud.facade.user.factory.UserBusinessPermissionFactory;
import com.baiyi.opscloud.packer.server.ServerGroupPacker;
import com.baiyi.opscloud.packer.server.ServerGroupTypePacker;
import com.baiyi.opscloud.packer.user.UserPermissionPacker;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerGroupTypeService;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.util.ServerTreeUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * @Author baiyi
 * @Date 2021/5/24 10:33 上午
 * @Version 1.0
 */
@Service
public class ServerGroupFacadeImpl implements ServerGroupFacade, IUserBusinessPermissionPageQuery, InitializingBean {

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private ServerGroupTypeService serverGroupTypeService;

    @Resource
    private ServerGroupPacker serverGroupPacker;

    @Resource
    private UserPermissionPacker userPermissionPacker;

    @Resource
    private ServerGroupTypePacker serverGroupTypePacker;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private ServerService serverService;

    @Resource
    private ServerAlgorithm serverAlgorithm;

    @Resource
    private ServerTreeUtil serverTreeUtil;

    @Resource
    private ServerGroupEventHandler serverGroupEventHandler;

    @Override
    public int getBusinessType() {
        return BusinessTypeEnum.SERVERGROUP.getType();
    }

    @Override
    public DataTable<ServerGroupVO.ServerGroup> queryServerGroupPage(ServerGroupParam.ServerGroupPageQuery pageQuery) {
        DataTable<ServerGroup> table = serverGroupService.queryPageByParam(pageQuery);
        return new DataTable<>(serverGroupPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public DataTable<UserVO.IUserPermission> queryUserBusinessPermissionPage(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        DataTable<ServerGroup> table = serverGroupService.queryPageByParam(pageQuery);
        List<ServerGroupVO.ServerGroup> data = serverGroupPacker.wrapVOList(table.getData(), pageQuery);
        if (pageQuery.getAuthorized())
            data.forEach(e -> {
                e.setUserId(pageQuery.getUserId());
                userPermissionPacker.wrap(e);
            });
        return new DataTable<>(Lists.newArrayList(data), table.getTotalNum());
    }

    @Override
    public void addServerGroup(ServerGroupVO.ServerGroup serverGroup) {
        try {
            serverGroupService.add(toDO(serverGroup));
        } catch (Exception ex) {
            throw new CommonRuntimeException(ErrorEnum.SERVERGROUP_NAME_ALREADY_EXIST);
        }
    }

    @Override
    public void updateServerGroup(ServerGroupVO.ServerGroup serverGroup) {
        try {
            ServerGroupCacheEvict(serverGroup.getId());
            serverGroupService.update(toDO(serverGroup));
        } catch (Exception ex) {
            throw new CommonRuntimeException(ErrorEnum.SERVERGROUP_NAME_ALREADY_EXIST);
        }
    }

    @TagClear(type = BusinessTypeEnum.SERVERGROUP)
    @Override
    public void deleteServerGroupById(int id) {
        ServerGroupEventParam.delete delete = ServerGroupEventParam.delete.builder()
                .id(id).build();
        serverGroupEventHandler.deleteHandle(delete);
    }

    private ServerGroup toDO(ServerGroupVO.ServerGroup serverGroup) {
        ServerGroup pre = BeanCopierUtil.copyProperties(serverGroup, ServerGroup.class);
        RegexUtil.tryServerGroupNameRule(pre.getName()); // 名称规范
        return pre;
    }

    @Override
    public DataTable<ServerGroupTypeVO.ServerGroupType> queryServerGroupTypePage(ServerGroupTypeParam.ServerGroupTypePageQuery pageQuery) {
        DataTable<ServerGroupType> table = serverGroupTypeService.queryPageByParam(pageQuery);
        return new DataTable<>(serverGroupTypePacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public void addServerGroupType(ServerGroupTypeVO.ServerGroupType serverGroupType) {
        serverGroupTypeService.add(BeanCopierUtil.copyProperties(serverGroupType, ServerGroupType.class));
    }

    @Override
    public void updateServerGroupType(ServerGroupTypeVO.ServerGroupType serverGroupType) {
        serverGroupTypeService.update(BeanCopierUtil.copyProperties(serverGroupType, ServerGroupType.class));
    }

    @Override
    public void deleteServerGroupTypeById(int id) {
        if (serverGroupService.countByServerGroupTypeId(id) > 0)
            throw new CommonRuntimeException(ErrorEnum.SERVERGROUP_TYPE_HAS_USED);
        serverGroupTypeService.deleteById(id);
    }

    @Override
    public ServerTreeVO.ServerTree queryServerTree(ServerGroupParam.UserServerTreeQuery queryParam, User user) {
        // 过滤空服务器组
        int accessLevel = userPermissionFacade.getUserAccessLevel(user.getUsername());
        queryParam.setIsAdmin(accessLevel >= AccessLevel.OPS.getLevel());

        List<ServerGroup> groups
                = serverGroupService.queryUserServerGroupTreeByParam(queryParam).stream()
                .filter(g -> serverService.countByServerGroupId(g.getId()) != 0)
                .collect(Collectors.toList());

        List<ServerTreeVO.Tree> treeList = Lists.newArrayList();
        AtomicInteger treeSize = new AtomicInteger();

        for (ServerGroup group : groups) {
            Map<String, List<Server>> serverGroupMap = serverAlgorithm.grouping(group);
            treeList.add(serverTreeUtil.wrap(group, serverGroupMap));
            treeSize.addAndGet(serverTreeUtil.getServerGroupMapSize(serverGroupMap));
        }

        return ServerTreeVO.ServerTree.builder()
                .userId(user.getId())
                .tree(treeList)
                .size(treeSize.get())
                .build();
    }

    @Override
    public void ServerGroupCacheEvict(Integer serverGroupId) {
        serverTreeUtil.evictWrap(serverGroupId);
        serverAlgorithm.evictGrouping(serverGroupId);
    }


    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        UserBusinessPermissionFactory.register(this);
    }
}
