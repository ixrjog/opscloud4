package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.account.AccountCenter;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.PasswordUtils;
import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.decorator.UserGroupDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcUser;
import com.baiyi.opscloud.domain.generator.OcUserGroup;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.user.OcUserGroupVO;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.ldap.entry.Group;
import com.baiyi.opscloud.ldap.repo.GroupRepo;
import com.baiyi.opscloud.service.user.OcUserGroupService;
import com.baiyi.opscloud.service.user.OcUserService;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/2/20 11:17 上午
 * @Version 1.0
 */
@Service
public class UserFacadeImpl implements UserFacade {

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcUserGroupService ocUserGroupService;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private AccountCenter accountCenter;

    @Resource
    private GroupRepo groupRepo;

    @Resource
    private UserGroupDecorator userGroupDecorator;

    @Override
    public DataTable<OcUserVO.User> queryUserPage(UserParam.PageQuery pageQuery) {
        DataTable<OcUser> table = ocUserService.queryOcUserByParam(pageQuery);
        List<OcUserVO.User> page = BeanCopierUtils.copyListProperties(table.getData(), OcUserVO.User.class);
        DataTable<OcUserVO.User> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public DataTable<OcUserVO.User> fuzzyQueryUserPage(UserParam.PageQuery pageQuery) {
        DataTable<OcUser> table = ocUserService.fuzzyQueryUserByParam(pageQuery);
        List<OcUserVO.User> page = BeanCopierUtils.copyListProperties(table.getData(), OcUserVO.User.class);
        DataTable<OcUserVO.User> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public String getRandomPassword() {
        return PasswordUtils.getRandomPW(20);
    }

    @Override
    public BusinessWrapper<Boolean> updateBaseUser(OcUserVO.User user) {
        OcUser ocUser = BeanCopierUtils.copyProperties(user, OcUser.class);
        String password; // 用户密码原文
        // 用户尝试修改密码
        if (!StringUtils.isEmpty(ocUser.getPassword())) {
            if (!RegexUtils.checkPasswordRule(ocUser.getPassword()))
                return new BusinessWrapper<>(ErrorEnum.USER_PASSWORD_NON_COMPLIANCE_WITH_RULES);
            password = ocUser.getPassword();
            // 加密
            ocUser.setPassword(stringEncryptor.encrypt(password));
        }
        // 校验手机
        if (!StringUtils.isEmpty(ocUser.getPhone())) {
            if (!RegexUtils.isPhone(ocUser.getPhone()))
                return new BusinessWrapper<>(ErrorEnum.USER_PHONE_NON_COMPLIANCE_WITH_RULES);
        }
        // 校验邮箱
        if (!StringUtils.isEmpty(ocUser.getEmail())) {
            if (!RegexUtils.isEmail(ocUser.getEmail()))
                return new BusinessWrapper<>(ErrorEnum.USER_EMAIL_NON_COMPLIANCE_WITH_RULES);
        }
        ocUserService.updateBaseOcUser(ocUser);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<OcUserGroupVO.UserGroup> queryUserGroupPage(UserGroupParam.PageQuery pageQuery) {
        DataTable<OcUserGroup> table = ocUserGroupService.queryOcUserGroupByParam(pageQuery);
        List<OcUserGroupVO.UserGroup> page = BeanCopierUtils.copyListProperties(table.getData(), OcUserGroupVO.UserGroup.class);
        DataTable<OcUserGroupVO.UserGroup> dataTable = new DataTable<>(page.stream().map(e -> userGroupDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
        //DataTable<OcUserGroupVO.UserGroup> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> addUserGroup(OcUserGroupVO.UserGroup userGroup) {
        if (!RegexUtils.isUserGroupNameRule(userGroup.getName()))
            return new BusinessWrapper(ErrorEnum.USERGROUP_NAME_NON_COMPLIANCE_WITH_RULES);
        OcUserGroup checkOcUserGroupName = ocUserGroupService.queryOcUserGroupByName(userGroup.getName());
        if (checkOcUserGroupName != null)
            return new BusinessWrapper(ErrorEnum.USERGROUP_NAME_ALREADY_EXIST);
        OcUserGroup ocUserGroup = BeanCopierUtils.copyProperties(userGroup, OcUserGroup.class);
        ocUserGroupService.addOcUserGroup(ocUserGroup);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> syncUserGroup() {
        List<Group> groupList = groupRepo.getGroupList();
        for (Group group : groupList) {
            try {
                OcUserGroupVO.UserGroup userGroup = new OcUserGroupVO.UserGroup();
                userGroup.setName(group.getGroupName());
                userGroup.setSource("ldap");
                userGroup.setWorkflow(0); // 不允许工作流申请
                userGroup.setGrpType(0);  // 默认组类型
                addUserGroup(userGroup);
            } catch (Exception e) {
            }
        }
        return BusinessWrapper.SUCCESS;
    }

}
