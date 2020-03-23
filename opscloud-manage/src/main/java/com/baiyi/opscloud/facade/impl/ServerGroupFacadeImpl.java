package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.account.AccountCenter;
import com.baiyi.opscloud.builder.UserPermissionBuilder;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.decorator.ServerGroupDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroupType;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserPermission;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.domain.vo.server.OcServerAttributeVO;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupTypeVO;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupVO;
import com.baiyi.opscloud.facade.ServerAttributeFacade;
import com.baiyi.opscloud.facade.ServerGroupFacade;
import com.baiyi.opscloud.facade.UserPermissionFacade;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerGroupTypeService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.user.OcUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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


    public static final boolean ACTION_ADD = true;
    public static final boolean ACTION_UPDATE = false;

    @Override
    public DataTable<OcServerGroupVO.ServerGroup> queryServerGroupPage(ServerGroupParam.PageQuery pageQuery) {
        DataTable<OcServerGroup> table = ocServerGroupService.queryOcServerGroupByParam(pageQuery);
        List<OcServerGroupVO.ServerGroup> page = BeanCopierUtils.copyListProperties(table.getData(), OcServerGroupVO.ServerGroup.class);
        DataTable<OcServerGroupVO.ServerGroup> dataTable = new DataTable<>(page.stream().map(e -> serverGroupDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> addServerGroup(OcServerGroupVO.ServerGroup serverGroup) {
        return saveServerGroup(serverGroup, ACTION_ADD);
    }

    @Override
    public BusinessWrapper<Boolean> updateServerGroup(OcServerGroupVO.ServerGroup serverGroup) {
        return saveServerGroup(serverGroup, ACTION_UPDATE);
    }

    private BusinessWrapper<Boolean> saveServerGroup(OcServerGroupVO.ServerGroup serverGroup, boolean action) {
        OcServerGroup checkOcServerGroup = ocServerGroupService.queryOcServerGroupByName(serverGroup.getName());
        if (!RegexUtils.isServerGroupNameRule(serverGroup.getName()))
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_NAME_NON_COMPLIANCE_WITH_RULES);
        OcServerGroup ocServerGroup = BeanCopierUtils.copyProperties(serverGroup, OcServerGroup.class);
        // 对象存在 && 新增
        if (checkOcServerGroup != null && action) {
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_NAME_ALREADY_EXIST);
        }
        if (action) {
            ocServerGroupService.addOcServerGroup(ocServerGroup);
        } else {
            ocServerGroupService.updateOcServerGroup(ocServerGroup);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteServerGroupById(int id) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(id);
        if (ocServerGroup == null)
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_NOT_EXIST);
        // 判断server绑定的资源
        int count = ocServerService.countByServerGroupId(id);
        if (count == 0) {
            ocServerGroupService.deleteOcServerGroupById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_HAS_USED);
        }
    }

    @Override
    public DataTable<OcServerGroupTypeVO.ServerGroupType> queryServerGroupTypePage(ServerGroupTypeParam.PageQuery pageQuery) {
        DataTable<OcServerGroupType> table = ocServerGroupTypeService.queryOcServerGroupTypeByParam(pageQuery);
        List<OcServerGroupTypeVO.ServerGroupType> page = BeanCopierUtils.copyListProperties(table.getData(), OcServerGroupTypeVO.ServerGroupType.class);
        DataTable<OcServerGroupTypeVO.ServerGroupType> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> addServerGroupType(OcServerGroupTypeVO.ServerGroupType serverGroupType) {
        return saveServerGroupType(serverGroupType, ACTION_ADD);
    }

    @Override
    public BusinessWrapper<Boolean> updateServerGroupType(OcServerGroupTypeVO.ServerGroupType serverGroupType) {
        return saveServerGroupType(serverGroupType, ACTION_UPDATE);
    }

    private BusinessWrapper<Boolean> saveServerGroupType(OcServerGroupTypeVO.ServerGroupType serverGroupType, boolean action) {


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
    public DataTable<OcServerGroupVO.ServerGroup> queryUserIncludeServerGroupPage(ServerGroupParam.UserServerGroupPageQuery pageQuery) {
        DataTable<OcServerGroup> table = ocServerGroupService.queryUserIncludeOcServerGroupByParam(pageQuery);
        List<OcServerGroupVO.ServerGroup> page = BeanCopierUtils.copyListProperties(table.getData(), OcServerGroupVO.ServerGroup.class);
        DataTable<OcServerGroupVO.ServerGroup> dataTable = new DataTable<>(page.stream().map(e -> serverGroupDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public DataTable<OcServerGroupVO.ServerGroup> queryUserExcludeServerGroupPage(ServerGroupParam.UserServerGroupPageQuery pageQuery) {
        DataTable<OcServerGroup> table = ocServerGroupService.queryUserExcludeOcServerGroupByParam(pageQuery);
        List<OcServerGroupVO.ServerGroup> page = BeanCopierUtils.copyListProperties(table.getData(), OcServerGroupVO.ServerGroup.class);
        DataTable<OcServerGroupVO.ServerGroup> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
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
            accountCenter.grant(ocUser,ocServerGroup.getName());
            return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
        }
        return new BusinessWrapper(ErrorEnum.USER_GRANT_USERGROUP_ERROR);
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
            accountCenter.revoke(ocUser,ocServerGroup.getName());
            return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
        }
        return new BusinessWrapper(ErrorEnum.USER_REVOKE_USERGROUP_ERROR);
    }

    @Override
    public List<OcServerAttributeVO.ServerAttribute> queryServerGroupAttribute(int id) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(id);
        return serverAttributeFacade.queryServerGroupAttribute(ocServerGroup);
    }

    @Override
    public BusinessWrapper<Boolean> saveServerGroupAttribute(OcServerAttributeVO.ServerAttribute serverAttribute) {
        return serverAttributeFacade.saveServerAttribute(serverAttribute);
    }

}
