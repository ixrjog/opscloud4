package com.baiyi.opscloud.facade.server.impl;

import com.baiyi.opscloud.algorithm.ServerPack;
import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.ansible.ServerGroupingAlgorithm;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.annotation.*;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroupType;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupTypeVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.server.converter.ServerGroupConverter;
import com.baiyi.opscloud.facade.server.ServerGroupFacade;
import com.baiyi.opscloud.facade.user.UserPermissionFacade;
import com.baiyi.opscloud.facade.user.base.IUserBusinessPermissionPageQuery;
import com.baiyi.opscloud.facade.user.factory.UserBusinessPermissionFactory;
import com.baiyi.opscloud.factory.resource.base.AbstractAppResQuery;
import com.baiyi.opscloud.packer.server.ServerGroupPacker;
import com.baiyi.opscloud.packer.server.ServerGroupTypePacker;
import com.baiyi.opscloud.packer.user.UserPermissionPacker;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerGroupTypeService;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.util.ServerTreeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * @Author baiyi
 * @Date 2021/5/24 10:33 上午
 * @Version 1.0
 */
@ApplicationResType(ApplicationResTypeEnum.SERVERGROUP)
@BusinessType(BusinessTypeEnum.SERVERGROUP)
@Service
@RequiredArgsConstructor
public class ServerGroupFacadeImpl extends AbstractAppResQuery implements ServerGroupFacade, IUserBusinessPermissionPageQuery, InitializingBean {

    private final ServerGroupService serverGroupService;

    private final ServerGroupTypeService serverGroupTypeService;

    private final ServerGroupPacker serverGroupPacker;

    private final UserPermissionPacker userPermissionPacker;

    private final ServerGroupTypePacker serverGroupTypePacker;

    private final UserPermissionFacade userPermissionFacade;

    private final ServerService serverService;

    private final ServerGroupingAlgorithm serverAlgorithm;

    private final ServerTreeUtil serverTreeUtil;

    @Override
    public DataTable<ServerGroupVO.ServerGroup> queryServerGroupPage(ServerGroupParam.ServerGroupPageQuery pageQuery) {
        DataTable<ServerGroup> table = serverGroupService.queryPageByParam(pageQuery);
        List<ServerGroupVO.ServerGroup> data = BeanCopierUtil.copyListProperties(table.getData(), ServerGroupVO.ServerGroup.class)
                .stream()
                .peek(e -> serverGroupPacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public DataTable<ApplicationResourceVO.Resource> queryResourcePage(ApplicationResourceParam.ResourcePageQuery pageQuery) {
        ServerGroupParam.ServerGroupPageQuery query = ServerGroupParam.ServerGroupPageQuery.builder()
                .name(pageQuery.getQueryName())
                .build();
        query.setLength(pageQuery.getLength());
        query.setPage(pageQuery.getPage());
        DataTable<ServerGroupVO.ServerGroup> table = queryServerGroupPage(query);
        return new DataTable<>(table.getData()
                .stream()
                .map(e -> ApplicationResourceVO.Resource.builder()
                        .name(e.getName())
                        .applicationId(pageQuery.getApplicationId())
                        .resourceType(getAppResType())
                        .businessType(getBusinessType())
                        .businessId(e.getBusinessId())
                        .comment(e.getComment())
                        .build()
                ).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<UserVO.IUserPermission> queryUserBusinessPermissionPage(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        pageQuery.setBusinessType(getBusinessType());
        DataTable<ServerGroup> table = serverGroupService.queryPageByParam(pageQuery);
        List<ServerGroupVO.ServerGroup> data = BeanCopierUtil.copyListProperties(table.getData(), ServerGroupVO.ServerGroup.class)
                .stream()
                .peek(e -> serverGroupPacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        if (pageQuery.getAuthorized()) {
            data.forEach(e -> {
                e.setUserId(pageQuery.getUserId());
                userPermissionPacker.wrap(e);
            });
        }
        return new DataTable<>(Lists.newArrayList(data), table.getTotalNum());
    }

    @Override
    public void addServerGroup(ServerGroupParam.AddServerGroup addServerGroup) {
        if (serverGroupService.getByName(addServerGroup.getName()) != null) {
            throw new OCException(ErrorEnum.SERVERGROUP_NAME_ALREADY_EXIST);
        }
        serverGroupService.add(ServerGroupConverter.to(addServerGroup));
    }

    @Override
    public void updateServerGroup(ServerGroupParam.UpdateServerGroup updateServerGroup) {
        ServerGroup group = serverGroupService.getById(updateServerGroup.getId());
        // 不允许修改名称
        updateServerGroup.setName(group.getName());
        serverGroupService.update(ServerGroupConverter.to(updateServerGroup));
    }

    @TagClear
    @BusinessObjectClear
    @RevokeUserPermission
    @Override
    public void deleteServerGroupById(int id) {
        ServerGroup serverGroup = serverGroupService.getById(id);
        if (serverGroup == null) {
            return;
        }
        if (serverService.countByServerGroupId(id) > 0) {
            throw new OCException("服务器组不为空：必须删除组内服务器成员！");
        }
        serverGroupService.delete(serverGroup);
    }

    @Override
    public DataTable<ServerGroupTypeVO.ServerGroupType> queryServerGroupTypePage(ServerGroupTypeParam.ServerGroupTypePageQuery pageQuery) {
        DataTable<ServerGroupType> table = serverGroupTypeService.queryPageByParam(pageQuery);
        List<ServerGroupTypeVO.ServerGroupType> data = BeanCopierUtil.copyListProperties(table.getData(), ServerGroupTypeVO.ServerGroupType.class)
                .stream()
                .peek(e -> serverGroupTypePacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addServerGroupType(ServerGroupTypeParam.AddServerGroupType addServerGroupType) {
        serverGroupTypeService.add(BeanCopierUtil.copyProperties(addServerGroupType, ServerGroupType.class));
    }

    @Override
    public void updateServerGroupType(ServerGroupTypeParam.UpdateServerGroupType updateServerGroupType) {
        serverGroupTypeService.update(BeanCopierUtil.copyProperties(updateServerGroupType, ServerGroupType.class));
    }

    @Override
    public void deleteServerGroupTypeById(int id) {
        if (serverGroupService.countByServerGroupTypeId(id) > 0) {
            throw new OCException(ErrorEnum.SERVERGROUP_TYPE_HAS_USED);
        }
        serverGroupTypeService.deleteById(id);
    }

    @Override
    public ServerTreeVO.ServerTree queryServerTree(ServerGroupParam.UserServerTreeQuery queryParam, User user) {
        // 过滤空服务器组
        int accessLevel = userPermissionFacade.getUserAccessLevel(user.getUsername());
        queryParam.setIsAdmin(accessLevel >= AccessLevel.OPS.getLevel());
        List<ServerGroup> groups = serverGroupService.queryUserServerGroupTreeByParam(queryParam)
                .stream()
                .filter(g -> serverService.countByServerGroupId(g.getId()) != 0)
                .toList();

        List<ServerTreeVO.Tree> treeList = Lists.newArrayList();
        AtomicInteger treeSize = new AtomicInteger();
        groups.forEach(group -> {
            Map<String, List<ServerPack>> serverGroupMap = serverAlgorithm.grouping(group);
            treeList.add(serverTreeUtil.wrap(group, serverGroupMap));
            treeSize.addAndGet(serverTreeUtil.getServerGroupMapSize(serverGroupMap));
        });
        return ServerTreeVO.ServerTree.builder()
                .userId(user.getId())
                .tree(treeList)
                .size(treeSize.get())
                .build();
    }

    @Override
    public Map<String, List<Server>> queryServerGroupHostPatternByEnv(ServerGroupParam.ServerGroupEnvHostPatternQuery query) {
        ServerGroup serverGroup = serverGroupService.getByName(query.getServerGroupName());
        Map<String, List<ServerPack>> serverMap = serverAlgorithm.groupingByEnv(serverGroup, true, query.getEnvType());
        Map<String, List<Server>> resultMap = Maps.newHashMap();
        serverMap.keySet().forEach(k ->
                resultMap.put(k, serverMap.get(k).stream().map(ServerPack::getServer).collect(Collectors.toList()))
        );
        return resultMap;
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        UserBusinessPermissionFactory.register(this);
    }

}