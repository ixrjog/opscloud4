package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.account.AccountCenter;
import com.baiyi.opscloud.builder.UserGroupBO;
import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.base.Ressource;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.PasswordUtils;
import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.convert.UserApiTokenConvert;
import com.baiyi.opscloud.convert.UserCredentialConvert;
import com.baiyi.opscloud.decorator.UserDecorator;
import com.baiyi.opscloud.decorator.UserGroupDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcUser;
import com.baiyi.opscloud.domain.generator.OcUserApiToken;
import com.baiyi.opscloud.domain.generator.OcUserCredential;
import com.baiyi.opscloud.domain.generator.OcUserGroup;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.user.OcUserApiTokenVO;
import com.baiyi.opscloud.domain.vo.user.OcUserCredentialVO;
import com.baiyi.opscloud.domain.vo.user.OcUserGroupVO;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;
import com.baiyi.opscloud.facade.AuthFacade;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.facade.UserPermissionFacade;
import com.baiyi.opscloud.ldap.entry.Group;
import com.baiyi.opscloud.ldap.repo.GroupRepo;
import com.baiyi.opscloud.ldap.repo.PersonRepo;
import com.baiyi.opscloud.service.user.OcUserApiTokenService;
import com.baiyi.opscloud.service.user.OcUserCredentialService;
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
    private PersonRepo personRepo;

    @Resource
    private UserGroupDecorator userGroupDecorator;

    @Resource
    private UserDecorator userDecorator;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private OcUserApiTokenService ocUserApiTokenService;

    @Resource
    private AuthFacade authFacade;

    @Resource
    private OcUserCredentialService ocUserCredentialService;

    @Override
    public DataTable<OcUserVO.User> queryUserPage(UserParam.PageQuery pageQuery) {
        DataTable<OcUser> table = ocUserService.queryOcUserByParam(pageQuery);
        return toUserPage(table, pageQuery.getExtend());
    }

    @Override
    public OcUserVO.User queryUserDetail() {
        OcUser ocUser = ocUserService.queryOcUserByUsername(SessionUtils.getUsername());
        OcUserVO.User user = BeanCopierUtils.copyProperties(ocUser, OcUserVO.User.class);
        return userDecorator.decorator(user, 1);
    }

    @Override
    public DataTable<OcUserVO.User> fuzzyQueryUserPage(UserParam.PageQuery pageQuery) {
        DataTable<OcUser> table = ocUserService.fuzzyQueryUserByParam(pageQuery);
        return toUserPage(table, pageQuery.getExtend());
    }

    @Override
    public OcUserApiTokenVO.UserApiToken applyUserApiToken(OcUserApiTokenVO.UserApiToken userApiToken) {
        if (StringUtils.isEmpty(userApiToken.getComment()))
            return userApiToken;
        if (userApiToken.getExpiredTime() == null)
            return userApiToken;
        UserApiTokenConvert.convertOcUserApiToken(userApiToken);
        OcUserApiToken ocUserApiToken = UserApiTokenConvert.convertOcUserApiToken(userApiToken);
        ocUserApiTokenService.addOcUserApiToken(ocUserApiToken);
        return BeanCopierUtils.copyProperties(ocUserApiToken, OcUserApiTokenVO.UserApiToken.class);
    }

    @Override
    public OcUserCredentialVO.UserCredential saveUserCredentia(OcUserCredentialVO.UserCredential userCredential) {
        if (userCredential.getCredentialType() == null)
            return userCredential;
        if (StringUtils.isEmpty(userCredential.getCredential()))
            return userCredential;
        OcUser ocUser = ocUserService.queryOcUserByUsername(SessionUtils.getUsername());
        userCredential.setUserId(ocUser.getId());
        userCredential.setUsername(ocUser.getUsername());
        OcUserCredential ocUserCredential = UserCredentialConvert.convertOcUserCredential(userCredential);

        OcUserCredential check = ocUserCredentialService.queryOcUserCredentialByUniqueKey(ocUserCredential);
        if (check == null) {
            ocUserCredentialService.addOcUserCredential(ocUserCredential);
        } else {
            ocUserCredential.setId(check.getId());
            ocUserCredentialService.updateOcUserCredential(ocUserCredential);
        }
        return BeanCopierUtils.copyProperties(ocUserCredential, OcUserCredentialVO.UserCredential.class);
    }

    private DataTable<OcUserVO.User> toUserPage(DataTable<OcUser> table, Integer extend) {
        List<OcUserVO.User> page = BeanCopierUtils.copyListProperties(table.getData(), OcUserVO.User.class);
        return new DataTable<>(page.stream().map(e -> userDecorator.decorator(e, extend)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public String getRandomPassword() {
        return PasswordUtils.getPW(20);
    }

    @Override
    public BusinessWrapper<Boolean> updateBaseUser(OcUserVO.User user) {
        // 公共接口需要2次鉴权
        String username = SessionUtils.getUsername();
        OcUser checkOcUser = ocUserService.queryOcUserById(user.getId());
        if (!username.equals(checkOcUser.getUsername())) {
            BusinessWrapper<Boolean> wrapper = authFacade.authenticationByResourceName(Ressource.USER_UPDATE);
            if (!wrapper.isSuccess())
                return wrapper;
        }

        OcUser ocUser = BeanCopierUtils.copyProperties(user, OcUser.class);
        String password = ""; // 用户密码原文
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
        ocUserService.updateBaseOcUser(ocUser); // 更新数据库
        if (!StringUtils.isEmpty(password))
            ocUser.setPassword(password);
        accountCenter.update(ocUser); // 更新账户中心所有实例
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<OcUserGroupVO.UserGroup> queryUserGroupPage(UserGroupParam.PageQuery pageQuery) {
        DataTable<OcUserGroup> table = ocUserGroupService.queryOcUserGroupByParam(pageQuery);
        List<OcUserGroupVO.UserGroup> page = BeanCopierUtils.copyListProperties(table.getData(), OcUserGroupVO.UserGroup.class);
        DataTable<OcUserGroupVO.UserGroup> dataTable = new DataTable<>(page.stream().map(e -> userGroupDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
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

    /**
     * 同步用户组（会同步用户成员关系）
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> syncUserGroup() {
        List<Group> groupList = groupRepo.getGroupList();
        for (Group group : groupList) {
            try {
                UserGroupBO userGroupBO = UserGroupBO.builder()
                        .name(group.getGroupName())
                        .build();
                OcUserGroupVO.UserGroup userGroup = BeanCopierUtils.copyProperties(userGroupBO, OcUserGroupVO.UserGroup.class);
                addUserGroup(userGroup);
                syncUserGroupPermission(userGroup);
            } catch (Exception e) {
            }
        }
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 同步用户（会同步用户组关系）
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> syncUser() {
        accountCenter.sync(AccountCenter.LDAP_ACCOUNT_KEY); // 同步Ldap用户数据
        List<String> usernameList = personRepo.getAllPersonNames();
        for (String username : usernameList) {
            OcUser ocUser = ocUserService.queryOcUserByUsername(username);
            if (ocUser == null) continue;
            syncUserPermission(BeanCopierUtils.copyProperties(ocUser, OcUserVO.User.class));
        }
        return BusinessWrapper.SUCCESS;
    }

    private void syncUserPermission(OcUserVO.User user) {
        // OcUser ocUser= ocUserService.queryOcUserByUsername(user.getUsername());
        List<OcUserGroupVO.UserGroup> userGroups = userDecorator.decoratorFromLdapRepo(user, 1).getUserGroups();
        userPermissionFacade.syncUserBusinessPermission(user.getId(), BusinessType.USERGROUP.getType(), userGroups.stream().map(e -> e.getId()).collect(Collectors.toList()));
    }


    private void syncUserGroupPermission(OcUserGroupVO.UserGroup userGroup) {
        OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupByName(userGroup.getName());
        userPermissionFacade.syncUserBusinessPermission(userGroupDecorator.decoratorFromLdapRepo(userGroup, 1).getUsers(), BusinessType.USERGROUP.getType(), ocUserGroup.getId());
    }

}
