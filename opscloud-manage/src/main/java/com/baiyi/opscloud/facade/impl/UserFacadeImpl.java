package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.account.AccountCenter;
import com.baiyi.opscloud.account.IAccount;
import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.bo.UserGroupBO;
import com.baiyi.opscloud.builder.UserPermissionBuilder;
import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.base.CredentialType;
import com.baiyi.opscloud.common.base.Resource;
import com.baiyi.opscloud.common.util.*;
import com.baiyi.opscloud.convert.UserApiTokenConvert;
import com.baiyi.opscloud.convert.UserCredentialConvert;
import com.baiyi.opscloud.decorator.UserDecorator;
import com.baiyi.opscloud.decorator.UserGroupDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/2/20 11:17 上午
 * @Version 1.0
 */
@Service
public class UserFacadeImpl implements UserFacade {

    @javax.annotation.Resource
    private OcUserService ocUserService;

    @javax.annotation.Resource
    private OcUserGroupService ocUserGroupService;

    @javax.annotation.Resource
    private StringEncryptor stringEncryptor;

    @javax.annotation.Resource
    private AccountCenter accountCenter;

    @javax.annotation.Resource
    private GroupRepo groupRepo;

    @javax.annotation.Resource
    private PersonRepo personRepo;

    @javax.annotation.Resource
    private UserGroupDecorator userGroupDecorator;

    @javax.annotation.Resource
    private UserDecorator userDecorator;

    @javax.annotation.Resource
    private UserPermissionFacade userPermissionFacade;

    @javax.annotation.Resource
    private OcUserApiTokenService ocUserApiTokenService;

    @javax.annotation.Resource
    private AuthFacade authFacade;

    @javax.annotation.Resource
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
    public BusinessWrapper<Boolean> applyUserApiToken(OcUserApiTokenVO.UserApiToken userApiToken) {
        if (StringUtils.isEmpty(userApiToken.getComment()))
            return new BusinessWrapper(ErrorEnum.USER_APPLY_API_TOKEN_COMMENT_IS_NULL);
        if (userApiToken.getExpiredTime() == null)
            return new BusinessWrapper(ErrorEnum.USER_APPLY_API_TOKEN_EXPIRED_TIME_FORMAT_ERROR);
        UserApiTokenConvert.convertOcUserApiToken(userApiToken);
        OcUserApiToken ocUserApiToken = UserApiTokenConvert.convertOcUserApiToken(userApiToken);
        ocUserApiTokenService.addOcUserApiToken(ocUserApiToken);
        return new BusinessWrapper(BeanCopierUtils.copyProperties(ocUserApiToken, OcUserApiTokenVO.UserApiToken.class));
    }

    @Override
    public BusinessWrapper<Boolean> delUserApiToken(int id) {
        OcUserApiToken ocUserApiToken = ocUserApiTokenService.queryOcUserApiTokenById(id);
        if (!SessionUtils.getUsername().equals(ocUserApiToken.getUsername()))
            return new BusinessWrapper(ErrorEnum.AUTHENTICATION_FAILUER);
        ocUserApiTokenService.delOcUserApiTokenById(id);
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 2次鉴权
     * @param userId
     * @param resource
     * @return
     */
    private BusinessWrapper<Boolean> enhancedAuthority(int userId, String resource) {
        // 公共接口需要2次鉴权
        String username = SessionUtils.getUsername();
        OcUser checkOcUser = ocUserService.queryOcUserById(userId);
        if (!username.equals(checkOcUser.getUsername())) {
            BusinessWrapper<Boolean> wrapper = authFacade.authenticationByResourceName(resource);
            if (!wrapper.isSuccess())
                return wrapper;
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> saveUserCredentia(OcUserCredentialVO.UserCredential userCredential) {
        // 公共接口需要2次鉴权
        BusinessWrapper<Boolean> wrapper = enhancedAuthority(userCredential.getUserId(),Resource.USER_CREDENTIAL_SAVE);
        if(!wrapper.isSuccess())
            return wrapper;

        if (userCredential.getCredentialType() == null)
            return new BusinessWrapper(ErrorEnum.USER_CREDENTIAL_TYPE_ERROR);
        if (StringUtils.isEmpty(userCredential.getCredential()))
            return new BusinessWrapper(ErrorEnum.USER_CREDENTIAL_ERROR);
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
        // sshkey push
        if (userCredential.getCredentialType() == CredentialType.SSH_PUB_KEY.getType()) {
            accountCenter.pushSSHKey(ocUser);
        }
        return new BusinessWrapper(BeanCopierUtils.copyProperties(ocUserCredential, OcUserCredentialVO.UserCredential.class));
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
        BusinessWrapper<Boolean> wrapper = enhancedAuthority(user.getId(),Resource.USER_UPDATE);
        if(!wrapper.isSuccess())
            return wrapper;
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
    public BusinessWrapper<Boolean> createUser(OcUserVO.User user) {
        if (StringUtils.isEmpty(user.getUsername()))
            return new BusinessWrapper(ErrorEnum.USER_USERNAME_IS_NULL);
        if (StringUtils.isEmpty(user.getPassword()))
            return new BusinessWrapper(ErrorEnum.USER_PASSWORD_IS_NULL);
        if (StringUtils.isEmpty(user.getDisplayName()))
            return new BusinessWrapper(ErrorEnum.USER_DISPLAYNAME_IS_NULL);
        if (StringUtils.isEmpty(user.getEmail()))
            return new BusinessWrapper(ErrorEnum.USER_EMAIL_IS_NULL);
        if (!RegexUtils.isUsernameRule(user.getUsername()))
            return new BusinessWrapper(ErrorEnum.USER_USERNAME_NON_COMPLIANCE_WITH_RULES);
        if (!RegexUtils.checkPasswordRule(user.getPassword()))
            return new BusinessWrapper<>(ErrorEnum.USER_PASSWORD_NON_COMPLIANCE_WITH_RULES);
        OcUser ocUser = BeanCopierUtils.copyProperties(user, OcUser.class);
        ocUser.setIsActive(true);
        ocUser.setSource("ldap");
        ocUser.setUuid(UUIDUtils.getUUID());
        accountCenter.create(AccountCenter.LDAP_ACCOUNT_KEY, ocUser);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<OcUserGroupVO.UserGroup> queryUserGroupPage(UserGroupParam.PageQuery pageQuery) {
        DataTable<OcUserGroup> table = ocUserGroupService.queryOcUserGroupByParam(pageQuery);
        List<OcUserGroupVO.UserGroup> page = BeanCopierUtils.copyListProperties(table.getData(), OcUserGroupVO.UserGroup.class);
        DataTable<OcUserGroupVO.UserGroup> dataTable = new DataTable<>(page.stream().map(e -> userGroupDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> grantUserUserGroup(UserGroupParam.UserUserGroupPermission userUserGroupPermission) {
        OcUserPermission ocUserPermission = UserPermissionBuilder.build(userUserGroupPermission);
        BusinessWrapper<Boolean> wrapper = userPermissionFacade.addOcUserPermission(ocUserPermission);
        if (!wrapper.isSuccess())
            return wrapper;
        try {
            OcUser ocUser = ocUserService.queryOcUserById(userUserGroupPermission.getUserId());
            OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupById(userUserGroupPermission.getUserGroupId());
            IAccount iAccount = AccountFactory.getAccountByKey(AccountCenter.LDAP_ACCOUNT_KEY);
            boolean result = iAccount.grant(ocUser, ocUserGroup.getName());
            if (result)
                return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
        }
        return new BusinessWrapper(ErrorEnum.USER_GRANT_USERGROUP_ERROR);
    }

    @Override
    public BusinessWrapper<Boolean> revokeUserUserGroup(UserGroupParam.UserUserGroupPermission userUserGroupPermission) {
        OcUserPermission ocUserPermission = UserPermissionBuilder.build(userUserGroupPermission);
        BusinessWrapper<Boolean> wrapper = userPermissionFacade.delOcUserPermission(ocUserPermission);
        if (!wrapper.isSuccess())
            return wrapper;
        try {
            OcUser ocUser = ocUserService.queryOcUserById(userUserGroupPermission.getUserId());
            OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupById(userUserGroupPermission.getUserGroupId());
            IAccount iAccount = AccountFactory.getAccountByKey(AccountCenter.LDAP_ACCOUNT_KEY);
            boolean result = iAccount.revoke(ocUser, ocUserGroup.getName());
            if (result)
                return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
        }
        return new BusinessWrapper(ErrorEnum.USER_REVOKE_USERGROUP_ERROR);
    }

    @Override
    public DataTable<OcUserGroupVO.UserGroup> queryUserIncludeUserGroupPage(UserGroupParam.UserUserGroupPageQuery pageQuery) {
        DataTable<OcUserGroup> table = ocUserGroupService.queryUserIncludeOcUserGroupByParam(pageQuery);
        List<OcUserGroupVO.UserGroup> page = BeanCopierUtils.copyListProperties(table.getData(), OcUserGroupVO.UserGroup.class);
        DataTable<OcUserGroupVO.UserGroup> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public DataTable<OcUserGroupVO.UserGroup> queryUserExcludeUserGroupPage(UserGroupParam.UserUserGroupPageQuery pageQuery) {
        DataTable<OcUserGroup> table = ocUserGroupService.queryUserExcludeOcUserGroupByParam(pageQuery);
        List<OcUserGroupVO.UserGroup> page = BeanCopierUtils.copyListProperties(table.getData(), OcUserGroupVO.UserGroup.class);
        DataTable<OcUserGroupVO.UserGroup> dataTable = new DataTable<>(page, table.getTotalNum());
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
