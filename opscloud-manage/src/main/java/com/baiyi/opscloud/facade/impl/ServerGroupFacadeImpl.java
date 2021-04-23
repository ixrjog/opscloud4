package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.account.AccountCenter;
import com.baiyi.opscloud.builder.UserPermissionBuilder;
import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.RedisKeyUtils;
import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.decorator.server.ServerGroupDecorator;
import com.baiyi.opscloud.decorator.server.ServerTreeDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.domain.param.server.SeverGroupPropertyParam;
import com.baiyi.opscloud.domain.param.user.UserServerTreeParam;
import com.baiyi.opscloud.domain.vo.server.*;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.facade.ServerBaseFacade;
import com.baiyi.opscloud.facade.ServerCacheFacade;
import com.baiyi.opscloud.facade.ServerGroupFacade;
import com.baiyi.opscloud.facade.UserPermissionFacade;
import com.baiyi.opscloud.factory.attribute.impl.AnsibleAttribute;
import com.baiyi.opscloud.server.facade.ServerAttributeFacade;
import com.baiyi.opscloud.service.server.OcServerGroupPropertyService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerGroupTypeService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/2/21 11:00 上午
 * @Version 1.0
 */
@Service
public class ServerGroupFacadeImpl implements ServerGroupFacade {

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcServerGroupTypeService ocServerGroupTypeService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private ServerGroupDecorator serverGroupDecorator;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private AccountCenter accountCenter;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

    @Resource
    private AnsibleAttribute ansibleAttribute;

    @Resource
    private OcServerGroupPropertyService ocServerGroupPropertyService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ServerTreeDecorator serverTreeDecorator;

    @Resource
    private ServerCacheFacade serverCacheFacade;

    public static final boolean ACTION_ADD = true;
    public static final boolean ACTION_UPDATE = false;

    @Override
    public DataTable<ServerGroupVO.ServerGroup> queryServerGroupPage(ServerGroupParam.PageQuery pageQuery) {
        DataTable<OcServerGroup> table = ocServerGroupService.queryOcServerGroupByParam(pageQuery);
        List<ServerGroupVO.ServerGroup> page = BeanCopierUtils.copyListProperties(table.getData(), ServerGroupVO.ServerGroup.class);
        return new DataTable<>(page.stream().map(e -> serverGroupDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<ServerGroupVO.ServerGroup> queryServerGroupById(int id) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(id);
        return new BusinessWrapper<>(BeanCopierUtils.copyProperties(ocServerGroup, ServerGroupVO.ServerGroup.class));
    }

    @Override
    public BusinessWrapper<Boolean> addServerGroup(ServerGroupVO.ServerGroup serverGroup) {
        return saveServerGroup(serverGroup, ACTION_ADD);
    }

    @Override
    public BusinessWrapper<Boolean> updateServerGroup(ServerGroupVO.ServerGroup serverGroup) {
        return saveServerGroup(serverGroup, ACTION_UPDATE);
    }

    private BusinessWrapper<Boolean> saveServerGroup(ServerGroupVO.ServerGroup serverGroup, boolean action) {
        if (!RegexUtils.isServerGroupNameRule(serverGroup.getName()))
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_NAME_NON_COMPLIANCE_WITH_RULES);
        OcServerGroup ocServerGroup = BeanCopierUtils.copyProperties(serverGroup, OcServerGroup.class);
        // 对象存在 && 新增
        if (ocServerGroupService.queryOcServerGroupByName(serverGroup.getName()) != null && action) {
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_NAME_ALREADY_EXIST);
        }
        if (action) {
            ocServerGroupService.addOcServerGroup(ocServerGroup);
        } else {
            ocServerGroupService.updateOcServerGroup(ocServerGroup);
        }
        serverCacheFacade.evictServerGroupCache(ocServerGroup);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteServerGroupById(int id) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(id);
        if (ocServerGroup == null)
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_NOT_EXIST);
        // 判断server绑定的资源
        if (ocServerService.countByServerGroupId(id) == 0) {
            // 清理缓存
            serverCacheFacade.evictServerGroupCache(ocServerGroup);
            ocServerGroupService.deleteOcServerGroupById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_HAS_USED);
        }
    }

    @Override
    public DataTable<ServerGroupTypeVO.ServerGroupType> queryServerGroupTypePage(ServerGroupTypeParam.PageQuery pageQuery) {
        DataTable<OcServerGroupType> table = ocServerGroupTypeService.queryOcServerGroupTypeByParam(pageQuery);
        List<ServerGroupTypeVO.ServerGroupType> page = BeanCopierUtils.copyListProperties(table.getData(), ServerGroupTypeVO.ServerGroupType.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addServerGroupType(ServerGroupTypeVO.ServerGroupType serverGroupType) {
        return saveServerGroupType(serverGroupType, ACTION_ADD);
    }

    @Override
    public BusinessWrapper<Boolean> updateServerGroupType(ServerGroupTypeVO.ServerGroupType serverGroupType) {
        return saveServerGroupType(serverGroupType, ACTION_UPDATE);
    }

    private BusinessWrapper<Boolean> saveServerGroupType(ServerGroupTypeVO.ServerGroupType serverGroupType, boolean action) {
        OcServerGroupType checkOcServerGroupTypeName = ocServerGroupTypeService.queryOcServerGroupTypeByName(serverGroupType.getName());
        OcServerGroupType ocServerGroupType = BeanCopierUtils.copyProperties(serverGroupType, OcServerGroupType.class);
        // 对象存在 && 新增
        if (checkOcServerGroupTypeName != null && action) {
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_TYPE_NAME_ALREADY_EXIST);
        }
        if (action) {
            ocServerGroupTypeService.addOcServerGroupType(ocServerGroupType);
        } else {
            ocServerGroupTypeService.updateOcServerGroupType(ocServerGroupType);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteServerGroupTypeById(int id) {
        OcServerGroupType ocServerGroupType = ocServerGroupTypeService.queryOcServerGroupTypeById(id);
        if (ocServerGroupType == null)
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_TYPE_NOT_EXIST);
        // 判断默认值
        if (ocServerGroupType.getGrpType() == 0)
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_TYPE_IS_DEFAULT);
        // 判断server绑定的资源
        int count = ocServerGroupService.countByGrpType(ocServerGroupType.getGrpType());
        if (count == 0) {
            ocServerGroupTypeService.deleteOcServerGroupTypeById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_TYPE_HAS_USED);
        }
    }

    @Override
    public DataTable<ServerGroupVO.ServerGroup> queryUserIncludeServerGroupPage(ServerGroupParam.UserServerGroupPageQuery pageQuery) {
        DataTable<OcServerGroup> table = ocServerGroupService.queryUserIncludeOcServerGroupByParam(pageQuery);
        List<ServerGroupVO.ServerGroup> page = BeanCopierUtils.copyListProperties(table.getData(), ServerGroupVO.ServerGroup.class);
        return new DataTable<>(page.stream().map(e -> serverGroupDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<ServerGroupVO.ServerGroup> queryUserExcludeServerGroupPage(ServerGroupParam.UserServerGroupPageQuery pageQuery) {
        DataTable<OcServerGroup> table = ocServerGroupService.queryUserExcludeOcServerGroupByParam(pageQuery);
        List<ServerGroupVO.ServerGroup> page = BeanCopierUtils.copyListProperties(table.getData(), ServerGroupVO.ServerGroup.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> grantUserServerGroup(ServerGroupParam.UserServerGroupPermission userServerGroupPermission) {
        OcUserPermission ocUserPermission = UserPermissionBuilder.build(userServerGroupPermission);
        BusinessWrapper<Boolean> wrapper = userPermissionFacade.addOcUserPermission(ocUserPermission);
        if (!wrapper.isSuccess())
            return wrapper;
        try {
            OcUser ocUser = ocUserService.queryOcUserById(ocUserPermission.getUserId());
            OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(ocUserPermission.getBusinessId());
            accountCenter.grant(ocUser, ocServerGroup.getName());
            return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
        }
        return new BusinessWrapper(ErrorEnum.USER_GRANT_SERVERGROUP_ERROR);
    }

    @Override
    public BusinessWrapper<Boolean> revokeUserServerGroup(ServerGroupParam.UserServerGroupPermission userServerGroupPermission) {
        OcUserPermission ocUserPermission = UserPermissionBuilder.build(userServerGroupPermission);
        BusinessWrapper<Boolean> wrapper = userPermissionFacade.delOcUserPermission(ocUserPermission);
        if (!wrapper.isSuccess())
            return wrapper;
        try {
            OcUser ocUser = ocUserService.queryOcUserById(ocUserPermission.getUserId());
            OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(ocUserPermission.getBusinessId());
            accountCenter.revoke(ocUser, ocServerGroup.getName());
            return BusinessWrapper.SUCCESS;
        } catch (Exception ignored) {
        }
        return new BusinessWrapper(ErrorEnum.USER_REVOKE_SERVERGROUP_ERROR);
    }

    @Override
    public BusinessWrapper<List<ServerAttributeVO.ServerAttribute>> queryServerGroupAttribute(int id) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(id);
        return new BusinessWrapper(serverAttributeFacade.queryServerGroupAttribute(ocServerGroup));
    }

    @Override
    public BusinessWrapper<Map<Integer, Map<String, String>>> queryServerGroupPropertyMap(int id) {
        Map<Integer, Map<String, String>> propertyEnvMap = Maps.newHashMap();
        List<OcServerGroupProperty> serverGroupProperties = ocServerGroupPropertyService.queryOcServerGroupPropertyByServerGroupId(id);
        if (!CollectionUtils.isEmpty(serverGroupProperties))
            serverGroupProperties.forEach(e -> invokePropertyEnvMap(e, propertyEnvMap));
        return new BusinessWrapper(propertyEnvMap);
    }

    @Override
    public BusinessWrapper<Map<Integer, Map<String, String>>> queryServerGroupPropertyMap(SeverGroupPropertyParam.PropertyParam propertyParam) {
        Map<Integer, Map<String, String>> propertyEnvMap = Maps.newHashMap();
        if (propertyParam.getPropertyNameSet() == null) return new BusinessWrapper(propertyEnvMap);
        propertyParam.getPropertyNameSet().forEach(k -> {
            List<OcServerGroupProperty> properties = ocServerGroupPropertyService.queryOcServerGroupPropertyByServerGroupIdAndEnvTypeAnd(propertyParam.getServerGroupId(), propertyParam.getEnvType(), k);
            if (!CollectionUtils.isEmpty(properties))
                properties.forEach(e -> invokePropertyEnvMap(e, propertyEnvMap));
        });
        return new BusinessWrapper(propertyEnvMap);
    }

    private void invokePropertyEnvMap(OcServerGroupProperty e, Map<Integer, Map<String, String>> propertyEnvMap) {
        Map<String, String> propertyMap = propertyEnvMap.get(e.getEnvType());
        if (propertyMap == null)
            propertyMap = Maps.newHashMap();
        propertyMap.put(e.getPropertyName(), e.getPropertyValue());
        propertyEnvMap.put(e.getEnvType(), propertyMap);
    }

    @Override
    public BusinessWrapper<Boolean> saveServerGroupProperty(ServerGroupPropertyVO.ServerGroupProperty serverGroupProperty) {
        if (CollectionUtils.isEmpty(serverGroupProperty.getProperty()))
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_PROPERTY_KV_EMPTY);
        if (ocServerGroupService.queryOcServerGroupById(serverGroupProperty.getServerGroupId()) == null)
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_NOT_EXIST);
        serverGroupProperty.getProperty().forEach((key, value) -> {
            OcServerGroupProperty property = new OcServerGroupProperty();
            property.setServerGroupId(serverGroupProperty.getServerGroupId());
            property.setEnvType(serverGroupProperty.getEnvType());
            property.setPropertyName(key);
            property.setPropertyValue(value);
            saveServerGroupProperty(property);
        });
        return BusinessWrapper.SUCCESS;
    }

    private void saveServerGroupProperty(OcServerGroupProperty property) {
        OcServerGroupProperty ocServerGroupProperty = ocServerGroupPropertyService.queryOcServerGroupPropertyByUniqueKey(property);
        if (ocServerGroupProperty == null) {
            ocServerGroupPropertyService.addOcServerGroupProperty(property);
        } else {
            property.setId(ocServerGroupProperty.getId());
            ocServerGroupPropertyService.updateOcServerGroupProperty(property);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delServerGroupPropertyById(int id) {
        ocServerGroupPropertyService.deleteOcServerGroupPropertyById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> saveServerGroupAttribute(ServerAttributeVO.ServerAttribute serverAttribute) {
        return serverAttributeFacade.saveServerAttribute(serverAttribute);
    }

    @Override
    public ServerTreeVO.MyServerTree queryUserServerTree(UserServerTreeParam.UserServerTreeQuery userServerTreeQuery, OcUser ocUser) {
        // 过滤空服务器组
        int accessLevel = userPermissionFacade.getUserAccessLevel(ocUser);
        if (accessLevel >= AccessLevel.OPS.getLevel())
            userServerTreeQuery.setUserId(0);
        List<OcServerGroup> serverGroupList
                = ocServerGroupService.queryUserPermissionOcServerGroupByParam(userServerTreeQuery).stream()
                .filter(g -> ocServerService.countByServerGroupId(g.getId()) != 0).collect(Collectors.toList());
        // 缓存
        Map<String, String> serverTreeHostPatternMap = Maps.newHashMap();
        List<TreeVO.Tree> treeList = Lists.newArrayList();
        AtomicInteger treeSize = new AtomicInteger();

        serverGroupList.forEach(e -> {
            Map<String, List<OcServer>> serverGroupMap = ansibleAttribute.grouping(e);
            treeSize.addAndGet(getServerGroupMapSize(serverGroupMap));
            // 组装缓存
            assembleServerTreeHostPatternMap(serverTreeHostPatternMap, serverGroupMap);
            treeList.add(serverTreeDecorator.decorator(e, serverGroupMap));
        });
        ServerTreeVO.MyServerTree myServerTree = ServerTreeVO.MyServerTree.builder()
                .userId(ocUser.getId())
                .uuid(UUIDUtils.getUUID())
                .tree(treeList)
                .size(treeSize.get())
                .build();
        // 缓存1小时
        String key = RedisKeyUtils.getMyServerTreeKey(ocUser.getId(), myServerTree.getUuid());
        redisUtil.set(key, serverTreeHostPatternMap, 3600);
        return myServerTree;
    }

    private void assembleServerTreeHostPatternMap(Map<String, String> serverTreeHostPatternMap, Map<String, List<OcServer>> serverGroupMap) {
        serverGroupMap.keySet().forEach(k ->
                serverGroupMap.get(k).forEach(s -> serverTreeHostPatternMap.put(ServerBaseFacade.acqServerName(s), serverAttributeFacade.getManageIp(s)))
        );
    }

    private int getServerGroupMapSize(Map<String, List<OcServer>> serverGroupMap) {
        int size = 0;
        if (serverGroupMap.isEmpty())
            return size;
        for (String key : serverGroupMap.keySet())
            size += serverGroupMap.get(key).size();
        return size;
    }

    @Override
    public BusinessWrapper<Map<String, String>> getServerTreeHostPatternMap(String uuid, OcUser ocUser) {
        String key = RedisKeyUtils.getMyServerTreeKey(ocUser.getId(), uuid);
        if (!redisUtil.hasKey(key))
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_TREE_NOT_EXIST);
        return new BusinessWrapper(redisUtil.get(key));
    }

    @Override
    public BusinessWrapper<Map<String, List<OcServer>>> queryServerGroupHostPattern(ServerGroupParam.ServerGroupHostPatternQuery query) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupByName(query.getServerGroupName());
        Map<String, List<OcServer>> map = ansibleAttribute.grouping(ocServerGroup, true);
        return new BusinessWrapper<>(map);
    }

    @Override
    public BusinessWrapper<Map<String, List<OcServer>>> queryServerGroupEnvHostPattern(ServerGroupParam.ServerGroupEnvHostPatternQuery query) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupByName(query.getServerGroupName());
        Map<String, List<OcServer>> map = ansibleAttribute.grouping(ocServerGroup, true, query.getEnvType());
        return new BusinessWrapper<>(map);
    }

}
