package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.account.AccountCenter;
import com.baiyi.opscloud.account.IAccount;
import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.bo.UserGroupBO;
import com.baiyi.opscloud.builder.UserPermissionBuilder;
import com.baiyi.opscloud.common.base.*;
import com.baiyi.opscloud.common.util.*;
import com.baiyi.opscloud.convert.UserApiTokenConvert;
import com.baiyi.opscloud.convert.UserCredentialConvert;
import com.baiyi.opscloud.decorator.user.UserDecorator;
import com.baiyi.opscloud.decorator.user.UserGroupDecorator;
import com.baiyi.opscloud.dingtalk.DingtalkMsgApi;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkMsgParam;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.param.user.UserServerTreeParam;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.user.UserApiTokenVO;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.event.handler.UserRetireEventHandler;
import com.baiyi.opscloud.facade.*;
import com.baiyi.opscloud.facade.dingtalk.DingtalkUserFacade;
import com.baiyi.opscloud.ldap.entry.Group;
import com.baiyi.opscloud.ldap.repo.GroupRepo;
import com.baiyi.opscloud.ldap.repo.PersonRepo;
import com.baiyi.opscloud.service.file.OcFileTemplateService;
import com.baiyi.opscloud.service.it.OcItAssetApplyService;
import com.baiyi.opscloud.service.user.*;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.base.Global.DEV_ROLE_NAME;

/**
 * @Author baiyi
 * @Date 2020/2/20 11:17 上午
 * @Version 1.0
 */
@Slf4j
@Service
public class UserFacadeImpl implements UserFacade {

    private static final String FILE_TEMPLATE_USER_CREATE_WORKORDER_MSG = "DINGTALK_USER_CREATE_MSG";
    private static final String DINGTALK_UID = "xincheng";

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
    private UserFacade userFacade;

    @Resource
    private OcUserCredentialService ocUserCredentialService;

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @Resource
    private DingtalkUserFacade dingtalkUserFacade;

    @Resource
    private OrgFacade orgFacade;

    @Resource
    private UserRetireEventHandler userRetireEventHandler;

    @Resource
    private DingtalkMsgApi dingtalkMsgApi;

    @Resource
    private OcFileTemplateService ocFileTemplateService;

    @Resource
    private OcAccountService ocAccountService;

    @Resource
    private OcItAssetApplyService ocItAssetApplyService;

    @Resource
    private OcUserToBeRetiredService ocUserToBeRetiredService;

    @Override
    public DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery) {
        DataTable<OcUser> table = ocUserService.queryOcUserByParam(pageQuery);
        return convertTable(table, pageQuery.getExtend());
    }

    @Override
    public UserVO.User queryUserDetail() {
        return queryUserDetailByUsername(SessionUtils.getUsername());
    }

    @Override
    public UserVO.User queryUserDetailByUsername(String username) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(username);
        UserVO.User user = BeanCopierUtils.copyProperties(ocUser, UserVO.User.class);
        return userDecorator.decorator(user, 1);
    }

    @Override
    public DataTable<UserVO.User> fuzzyQueryUserPage(UserParam.UserPageQuery pageQuery) {
        DataTable<OcUser> table = ocUserService.fuzzyQueryUserByParam(pageQuery);
        return convertTable(table, pageQuery.getExtend());
    }

    @Override
    public BusinessWrapper<UserApiTokenVO.UserApiToken> applyUserApiToken(UserApiTokenVO.UserApiToken userApiToken) {
        if (StringUtils.isEmpty(userApiToken.getComment()))
            return new BusinessWrapper<>(ErrorEnum.USER_APPLY_API_TOKEN_COMMENT_IS_NULL);
        if (userApiToken.getExpiredTime() == null)
            return new BusinessWrapper<>(ErrorEnum.USER_APPLY_API_TOKEN_EXPIRED_TIME_FORMAT_ERROR);
        UserApiTokenConvert.convertOcUserApiToken(userApiToken);
        OcUserApiToken ocUserApiToken = UserApiTokenConvert.convertOcUserApiToken(userApiToken);
        ocUserApiTokenService.addOcUserApiToken(ocUserApiToken);
        return new BusinessWrapper<>(BeanCopierUtils.copyProperties(ocUserApiToken, UserApiTokenVO.UserApiToken.class));
    }

    @Override
    public BusinessWrapper<Boolean> delUserApiToken(int id) {
        OcUserApiToken ocUserApiToken = ocUserApiTokenService.queryOcUserApiTokenById(id);
        if (!SessionUtils.getUsername().equals(ocUserApiToken.getUsername()))
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        ocUserApiTokenService.delOcUserApiTokenById(id);
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 2次鉴权
     *
     * @param userId
     * @param resource
     * @return
     */
    private BusinessWrapper<Boolean> enhancedAuthority(int userId, String resource) {
        // 公共接口需要2次鉴权
        OcUser checkOcUser = getOcUserBySession();
        OcUser ocUser = ocUserService.queryOcUserById(userId);
        // 运维可以修改
        int userAccessLevel = userPermissionFacade.getUserAccessLevel(userFacade.getOcUserBySession());
        if (userAccessLevel >= AccessLevel.OPS.getLevel())
            return BusinessWrapper.SUCCESS;
        if (!ocUser.getUsername().equals(checkOcUser.getUsername())) {
            BusinessWrapper<Boolean> wrapper = authFacade.authenticationByResourceName(resource);
            if (!wrapper.isSuccess())
                return wrapper;
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<UserCredentialVO.UserCredential> saveUserCredentia(UserCredentialVO.UserCredential userCredential) {
        // 公共接口需要2次鉴权
        BusinessWrapper wrapper = enhancedAuthority(userCredential.getUserId(), URLResource.USER_CREDENTIAL_SAVE);
        if (!wrapper.isSuccess())
            return wrapper;
        if (userCredential.getCredentialType() == null)
            return new BusinessWrapper<>(ErrorEnum.USER_CREDENTIAL_TYPE_ERROR);
        if (StringUtils.isEmpty(userCredential.getCredential()))
            return new BusinessWrapper<>(ErrorEnum.USER_CREDENTIAL_ERROR);
        OcUser ocUser = ocUserService.queryOcUserById(userCredential.getUserId());
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
        if (!StringUtils.isEmpty(ocUser.getPassword()) && userCredential.getCredentialType() == CredentialType.SSH_PUB_KEY.getType())
            accountCenter.pushSSHKey(ocUser);
        return new BusinessWrapper<>(BeanCopierUtils.copyProperties(ocUserCredential, UserCredentialVO.UserCredential.class));
    }

    private DataTable<UserVO.User> convertTable(DataTable<OcUser> table, Integer extend) {
        List<UserVO.User> page = BeanCopierUtils.copyListProperties(table.getData(), UserVO.User.class);
        return new DataTable<>(page.stream().map(e -> userDecorator.decorator(e, extend)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public String getRandomPassword() {
        return PasswordUtils.getPW(20);
    }

    @Override
    public BusinessWrapper<Boolean> updateBaseUser(UserParam.UpdateUser updateUser) {
        BusinessWrapper<Boolean> wrapper = checkUpdateUser(updateUser);
        if (!wrapper.isSuccess()) return wrapper;

        OcUser preUser = BeanCopierUtils.copyProperties(updateUser, OcUser.class);
        String password = ""; // 用户密码原文
        // 用户尝试修改密码
        if (!StringUtils.isEmpty(preUser.getPassword())) {
            try {
                RegexUtils.checkPasswordRule(preUser.getPassword());
            } catch (RuntimeException e) {
                return new BusinessWrapper<>(11000, e.getMessage());
            }
            password = preUser.getPassword();
            // 加密
            preUser.setPassword(stringEncryptor.encrypt(password));
        }
        // 校验手机
        if (!StringUtils.isEmpty(preUser.getPhone())) {
            if (!RegexUtils.isPhone(preUser.getPhone()))
                return new BusinessWrapper<>(ErrorEnum.USER_PHONE_NON_COMPLIANCE_WITH_RULES);
        }
        // 校验邮箱
        if (!StringUtils.isEmpty(preUser.getEmail())) {
            if (!RegexUtils.isEmail(preUser.getEmail()))
                return new BusinessWrapper<>(ErrorEnum.USER_EMAIL_NON_COMPLIANCE_WITH_RULES);
        }
        ocUserService.updateBaseOcUser(preUser); // 更新数据库
        preUser = ocUserService.queryOcUserByUsername(preUser.getUsername());
        preUser.setPassword(password);
        accountCenter.update(preUser); // 更新账户中心所有实例
        if (!CollectionUtils.isEmpty(updateUser.getDeptIdList()))
            joinDept(updateUser.getUsername(), updateUser.getDeptIdList());
        return BusinessWrapper.SUCCESS;
    }

    private BusinessWrapper<Boolean> checkUpdateUser(UserParam.UpdateUser updateUser) {
        // 查询用户是否有效
        OcUser checkUser = ocUserService.queryOcUserByUsername(updateUser.getUsername());
        if (checkUser == null)
            return new BusinessWrapper<>(ErrorEnum.USER_NOT_EXIST);
        if (!checkUser.getIsActive())
            return new BusinessWrapper<>(ErrorEnum.USER_IS_UNACTIVE);
        // 公共接口需要2次鉴权
        BusinessWrapper<Boolean> wrapper = enhancedAuthority(checkUser.getId(), URLResource.USER_UPDATE);
        if (!wrapper.isSuccess())
            return wrapper;
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> checkUsername(String username) {
        OcUser user = ocUserService.queryOcUserByUsername(username);
        if (user != null)
            return new BusinessWrapper<>(ErrorEnum.USERNAME_EXIST);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> createUser(UserParam.CreateUser createUser) {
        if (!RegexUtils.isUsernameRule(createUser.getUsername()))
            return new BusinessWrapper<>(ErrorEnum.USER_USERNAME_NON_COMPLIANCE_WITH_RULES);
        try {
            if (StringUtils.isEmpty(createUser.getPassword())) {
                createUser.setPassword(getRandomPassword());
            } else {
                RegexUtils.checkPasswordRule(createUser.getPassword());
            }
        } catch (RuntimeException e) {
            return new BusinessWrapper<>(11000, e.getMessage());
        }
        OcUser oldUser = ocUserService.queryOcUserByUsername(createUser.getUsername());
        if (oldUser != null)
            return new BusinessWrapper<>(ErrorEnum.USERNAME_EXIST);
        OcUser ocUser = BeanCopierUtils.copyProperties(createUser, OcUser.class);
        ocUser.setIsActive(true);
        ocUser.setSource("ldap");
        ocUser.setUuid(UUIDUtils.getUUID());
        accountCenter.create(AccountCenter.LDAP_ACCOUNT_KEY, ocUser);
        if (createUser.getIsRD() != null && createUser.getIsRD())
            authFacade.grantUserRole(ocUser, DEV_ROLE_NAME);
        if (!IDUtils.isEmpty(createUser.getDingtalkUserId())) {
            bindDingtalkUser(createUser.getUsername(), createUser.getDingtalkUserId());
            sendDingtalkMsg(createUser);
        }
        if (!CollectionUtils.isEmpty(createUser.getDeptIdList()))
            joinDept(createUser.getUsername(), createUser.getDeptIdList());
        return BusinessWrapper.SUCCESS;
    }

    private void sendDingtalkMsg(UserParam.CreateUser createUser) {
        OcAccount ocAccount = ocAccountService.queryOcAccount(createUser.getDingtalkUserId());
        if (ocAccount == null) return;
        UserParam.CreateUserDingTalkMsg dingtalkMsg = UserParam.CreateUserDingTalkMsg.builder()
                .displayName(createUser.getDisplayName())
                .username(createUser.getUsername())
                .password(createUser.getPassword())
                .email(createUser.getEmail())
                .build();
        Map<String, Object> contentMap = ObjectUtils.objectToMap(dingtalkMsg);
        OcFileTemplate template = ocFileTemplateService.queryOcFileTemplateByUniqueKey(FILE_TEMPLATE_USER_CREATE_WORKORDER_MSG, 0);
        try {
            DingtalkMsgParam.MarkdownMsg markdownMsg = DingtalkMsgParam.MarkdownMsg.builder()
                    .title("Hi~热烈欢迎你加入辛橙")
                    .text(BeetlUtils.renderTemplate(template.getContent(), contentMap))
                    .build();
            DingtalkMsgParam msgParam = DingtalkMsgParam.builder()
                    .msgtype(DingtalkMsgType.MARKDOWN.getType())
                    .markdown(markdownMsg)
                    .build();
            DingtalkParam.SendAsyncMsg msg = new DingtalkParam.SendAsyncMsg();
            msg.setUid(DINGTALK_UID);
            msg.setMsg(msgParam);
            msg.setUseridList(Sets.newHashSet(ocAccount.getUsername()));
            dingtalkMsgApi.sendAsyncMsg(msg);
        } catch (IOException e) {
            log.error("生成钉钉发送消息参数失败", e);
        }
    }

    private void bindDingtalkUser(String username, Integer accountId) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(username);
        if (ocUser == null)
            return;
        DingtalkParam.BindOcUser param = new DingtalkParam.BindOcUser();
        param.setAccountId(accountId);
        param.setOcUserId(ocUser.getId());
        dingtalkUserFacade.bindOcUser(param);
    }

    private void joinDept(String username, List<Integer> deptIdList) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(username);
        if (ocUser == null)
            return;
        deptIdList.forEach(deptId -> orgFacade.addDepartmentMember(deptId, ocUser.getId()));
    }

    @Override
    public DataTable<UserGroupVO.UserGroup> queryUserGroupPage(UserBusinessGroupParam.PageQuery pageQuery) {
        DataTable<OcUserGroup> table = ocUserGroupService.queryOcUserGroupByParam(pageQuery);
        List<UserGroupVO.UserGroup> page = BeanCopierUtils.copyListProperties(table.getData(), UserGroupVO.UserGroup.class);
        return new DataTable<>(page.stream().map(e -> userGroupDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> grantUserUserGroup(UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission) {
        OcUserPermission ocUserPermission = UserPermissionBuilder.build(userUserGroupPermission);
        BusinessWrapper<Boolean> wrapper = userPermissionFacade.addOcUserPermission(ocUserPermission);
        if (!wrapper.isSuccess())
            return wrapper;
        try {
            OcUser ocUser = ocUserService.queryOcUserById(userUserGroupPermission.getUserId());
            OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupById(userUserGroupPermission.getUserGroupId());
            IAccount iAccount = AccountFactory.getAccountByKey(AccountCenter.LDAP_ACCOUNT_KEY);
            return iAccount.grant(ocUser, ocUserGroup.getName());
        } catch (Exception ignored) {
        }
        return new BusinessWrapper<>(ErrorEnum.USER_GRANT_USERGROUP_ERROR);
    }

    @Override
    public BusinessWrapper<Boolean> revokeUserUserGroup(UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission) {
        OcUserPermission ocUserPermission = UserPermissionBuilder.build(userUserGroupPermission);
        BusinessWrapper<Boolean> wrapper = userPermissionFacade.delOcUserPermission(ocUserPermission);
        if (!wrapper.isSuccess())
            return wrapper;
        try {
            OcUser ocUser = ocUserService.queryOcUserById(userUserGroupPermission.getUserId());
            OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupById(userUserGroupPermission.getUserGroupId());
            IAccount iAccount = AccountFactory.getAccountByKey(AccountCenter.LDAP_ACCOUNT_KEY);
            return iAccount.revoke(ocUser, ocUserGroup.getName());
        } catch (Exception ignored) {
        }
        return new BusinessWrapper<>(ErrorEnum.USER_REVOKE_USERGROUP_ERROR);
    }

    @Override
    public DataTable<UserGroupVO.UserGroup> queryUserIncludeUserGroupPage(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery) {
        DataTable<OcUserGroup> table = ocUserGroupService.queryUserIncludeOcUserGroupByParam(pageQuery);
        List<UserGroupVO.UserGroup> page = BeanCopierUtils.copyListProperties(table.getData(), UserGroupVO.UserGroup.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public DataTable<UserGroupVO.UserGroup> queryUserExcludeUserGroupPage(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery) {
        DataTable<OcUserGroup> table = ocUserGroupService.queryUserExcludeOcUserGroupByParam(pageQuery);
        List<UserGroupVO.UserGroup> page = BeanCopierUtils.copyListProperties(table.getData(), UserGroupVO.UserGroup.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addUserGroup(UserGroupVO.UserGroup userGroup) {
        if (!RegexUtils.isUserGroupNameRule(userGroup.getName()))
            return new BusinessWrapper<>(ErrorEnum.USERGROUP_NAME_NON_COMPLIANCE_WITH_RULES);
        OcUserGroup checkOcUserGroupName = ocUserGroupService.queryOcUserGroupByName(userGroup.getName());
        if (checkOcUserGroupName != null)
            return new BusinessWrapper<>(ErrorEnum.USERGROUP_NAME_ALREADY_EXIST);
        if (StringUtils.isEmpty(userGroup.getSource()))
            userGroup.setSource("ldap");
        OcUserGroup ocUserGroup = BeanCopierUtils.copyProperties(userGroup, OcUserGroup.class);
        ocUserGroupService.addOcUserGroup(ocUserGroup);
        groupRepo.create(ocUserGroup.getName());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> delUserGroupById(int id) {
        OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupById(id);
        if (ocUserGroup == null) return BusinessWrapper.SUCCESS;
        UserGroupVO.UserGroup group = userGroupDecorator.decorator(BeanCopierUtils.copyProperties(ocUserGroup, UserGroupVO.UserGroup.class), 1);
        if (!CollectionUtils.isEmpty(group.getUsers()))
            return new BusinessWrapper<>(ErrorEnum.USERGROUP_MEMBER_NOT_DELETED);
        BusinessWrapper<Boolean> wrapper = groupRepo.delete(ocUserGroup.getName());
        if (!wrapper.isSuccess()) return wrapper;
        ocUserGroupService.deleteOcUserGroupById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateUserGroup(UserGroupVO.UserGroup userGroup) {
        OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupByName(userGroup.getName());
        ocUserGroup.setInWorkorder(userGroup.getInWorkorder());
        ocUserGroup.setComment(userGroup.getComment());
        ocUserGroupService.updateOcUserGroup(ocUserGroup);
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
        filterGroup(groupList).forEach(g -> {
            try {
                UserGroupBO userGroupBO = UserGroupBO.builder()
                        .name(g.getGroupName())
                        .build();
                UserGroupVO.UserGroup userGroup = BeanCopierUtils.copyProperties(userGroupBO, UserGroupVO.UserGroup.class);
                addUserGroup(userGroup);
                syncUserGroupPermission(userGroup);
            } catch (Exception ignored) {
            }
        });
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 过滤
     *
     * @param groupList
     * @return
     */
    private List<Group> filterGroup(List<Group> groupList) {
        return groupList.stream().filter(g -> !g.getGroupName().startsWith("bamboo-")).collect(Collectors.toList());
    }

    /**
     * 同步用户（会同步用户组关系）
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> syncUser() {
        accountCenter.sync(AccountCenter.LDAP_ACCOUNT_KEY); // 同步Ldap用户数据
        personRepo.getAllPersonNames().forEach(e -> {
            OcUser ocUser = ocUserService.queryOcUserByUsername(e);
            if (ocUser != null)
                syncUserPermission(BeanCopierUtils.copyProperties(ocUser, UserVO.User.class));
        });
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public ServerTreeVO.MyServerTree queryUserServerTree(UserServerTreeParam.UserServerTreeQuery userServerTreeQuery) {
        OcUser ocUser = userFacade.getOcUserBySession();
        userServerTreeQuery.setUserId(ocUser.getId());
        return serverGroupFacade.queryUserServerTree(userServerTreeQuery, ocUser);
    }

    @Override
    public OcUser getOcUserBySession() {
        String username = SessionUtils.getUsername();
        if (StringUtils.isEmpty(username))
            return null;
        return ocUserService.queryOcUserByUsername(username);
    }

    @Override
    public BusinessWrapper<Boolean> retireUser(int userId) {
        List<OcItAssetApply> assetList = ocItAssetApplyService.queryMyAsset(userId);
        if (!CollectionUtils.isEmpty(assetList))
            return new BusinessWrapper<>(ErrorEnum.USER_HAVE_ASSET);
        OcUser ocUser = ocUserService.queryOcUserById(userId);
        // 禁用用户
        BusinessWrapper<Boolean> result = accountCenter.active(ocUser, false);
        if (result.isSuccess()) {
            userRetireEventHandler.eventPost(ocUser);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.USER_RESIGNATION_ERROR);
    }

    @Override
    public BusinessWrapper<Boolean> beReinstatedUser(int userId) {
        OcUser ocUser = ocUserService.queryOcUserById(userId);
        // 禁用用户
        BusinessWrapper<Boolean> result = accountCenter.active(ocUser, true);
        if (result.isSuccess()) {
            ocUser.setIsActive(true);
            ocUser.setLastLogin(null);
            ocUserService.updateOcUser(ocUser);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.USER_RESIGNATION_ERROR);
    }

    private void syncUserPermission(UserVO.User user) {
        List<UserGroupVO.UserGroup> userGroups = userDecorator.decoratorFromLdapRepo(user, 1).getUserGroups();
        userPermissionFacade.syncUserBusinessPermission(user.getId(), BusinessType.USERGROUP.getType(), userGroups.stream().map(UserGroupVO.UserGroup::getId).collect(Collectors.toList()));
    }

    private void syncUserGroupPermission(UserGroupVO.UserGroup userGroup) {
        OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupByName(userGroup.getName());
        userPermissionFacade.syncUserBusinessPermission(userGroupDecorator.decoratorFromLdapRepo(userGroup, 1).getUsers(), BusinessType.USERGROUP.getType(), ocUserGroup.getId());
    }

    @Override
    public BusinessWrapper<List<UserVO.User>> queryUserToBeRetired() {
        List<OcUserToBeRetired> toBeRetiredList = ocUserToBeRetiredService.queryOcUserToBeRetiredAll();
        if (CollectionUtils.isEmpty(toBeRetiredList))
            return new BusinessWrapper<>(Collections.emptyList());
        List<Integer> userIdList = toBeRetiredList.stream().map(OcUserToBeRetired::getUserId).collect(Collectors.toList());
        List<OcUser> ocUserList = ocUserService.queryOcUserByIdList(userIdList);
        List<UserVO.User> userList = BeanCopierUtils.copyListProperties(ocUserList, UserVO.User.class);
        List<UserVO.User> list = userList.stream().map(e -> userDecorator.decorator(e, 1)).collect(Collectors.toList());
        return new BusinessWrapper<>(list);
    }
}
