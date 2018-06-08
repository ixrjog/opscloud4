package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.AuthDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.*;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.LdapItemEnum;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerDO;
import com.sdg.cmdb.domain.ldap.LdapDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.extend.InvokeInvocation;
import com.sdg.cmdb.extend.InvokeResult;
import com.sdg.cmdb.handler.LdapHandler;
import com.sdg.cmdb.login.LoginPluginManager;
import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.PasswdUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.directory.*;
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


    //closed  active  locked
    public static final String MAIL_ACCOUNT_STATUS_CLOSED = "closed";

    public static final String MAIL_ACCOUNT_STATUS_LOCKED = "locked";

    public static final String MAIL_ACCOUNT_STATUS_ACTIVE = "active";

    @Resource
    private AuthDao authDao;

    @Resource
    private UserDao userDao;


    @Resource
    private UserService userService;

    @Resource
    private KeyBoxService keyBoxService;

    @Resource
    private LDAPFactory ldapFactory;

    @Resource
    private ConfigService configService;

//    @Resource
//    private LdapHandler ldapHandler;

    @Resource
    private SchedulerManager schedulerManager;

    private HashMap<String, String> configMap;

    private HashMap<String, String> acqConfigMap() {
        if (configMap != null) return configMap;
        return configCenterService.queryItemGroup(ConfigCenterItemGroupEnum.LDAP.getItemKey(),ConfigCenterServiceImpl.DEFAULT_ENV);
    }


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
            if(!result.isSuccess() && username.equals("admin") && !StringUtils.isEmpty(adminPasswd) && adminPasswd.equals(password))
                result.setSuccess(true);

            if (result.isSuccess()) {
                UserDO userDO = getUserByName(username);

                setUserInit(username, userDO);

                userDO.setResourceDOList(authDao.getUserAuthorizedResource(username, ResourceGroupDO.menu));

                if (!checkUserHasResourceAuthorize(userDO.getToken(), Permissions.lookAllServerGroup).isSuccess()) {
                    setUserGroup(userDO.getUsername());
                }

                //更新用户基本信息
                userDao.saveUserInfo(userDO);

                logger.info("user :" + username + " login success!");
                //新增业务，变更并同步翻墙配置
                configService.createAndInvokeConfigFile(ConfigServiceImpl.CONFIG_FILE_SHADOWSOCKS, ServerDO.EnvTypeEnum.prod.getCode());

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
                UserDO user = getUserByName(userDO.getUsername());

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
            //logger.info("user :" + username + " login failure!");
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
    public UserDO getUserByName(String username) {
        HashMap<String, String> configMap = acqConfigMap();
        String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());

        try {
            String dn = "cn=" + username + "," + userDN;
            DirContextAdapter adapter = (DirContextAdapter) ldapFactory.getLdapTemplateInstance().lookup(dn);
            String mail = adapter.getStringAttribute("mail");
            String displayName = adapter.getStringAttribute("displayname");
            UserDO userDO = new UserDO(mail, displayName, username, UUID.randomUUID().toString());
            userDO.setInvalid(UserDO.Invalid.invalid.getCode());
            //如果db有内容,则置入pwd
            UserDO dbUserDO = userDao.getUserByName(username);
            if (dbUserDO != null) {
                userDO.setPwd(dbUserDO.getPwd());
                userDO.setRsaKey(dbUserDO.getRsaKey());
                if (!StringUtils.isEmpty(dbUserDO.getMobile()))
                    userDO.setMobile(dbUserDO.getMobile());
            }
            if (StringUtils.isEmpty(userDO.getPwd())) {
                userDO.setPwd(PasswdUtils.getRandomPasswd(0));
            }
            return userDO;
        } catch (Exception e) {
            if (username.equals("admin")) {
                UserDO dbUserDO = userDao.getUserByName(username);
                dbUserDO.setToken(UUID.randomUUID().toString());
                return dbUserDO;
            }
            return null;
        }
    }

    @Override
    public List<UserDO> getUsersByRole(long roleId) {
        List<String> userList = authDao.getUsersByRole(roleId);
        if (userList == null || userList.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<UserDO> userDOList = new ArrayList<>();
            for (String username : userList) {
                userDOList.add(getUserByName(username));
            }
            return userDOList;
        }
    }

    @Override
    public BusinessWrapper<Boolean> unbindUser(String username) {
        LdapTemplate ldapTemplate = ldapFactory.getLdapTemplateInstance();
        HashMap<String, String> configMap = acqConfigMap();
        String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());
        String dn = "cn=" + username + "," + userDN;
        //删除ldap
        try {
            // 解除绑定的用户
            ldapTemplate.unbind(dn);
            // 解除绑定的组
            removeMember2Group(username);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(false);
        }

    }

    @Override
    public BusinessWrapper<Boolean> delUser(String username) {

        if (invokeEnv.equals("daily")) {    //日常正常返回即可
            return new BusinessWrapper<>(true);
        }
        LdapTemplate ldapTemplate = ldapFactory.getLdapTemplateInstance();
        coreLogger.warn("del user for:" + username);

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

        try {
            //删除ldap
            AndFilter filter = new AndFilter();
            filter.and(new EqualsFilter("cn", username));
            List list = ldapTemplate.search("", filter.encode(), (Attributes attrs) -> {
                return attrs.get("cn").get();
            });
            if (!list.isEmpty()) {
                HashMap<String, String> configMap = acqConfigMap();
                String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());
                String dn = "cn=" + username + "," + userDN;

                //删除ldap
                ldapTemplate.unbind(dn);
            }
            //记录删除成功
            userLeaveDO.setDumpLdap(UserLeaveDO.DumpTypeEnum.success.getCode());
            userService.saveUserLeave(userLeaveDO);
            //清理用户绑定的组
            removeMember2Group(username);
        } catch (Exception e) {
            e.printStackTrace();
            //记录删除失败
            userLeaveDO.setDumpLdap(UserLeaveDO.DumpTypeEnum.fail.getCode());
            userService.saveUserLeave(userLeaveDO);
        }

        try {
            //关闭邮箱
            if (StringUtils.isEmpty(userDO.getMail())) {
                String accountStatus = getMailAccountStatus(username + "@opscloud");
                if (!accountStatus.equalsIgnoreCase("null"))
                    userLeaveDO.setMail(username + "@opscloud");

                if (accountStatus.equalsIgnoreCase("null") || accountStatus.equalsIgnoreCase(MAIL_ACCOUNT_STATUS_CLOSED)) {
                    closeMailAccount(username);
                }
            }
            userLeaveDO.setDumpMail(UserLeaveDO.DumpTypeEnum.success.getCode());
            userService.saveUserLeave(userLeaveDO);
        } catch (Exception e) {
            userLeaveDO.setDumpMail(UserLeaveDO.DumpTypeEnum.fail.getCode());
            userService.saveUserLeave(userLeaveDO);
        }

        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        //解除堡垒机授权
                        keyBoxService.delUserKeybox(username);

                        //删除用户服务器组关系
                        KeyboxUserServerDO userServerDO = new KeyboxUserServerDO();
                        userServerDO.setUsername(username);
                        keyBoxService.delUserGroup(userServerDO);

                        //本地也做清理
                        userDao.delUser(username);
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
                setUserGroup(userDO.getUsername());
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

        /**
         * 如果有任意bamboo组,则视为有开发角色
         */
        if (!searchBambooGroupFilter(username).isEmpty()) {
            RoleDO developRole = new RoleDO();
            developRole.setRoleName(RoleDO.roleDevelop);
            RoleDO developRoleDO = authDao.getRole(developRole);
            authDao.userRoleBind(username, developRoleDO.getId());
        }

        authDao.addUserLoginRecord(userDO);
    }

    @Override
    public List<String> searchBambooGroupFilter(String username) {
        HashMap<String, String> configMap = acqConfigMap();
        // LdapTemplate ldapTemplate = acqLdapTemplate(LdapDO.LdapTypeEnum.cmdb.getDesc());
        String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());
        String groupDN = configMap.get(LdapItemEnum.LDAP_GROUP_DN.getItemKey());
        // bamboo-
        String groupFilter = configMap.get(LdapItemEnum.LDAP_GROUP_FILTER.getItemKey());
        // group_
        String groupPrefix = configMap.get(LdapItemEnum.LDAP_GROUP_PREFIX.getItemKey());

        String dn = "cn=" + username + "," + userDN;

        List<String> groups = Collections.EMPTY_LIST;

        try {
            groups = ldapFactory.getLdapTemplateInstance().search(LdapQueryBuilder.query().base(groupDN)
                            .where("uniqueMember").is(dn).and("cn").like(groupFilter + "*"),
                    new AttributesMapper<String>() {
                        @Override
                        public String mapFromAttributes(Attributes attributes) throws NamingException {
                            return groupPrefix + attributes.get("cn").get(0).toString().substring(groupFilter.length());
                        }
                    }
            );
        } catch (Exception e) {
            logger.warn("username={} search bamboo group error={}", username, e.getMessage(), e);
        }

        return groups;
    }

    @Override
    public List<String> searchLdapGroup(String username) {
        HashMap<String, String> configMap = acqConfigMap();
        // LdapTemplate ldapTemplate = acqLdapTemplate(LdapDO.LdapTypeEnum.cmdb.getDesc());
        String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());
        String groupDN = configMap.get(LdapItemEnum.LDAP_GROUP_DN.getItemKey());
        String dn = "cn=" + username + "," + userDN;

        List<String> groups = Collections.EMPTY_LIST;

        try {
            groups = ldapFactory.getLdapTemplateInstance().search(LdapQueryBuilder.query().base(groupDN)
                            .where("uniqueMember").is(dn).and("cn").like("*"),
                    new AttributesMapper<String>() {
                        @Override
                        public String mapFromAttributes(Attributes attributes) throws NamingException {
                            return attributes.get("cn").get(0).toString();
                        }
                    }
            );
        } catch (Exception e) {
            logger.warn("username={} search ldap group error={}", username, e.getMessage(), e);
        }

        return groups;
    }

    @Override
    public boolean isJiraUsers(String username) {
        HashMap<String, String> configMap = acqConfigMap();
        String groupJiraUsers = configMap.get(LdapItemEnum.LDAP_GROUP_JIRA_USERS.getItemKey());
        return isGroupMember(username, groupJiraUsers);
    }

    @Override
    public boolean isConfluenceUsers(String username) {
        HashMap<String, String> configMap = acqConfigMap();
        String groupConfluenceUsers = configMap.get(LdapItemEnum.LDAP_GROUP_CONFLUENCE_USERS.getItemKey());
        return isGroupMember(username, groupConfluenceUsers);
    }

    @Override
    public boolean isGroupMember(String username, String groupName) {
        HashMap<String, String> configMap = acqConfigMap();
        String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());
        String groupDN = configMap.get(LdapItemEnum.LDAP_GROUP_DN.getItemKey());
        String dn = "cn=" + username + "," + userDN;
        List<String> groups = ldapFactory.getLdapTemplateInstance().search(LdapQueryBuilder.query().base(groupDN)
                        .where("uniqueMember").is(dn).and("cn").like(groupName),
                (Attributes attributes) -> attributes.get("cn").get(0).toString()
        );
        if (groups.size() == 0)
            return false;
        return true;
    }


    private void setUserGroup(String username) {
        List<String> groups = searchBambooGroupFilter(username);
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

    @Override
    public boolean checkUserInLdapGroup(UserDO userDO, ServerGroupDO serverGroupDO) {
        List<String> groups = searchBambooGroupFilter(userDO.getUsername());

        for (String groupName : groups) {

            if (serverGroupDO.getName().equals(groupName)) return true;
        }
        return false;
    }

    @Override
    public boolean addMemberToGroup(UserDO userDO, ServerGroupDO serverGroupDO) {
        // 判断是否已经添加了用户
        if (checkUserInLdapGroup(userDO, serverGroupDO)) return true;
        return chgMemberToGroup(userDO, serverGroupDO, DirContext.ADD_ATTRIBUTE);
    }

    @Override
    public boolean addMemberToGroup(UserDO userDO, String groupName) {
        if (checkUserInLdapGroup(userDO, groupName)) return true;
        return chgMemberToGroup(userDO.getUsername(), groupName, DirContext.ADD_ATTRIBUTE);
    }


    public boolean checkUserInLdapGroup(UserDO userDO, String groupName) {
        List<String> groups = searchLdapGroup(userDO.getUsername());
        for (String name : groups) {
            if (name.equals(groupName)) return true;
        }
        return false;
    }


    @Override
    public boolean delMemberToGroup(UserDO userDO, ServerGroupDO serverGroupDO) {
        // 判断是否已经添加了用户
        if (!checkUserInLdapGroup(userDO, serverGroupDO)) return true;
        return chgMemberToGroup(userDO, serverGroupDO, DirContext.REMOVE_ATTRIBUTE);
    }

    @Override
    public boolean delMemberToGroup(UserDO userDO, String groupName) {
        if (!checkUserInLdapGroup(userDO, groupName)) return true;
        return chgMemberToGroup(userDO.getUsername(), groupName, DirContext.REMOVE_ATTRIBUTE);
    }


    private boolean chgMemberToGroup(UserDO userDO, ServerGroupDO serverGroupDO, int type) {
        //LdapTemplate ldapTemplate = acqLdapTemplate(LdapDO.LdapTypeEnum.cmdb.getDesc());
        HashMap<String, String> configMap = acqConfigMap();
        String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());
        String groupDN = configMap.get(LdapItemEnum.LDAP_GROUP_DN.getItemKey());
        String groupFilter = configMap.get(LdapItemEnum.LDAP_GROUP_FILTER.getItemKey());
        String groupPrefix = configMap.get(LdapItemEnum.LDAP_GROUP_PREFIX.getItemKey());

        boolean isAddedSuccessfully = false;
        String groupName = serverGroupDO.getName();
        groupName = groupName.replaceAll(groupPrefix, groupFilter);
        String dn = "cn=" + groupName + "," + groupDN;
        String value = "cn=" + userDO.getUsername() + "," + userDN;
        try {
            ldapFactory.getLdapTemplateInstance().modifyAttributes(dn, new ModificationItem[]{
                    new ModificationItem(type, new BasicAttribute("uniqueMember", value))
            });
            isAddedSuccessfully = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            isAddedSuccessfully = false;
        }
        return isAddedSuccessfully;
    }

    private boolean chgMemberToGroup(String username, String groupname, int type) {
        HashMap<String, String> configMap = acqConfigMap();
        String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());
        String groupDN = configMap.get(LdapItemEnum.LDAP_GROUP_DN.getItemKey());
        //boolean isAddedSuccessfully = false;
        String dn = "cn=" + groupname + "," + groupDN;
        String value = "cn=" + username + "," + userDN;
        try {
            ldapFactory.getLdapTemplateInstance().modifyAttributes(dn, new ModificationItem[]{
                    new ModificationItem(type, new BasicAttribute("uniqueMember", value))
            });
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean removeMember2Group(String username, String groupName) {
        HashMap<String, String> configMap = acqConfigMap();
        String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());
        String groupDN = configMap.get(LdapItemEnum.LDAP_GROUP_DN.getItemKey());

        String dn = "cn=" + groupName + "," + groupDN;
        String value = "cn=" + username + "," + userDN;
        try {
            ldapFactory.getLdapTemplateInstance().modifyAttributes(dn, new ModificationItem[]{
                    new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("uniqueMember", value))
            });
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public BusinessWrapper<Boolean> removeMember2Group(String username) {
        try {
            schedulerManager.registerJob(() -> {
                List<String> groups = searchLdapGroup(username);
                for (String groupName : groups)
                    removeMember2Group(username, groupName);
            });
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(false);
        }
    }

    /**
     * 添加ldap账户
     *
     * @param userDO
     * @return
     */
    public BusinessWrapper<Boolean> addUser(UserDO userDO) {
        if (StringUtils.isEmpty(userDO.getUsername())) return new BusinessWrapper<>(ErrorCode.usernameIsNull);
        // 判断LDAP是否存在此用户
        UserDO ldapUserDO = getUserByName(userDO.getUsername());
        if (ldapUserDO != null) {
            userDao.saveUserInfo(ldapUserDO);
            userService.addUserMobile(ldapUserDO.getId());
            return new BusinessWrapper<>(true);
        } else {
            try {
                HashMap<String, String> configMap = acqConfigMap();
                String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());
                String dn = "cn=" + userDO.getUsername() + "," + userDN;
                //LdapTemplate ldapTemplate = acqLdapTemplate(LdapDO.LdapTypeEnum.cmdb.getDesc());
                // 基类设置
                BasicAttribute ocattr = new BasicAttribute("objectClass");
                ocattr.add("top");
                ocattr.add("person");
                //ocattr.add("uidObject");
                ocattr.add("inetorgperson");
                ocattr.add("organizationalPerson");
                // 用户属性
                Attributes attrs = new BasicAttributes();
                attrs.put(ocattr);
                attrs.put("cn", userDO.getUsername());
                attrs.put("sn", userDO.getUsername());
                attrs.put("displayName", userDO.getDisplayName());
                attrs.put("mail", userDO.getMail());
                attrs.put("userpassword", userDO.getPwd());
                System.err.println(dn);
                ldapFactory.getLdapTemplateInstance().bind(dn, null, attrs);
                // 模拟登录插入本地用户数据
                loginCredentialCheck(userDO.getUsername(), userDO.getPwd());
                return new BusinessWrapper<>(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                return new BusinessWrapper<>(ErrorCode.serverFailure);
            }
        }
    }

    @Override
    public BusinessWrapper<Boolean> closeMailAccount(String username) {
        UserDO userDO = userDao.getUserByName(username);
        if (userDO == null) {
            userDO = new UserDO();
            userDO.setMail(username);
        }
        try {
            return new BusinessWrapper<>(setMailUserAccountStatus(userDO, MAIL_ACCOUNT_STATUS_CLOSED));

        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> activeMailAccount(String username) {
        UserDO userDO = userDao.getUserByName(username);
        if (userDO == null) {
            userDO = new UserDO();
            userDO.setMail(username);
        }
        try {
            return new BusinessWrapper<>(setMailUserAccountStatus(userDO, MAIL_ACCOUNT_STATUS_ACTIVE));
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> addLdapGroup(String username, String groupname) {
        UserDO userDO = userDao.getUserByName(username);
        if (userDO == null) return new BusinessWrapper<>(false);
        if (isGroupMember(username, groupname)) return new BusinessWrapper<>(true);
        if (chgMemberToGroup(username, groupname, DirContext.ADD_ATTRIBUTE)) {
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delLdapGroup(String username, String groupname) {
        UserDO userDO = userDao.getUserByName(username);
        if (userDO == null) return new BusinessWrapper<>(false);
        if (!isGroupMember(username, groupname)) return new BusinessWrapper<>(true);
        if (chgMemberToGroup(username, groupname, DirContext.REMOVE_ATTRIBUTE)) {
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(false);
        }
    }


    @Override
    public boolean setMailUserAccountStatus(UserDO userDO, String status) {
        //LdapTemplate ldapTemplate = acqLdapTemplate(LdapDO.LdapTypeEnum.mail.getDesc());
        HashMap<String, String> configMap = acqConfigMap();
        // ou=people,dc=51xianqu,dc=net
        String userDN = configMap.get(LdapItemEnum.LDAP_MAIL_USER_DN.getItemKey());


        String dn = "";

        if (StringUtils.isEmpty(userDO.getMail())) {
            if (StringUtils.isEmpty(userDO.getUsername())) {
                dn = "uid=" + userDO.getUsername() + "," + userDN;
            } else {
                return false;
            }
        } else {
            dn = "uid=" + userDO.getMail().replaceAll("@opscloud", "") + "," + userDN;
        }

        try {
            ldapFactory.getLdapTemplateInstance().modifyAttributes(dn, new ModificationItem[]
                    {
                            new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("zimbraAccountStatus", status))
                    });
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public String getMailAccountStatus(String mail) {
        //LdapTemplate ldapTemplate = acqLdapTemplate(LdapDO.LdapTypeEnum.mail.getDesc());
        HashMap<String, String> configMap = acqConfigMap();
        // ou=people,dc=51xianqu,dc=net
        String userDN = configMap.get(LdapItemEnum.LDAP_MAIL_USER_DN.getItemKey());

        String username = mail.replaceAll("@opscloud", "");
        String dn = "uid=" + username + "," + userDN;
        try {
            DirContextAdapter adapter = (DirContextAdapter) ldapFactory.getLdapTemplateInstance().lookup(dn);
            String status = adapter.getStringAttribute("zimbraAccountStatus");
            return status;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return "null";
        }
    }

    @Override
    public boolean checkUserInLdap(String username) {
        HashMap<String, String> configMap = acqConfigMap();
        String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());

        try {
            String dn = "cn=" + username + "," + userDN;
            DirContextAdapter adapter = (DirContextAdapter) ldapFactory.getLdapTemplateInstance().lookup(dn);
            String cn = adapter.getStringAttribute("cn");
            if (username.equalsIgnoreCase(cn)) return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getAccountLocked(String username) {
        //LdapTemplate ldapTemplate = acqLdapTemplate(LdapDO.LdapTypeEnum.mail.getDesc());
        HashMap<String, String> configMap = acqConfigMap();
        // ou=people,dc=51xianqu,dc=net
        String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());
        String dn = "cn=" + username + "," + userDN;
        try {
            DirContextAdapter adapter = (DirContextAdapter) ldapFactory.getLdapTemplateInstance().lookup(dn);

            String status = adapter.getStringAttribute("pwdAccountLockedTime");

            return status;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return "null";
        }
    }

    /**
     * 查询所有的bamboo用户组
     *
     * @return
     */
    @Override
    public List<String> searchBambooGroup() {
        HashMap<String, String> configMap = acqConfigMap();
        String groupDN = configMap.get(LdapItemEnum.LDAP_GROUP_DN.getItemKey());
        // bamboo-
        String groupFilter = configMap.get(LdapItemEnum.LDAP_GROUP_FILTER.getItemKey());
        List<String> groups = Collections.EMPTY_LIST;
        try {
            groups = ldapFactory.getLdapTemplateInstance().search(LdapQueryBuilder.query().base(groupDN)
                            .where("objectClass").is("groupOfUniqueNames").and("cn").like(groupFilter + "*"),
                    new AttributesMapper<String>() {
                        @Override
                        public String mapFromAttributes(Attributes attributes) throws NamingException {
                            return attributes.get("cn").get(0).toString();
                        }
                    }
            );
        } catch (Exception e) {
            logger.warn("search bamboo group error={}", e.getMessage(), e);
        }

        return groups;
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
