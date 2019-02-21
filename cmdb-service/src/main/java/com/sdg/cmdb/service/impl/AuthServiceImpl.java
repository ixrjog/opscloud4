package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.AuthDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.*;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.extend.InvokeInvocation;
import com.sdg.cmdb.extend.InvokeResult;
import com.sdg.cmdb.login.LoginPluginManager;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by zxxiao on 16/9/18.
 */
@Service("authService")
public class AuthServiceImpl implements AuthService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");

    @Resource
    private ConfigCenterService configCenterService;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    @Value("#{cmdb['admin.passwd']}")
    private String adminPasswd;

    @Resource
    private LoginPluginManager loginPluginManager;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private ServerGroupService serverGroupService;

    private Map<String, List<String>> userGroupMap = new HashMap<>();


    @Resource
    private AuthDao authDao;

    @Resource
    private UserDao userDao;


    @Resource
    private UserService userService;

    @Resource
    private KeyBoxService keyBoxService;


    @Resource
    private ConfigService configService;


    @Resource
    private LdapService ldapService;



    @Override
    public BusinessWrapper<UserDO> loginCredentialCheck(String username, String password) {
        logger.info("user :" + username + " try login!");
        if (org.springframework.util.StringUtils.isEmpty(username))
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        if (org.springframework.util.StringUtils.isEmpty(password))
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        try {
            Map<String, Object> loginContent = new HashMap<>();
            loginContent.put("username", username);
            loginContent.put("password", password);
            InvokeResult result = (InvokeResult) loginPluginManager.doInvoke(new InvokeInvocation(loginContent));
            // 认证账户
            if (!result.isSuccess() && username.equals("admin") && !StringUtils.isEmpty(adminPasswd) && adminPasswd.equals(password))
                result.setSuccess(true);

            if (result.isSuccess()) {
                UserDO userDO = ldapService.getUserByName(username);
                setUserInit(username, userDO);
                userDO.setResourceDOList(authDao.getUserAuthorizedResource(username, ResourceGroupDO.menu));
                if (!checkUserHasResourceAuthorize(userDO.getToken(), Permissions.lookAllServerGroup).isSuccess()) {
                    setUserGroup(userDO.getUsername());
                }
                //更新用户基本信息
                userDao.saveUserInfo(userDO);
                logger.info("user :" + username + " login success!");
                return new BusinessWrapper<>(userDO);
            } else {
                logger.info("user :" + username + " login failure!");
                return new BusinessWrapper<>(ErrorCode.credentialError);

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("user :" + username + " login failure!");
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public HttpResult apiLoginCheck(UserDO userDO) {
        logger.info("user :" + userDO.getUsername() + " try API Login!");
        HttpResult result = new HttpResult("");
        result.setSuccess(false);
        if (org.springframework.util.StringUtils.isEmpty(userDO.getUsername())) {
            result.setBody(ErrorCode.serverFailure);
            return result;
        }
        if (org.springframework.util.StringUtils.isEmpty(userDO.getPwd())) {
            result.setBody(ErrorCode.serverFailure);
            return result;
        }

        try {
            Map<String, Object> loginContent = new HashMap<>();
            loginContent.put("username", userDO.getUsername());
            loginContent.put("password", userDO.getPwd());
            InvokeResult invokeResult = (InvokeResult) loginPluginManager.doInvoke(new InvokeInvocation(loginContent));

            if (invokeResult.isSuccess()) {
                UserDO user = ldapService.getUserByName(userDO.getUsername());

                UserVO userVO = new UserVO();
                userVO.setUsername(userDO.getUsername());
                userVO.setDisplayName(user.getDisplayName());
                userVO.setMail(user.getMail());
                userVO.setMobile(user.getMobile());
                userVO.setLoginResult("succeed");
                result.setSuccess(true);
                result.setBody(userVO);
                return result;
            } else {
                //logger.info("user :" + username + " login failure!");
                UserVO userVO = new UserVO();
                userVO.setUsername(userDO.getUsername());
                userVO.setLoginResult("failed");

                result.setBody(userVO);

                return result;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return result;
        }
    }

    @Override
    public BusinessWrapper<Boolean> logout(String username) {
        try {
            authDao.updateUserTokenInvalid(username);

            userGroupMap.remove(username);

            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public String getUserByToken(String token) {
        return authDao.getUserByToken(token);
    }

    @Override
    public BusinessWrapper<Boolean> checkUserHasResourceAuthorize(String token, String resourceName) {
        ResourceDO resourceDO = authDao.getResourceByResourceName(resourceName);
        if (resourceDO == null) {
            return new BusinessWrapper<>(ErrorCode.resourceNotExist);
        }

        if (resourceDO.getNeedAuth() == 1) {
            return new BusinessWrapper<>(true);
        }

        if (StringUtils.isEmpty(token)) {
            return new BusinessWrapper<>(ErrorCode.requestNoToken);
        }

        //检查token是否失效
        if (authDao.checkTokenHasInvalid(token) == 0) {
            return new BusinessWrapper<>(ErrorCode.tokenInvalid);
        }

        int nums = authDao.checkUserHasResourceAuthorize(token, resourceName);
        if (nums == 0) {
            return new BusinessWrapper<>(ErrorCode.authenticationFailure);
        } else {
            return new BusinessWrapper<>(true);
        }
    }

    @Override
    public BusinessWrapper<List<ResourceDO>> checkAndGetUserHasResourceAuthorize(String token, String resourceName, List<String> authGroupList) {
        BusinessWrapper<Boolean> checkWrapper = checkUserHasResourceAuthorize(token, resourceName);
        if (checkWrapper.isSuccess()) {
            if (authGroupList.isEmpty()) {
                return new BusinessWrapper<>(Collections.EMPTY_LIST);
            }
            List<ResourceDO> doList = authDao.getUserAuthResources(token, authGroupList);
            return new BusinessWrapper<>(doList == null ? Collections.EMPTY_LIST : doList);
        } else {
            return new BusinessWrapper<>(checkWrapper.getCode(), checkWrapper.getMsg());
        }
    }

    @Override
    public TableVO<List<ResourceGroupDO>> getResourceGroups(String groupCode, int page, int length) {
        long size = authDao.getResourceGroupSize(groupCode);
        List<ResourceGroupDO> list = authDao.getResourceGroupPage(groupCode, page * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public BusinessWrapper<Boolean> saveResourceGroup(ResourceGroupDO resourceGroupDO) {
        try {
            if (resourceGroupDO.getId() == 0) {
                authDao.addResourceGroup(resourceGroupDO);
            } else {
                authDao.updateResourceGroup(resourceGroupDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delResourceGroup(long groupId) {
        int size = authDao.queryResourcesByGroupId(groupId);
        if (size <= 0) {
            authDao.delResourceGroupByGroupId(groupId);
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.resourceGroupRely);
        }
    }

    @Override
    public TableVO<List<ResourceVO>> getResources(long groupId, String resourceName, int authType, int page, int length) {
        long size = authDao.queryResourceSize(groupId, resourceName, authType);
        List<ResourceDO> list = authDao.queryResourcePage(groupId, resourceName, authType, page * length, length);

        List<ResourceVO> voList = new ArrayList<>();
        for (ResourceDO resourceDO : list) {
            List<ResourceGroupDO> groupDOList = authDao.getGroupByResourceId(resourceDO.getId());
            ResourceVO resourceVO = new ResourceVO(resourceDO, groupDOList);

            voList.add(resourceVO);
        }

        TableVO<List<ResourceVO>> tableVO = new TableVO<>(size, voList);

        return tableVO;
    }

    @Override
    public BusinessWrapper<Boolean> saveResource(ResourceVO resourceVO) {
        return transactionTemplate.execute(status -> {
            try {
                ResourceDO resourceDO = new ResourceDO(resourceVO);
                if (resourceVO.getId() == 0) {
                    authDao.addResource(resourceDO);
                } else {
                    authDao.updateResource(resourceDO);
                    authDao.delResourceGroupRelay(resourceDO.getId());
                }
                for (ResourceGroupDO groupDO : resourceVO.getGroupDOList()) {
                    authDao.addResourceGroupRelay(groupDO.getId(), resourceDO.getId());
                }
                return new BusinessWrapper<>(true);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                status.setRollbackOnly();
                return new BusinessWrapper<>(ErrorCode.serverFailure);
            }
        });
    }

    @Override
    public BusinessWrapper<Boolean> delResource(long resourceId) {
        return transactionTemplate.execute((TransactionStatus status) -> {
            try {
                if (authDao.getResourceRoleRelay(resourceId) != 0) {
                    return new BusinessWrapper<>(ErrorCode.resourceHasRelyCannotDel);
                } else {
                    authDao.delResourceGroupRelay(resourceId);
                    authDao.delResource(resourceId);
                    return new BusinessWrapper<>(true);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                status.setRollbackOnly();
                return new BusinessWrapper<>(ErrorCode.serverFailure);
            }
        });
    }

    @Override
    public RoleDO getRoleById(long roleId) {
        if (roleId == 0) {
            return null;
        }
        RoleDO roleDO = new RoleDO();
        roleDO.setId(roleId);
        return authDao.getRole(roleDO);
    }

    @Override
    public TableVO<List<RoleDO>> getRoles(long resourceId, String roleName, int page, int length) {
        long size = authDao.queryRoleSize(resourceId, roleName);
        List<RoleDO> list = authDao.queryRolePage(resourceId, roleName, page * length, length);
        return new TableVO<>(size, list);
    }


    @Override
    public boolean isRole(String username, String roleName) {
        List<Long> roleIds = getUserRoleIds(username);
        RoleDO roleDO = authDao.getRole(new RoleDO(roleName));
        for (Long id : roleIds)
            if (id == roleDO.getId())
                return true;
        return false;
    }

    @Override
    public BusinessWrapper<Boolean> saveRole(RoleDO roleDO) {
        try {
            if (roleDO.getId() == 0) {
                authDao.addRole(roleDO);
            } else {
                authDao.updateRole(roleDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delRole(long roleId) {
        try {
            if (authDao.getRoleUsers(roleId) == 0) {
                authDao.delRole(roleId);
                return new BusinessWrapper<>(true);
            } else {
                return new BusinessWrapper<>(ErrorCode.roleHasRelyCannotDel);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public TableVO<List<ResourceVO>> getRoleBindResources(long roleId, long resourceGroupId, int page, int length) {
        long size = authDao.getRoleBindResourceSize(roleId, resourceGroupId);
        List<ResourceDO> list = authDao.getRoleBindResourcePage(roleId, resourceGroupId, page * length, length);

        List<ResourceVO> voList = new ArrayList<>();
        for (ResourceDO resourceDO : list) {
            List<ResourceGroupDO> groupDOList = authDao.getGroupByResourceId(resourceDO.getId());
            ResourceVO resourceVO = new ResourceVO(resourceDO, groupDOList);

            voList.add(resourceVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public TableVO<List<ResourceVO>> getRoleUnbindResources(long roleId, long resourceGroupId, int page, int length) {
        long size = authDao.getRoleUnbindResourceSize(roleId, resourceGroupId);
        List<ResourceDO> list = authDao.getRoleUnbindResourcePage(roleId, resourceGroupId, page * length, length);

        List<ResourceVO> voList = new ArrayList<>();
        for (ResourceDO resourceDO : list) {
            List<ResourceGroupDO> groupDOList = authDao.getGroupByResourceId(resourceDO.getId());
            ResourceVO resourceVO = new ResourceVO(resourceDO, groupDOList);

            voList.add(resourceVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public BusinessWrapper<Boolean> roleResourceBind(long roleId, long resourceId) {
        try {
            authDao.roleResourceBind(roleId, resourceId);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> roleResourceUnbind(long roleId, long resourceId) {
        try {
            authDao.roleResourceUnbind(roleId, resourceId);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public TableVO<List<UserVO>> getUsers(String username, long roleId, int page, int length) {
        long size = authDao.getUserSize(username, roleId);
        List<UserDO> list = authDao.getUserPage(username, roleId, page * length, length);

        List<UserVO> voList = new ArrayList<>();
        for (UserDO userDO : list) {
            RoleDO queryRoleDO = new RoleDO();
            queryRoleDO.setId(userDO.getRoleId());
            RoleDO roleDO = authDao.getRole(queryRoleDO);

            UserVO userVO = new UserVO(userDO, roleDO);
            voList.add(userVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public BusinessWrapper<Boolean> userRoleBind(long roleId, String username) {
        try {
            authDao.userRoleBind(username, roleId);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> userRoleBind(String roleName, String username) {
        RoleDO roleDO = new RoleDO();
        roleDO.setRoleName(roleName);
        roleDO = authDao.getRole(roleDO);
        return userRoleBind(roleDO.getId(), username);
    }

    @Override
    public BusinessWrapper<Boolean> userRoleUnbind(long roleId, String username) {
        try {
            authDao.userRoleUnbind(username, roleId);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> userRoleUnbind(String roleName, String username) {
        RoleDO roleDO = new RoleDO();
        roleDO.setRoleName(roleName);
        roleDO = authDao.getRole(roleDO);
        return userRoleUnbind(roleDO.getId(), username);
    }

    @Override
    public List<String> getUserGroup(String username) {
        List<String> list = userGroupMap.get(username);
        return list == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(list);
    }

    @Override
    public List<Long> getUserGroupIds(String username) {
        List<Long> groupIdList = new ArrayList<>();
        List<String> list = userGroupMap.get(username);
        if (list == null) {
            return groupIdList;
        }
        for (String groupName : list) {
            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupByName(groupName);

            if (serverGroupDO != null) {
                groupIdList.add(serverGroupDO.getId());
            }
        }
        return groupIdList;
    }

    @Override
    public List<Long> getUserRoleIds(String username) {
        return authDao.getUserRoleIds(username);
    }


    @Override
    public List<UserDO> getUsersByRole(long roleId) {
        List<String> userList = authDao.getUsersByRole(roleId);
        if (userList == null || userList.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<UserDO> userDOList = new ArrayList<>();
            for (String username : userList) {
                // getUserByName(username)
                userDOList.add(userDao.getUserByName(username));
            }
            return userDOList;
        }
    }

    @Override
    public List<UserVO> getUsersByRole(String roleName) {
        RoleDO roleDO = authDao.getRole(new RoleDO(roleName));
        if (roleDO != null) {
            List<UserDO> userDOList = getUsersByRole(roleDO.getId());
            List<UserVO> users = new ArrayList<>();
            for (UserDO userDO : userDOList) {
                users.add(new UserVO(userDO, true));
            }
            return users;
        }
        return new ArrayList<UserVO>();
    }



    @Override
    public BusinessWrapper<Boolean> delUser(String username) {
        if (invokeEnv.equals("daily")) {    //日常正常返回即可
            return new BusinessWrapper<>(true);
        }

        // 新增离职记录
        UserDO userDO = userDao.getUserByName(username);
        UserLeaveDO userLeaveDO = new UserLeaveDO();
        if (userDO != null) {
            //离职用户记录
            userLeaveDO = new UserLeaveDO(userDO);
        } else {
            userLeaveDO.setUsername(username);
        }
        userDao.addUserLeave(userLeaveDO);

        if (ldapService.delUser(username)) {
            userLeaveDO.setDumpLdap(UserLeaveDO.DumpTypeEnum.success.getCode());
            userService.saveUserLeave(userLeaveDO);
        } else {
            userLeaveDO.setDumpLdap(UserLeaveDO.DumpTypeEnum.fail.getCode());
            userService.saveUserLeave(userLeaveDO);
        }
        // TODO 删除用户的组
        ldapService.removeMember(username);
        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        // TODO 解除堡垒机授权
                        keyBoxService.delUserKeybox(username);
                        // TODO 删除用户服务器组关系
                        KeyboxUserServerDO userServerDO = new KeyboxUserServerDO();
                        userServerDO.setUsername(username);
                        keyBoxService.delUserGroup(userServerDO);

                        // TODO 本地也做清理
                        userDao.delUser(username);
                        // TODO 用户变更配置
                        configService.invokeUserConfig();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        status.setRollbackOnly();
                    }
                }
            });
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
        // 清理zabbix用户

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        List<UserDO> loginUserList = authDao.getLoginUsers();

        for (UserDO userDO : loginUserList) {
            if (!checkUserHasResourceAuthorize(userDO.getToken(), Permissions.lookAllServerGroup).isSuccess()) {
                // setUserGroup(userDO.getUsername());
            }
        }
    }

    /**
     * 1.避免过期token攻击
     * 2.赋予默认镇民角色,如有bamboo组,赋予开发角色
     * 3.记录登录信息
     *
     * @param username
     * @param userDO
     */
    private void setUserInit(String username, UserDO userDO) {
        authDao.updateUserTokenInvalid(username);

        RoleDO baseRoleDO = new RoleDO();
        baseRoleDO.setRoleName(RoleDO.roleBase);
        RoleDO roleDO = authDao.getRole(baseRoleDO);
        authDao.userRoleBind(username, roleDO.getId());
        authDao.addUserLoginRecord(userDO);
    }


    private void setUserGroup(String username) {
        List<String> groups = ldapService.searchLdapGroup(username);
        userGroupMap.put(username, groups);
    }

    @Override
    public BusinessWrapper<Boolean> resetToken() {
        try {
            authDao.updateTokenInvalid();
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> resetToken(String username) {
        try {
            authDao.updateUserTokenInvalid(username);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    /**
     * 添加ldap账户
     *
     * @param userVO
     * @return
     */
    public BusinessWrapper<Boolean> addUser(UserVO userVO) {
        if (StringUtils.isEmpty(userVO.getUsername())) return new BusinessWrapper<>(ErrorCode.usernameIsNull);
        // 判断LDAP是否存在此用户
        if (ldapService.addUser(userVO)) {
            loginCredentialCheck(userVO.getUsername(), userVO.getUserpassword());
            configService.invokeUserConfig();
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }


    @Override
    public List<UserDO> queryUsersByRoleName(String roleName) {
        List<UserDO> users = new ArrayList<>();
        RoleDO roleDO = new RoleDO();
        roleDO.setRoleName(roleName);
        roleDO = authDao.getRole(roleDO);
        if (roleDO == null) return users;
        users = authDao.getUserPage("", roleDO.getId(), 0, 5);

        for (UserDO userDO : users) {
            UserDO user = userDao.getUserByName(userDO.getUsername());
            if (!org.springframework.util.StringUtils.isEmpty(user.getDisplayName()))
                userDO.setDisplayName(user.getDisplayName());
            userDO.setMail(user.getMail());
            userDO.setId(user.getId());
            if (!org.springframework.util.StringUtils.isEmpty(user.getMobile()))
                userDO.setMobile(user.getMobile());
        }

        return users;
    }

}
