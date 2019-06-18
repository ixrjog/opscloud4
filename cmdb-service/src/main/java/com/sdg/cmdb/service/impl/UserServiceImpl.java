package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.AuthDao;
import com.sdg.cmdb.dao.cmdb.CiDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.*;
import com.sdg.cmdb.domain.ci.CiAppAuthDO;
import com.sdg.cmdb.domain.ci.CiAppDO;
import com.sdg.cmdb.domain.ldap.LdapDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.ssh.SshKey;
import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.service.configurationProcessor.ShadowsocksFileProcessorService;
import com.sdg.cmdb.util.SessionUtils;


import org.gitlab.api.models.GitlabUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxxiao on 2016/11/22.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private CiDao ciDao;

    @Autowired
    private ServerGroupService serverGroupService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthDao authDao;

    @Autowired
    private LdapService ldapService;

    @Autowired
    private GitlabService gitlabService;

    @Autowired
    protected ShadowsocksFileProcessorService ssService;

    @Autowired
    private JumpserverService jumpserverService;

    @Autowired
    private AliyunRAMService aliyunRAMService;

    @Autowired
    private CiService ciService;

    @Override
    public TableVO<List<UserDO>> getUserPage(String username, int page, int length) {
        long size = userDao.getUserSize(username);
        List<UserDO> list = userDao.getUserPage(username, page * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public TableVO<List<UserVO>> getSafeUserPage(String username, int page, int length) {
        long size = userDao.getUserSize(username);
        List<UserDO> list = userDao.getUserPage(username, page * length, length);
        List<UserVO> listVO = new ArrayList<>();
        for (UserDO userDO : list) {
            boolean safe = true;
            UserVO userVO = new UserVO(userDO, safe);
            listVO.add(userVO);
        }
        return new TableVO<>(size, listVO);
    }

    @Override
    public TableVO<List<UserVO>> getCmdbUserPage(String username, int page, int length) {
        long size = userDao.getUserSize(username);
        List<UserDO> list = userDao.getUserPage(username, page * length, length);
        List<UserVO> listVO = new ArrayList<>();

        for (UserDO userDO : list) {
            UserVO userVO = new UserVO(userDO);
            if (ldapService.checkUserInLdap(userVO.getUsername()))
                userVO.setLdap(1);
            userVO.setLdapGroups(ldapService.searchUserLdapGroup(userVO.getUsername()));
            // 加入角色信息
            userVO.setRoleDOList(getUserRoles(userVO.getUsername()));
            listVO.add(userVO);
        }
        return new TableVO<>(size, listVO);
    }

    /**
     * 查询用户角色
     *
     * @param username
     */
    private List<RoleDO> getUserRoles(String username) {
        List<UserDO> authUserRolelist = authDao.getUser(username);
        List<RoleDO> roles = new ArrayList<>();
        for (UserDO userDO : authUserRolelist) {
            RoleDO queryRoleDO = new RoleDO();
            queryRoleDO.setId(userDO.getRoleId());
            RoleDO roleDO = authDao.getRole(queryRoleDO);
            roles.add(roleDO);
        }
        return roles;
    }


    @Override
    public BusinessWrapper<UserVO> getUserVOByName(String username) {
        UserDO userDO = userDao.getUserByName(username);
        if (userDO == null) {
            return new BusinessWrapper<>(ErrorCode.userNotExist);
        }

        List<ServerGroupDO> groupDOList = serverGroupService.getServerGroupsByUsername(username);
        UserVO userVO = new UserVO(userDO, groupDOList);
        if (ldapService.checkUserInLdap(userVO.getUsername()))
            userVO.setLdap(1);
        userVO.setLdapGroups(ldapService.searchUserLdapGroup(userVO.getUsername()));
        userVO.setSsList(ssService.getSsByUser(userDO));
        userVO.setSetKeyByGitlab(gitlabService.isSetKey(username));
        userVO.setSetKeyByJms(jumpserverService.isSetKey(username));
        userVO.setCiAppList(ciService.queryUserApp(userDO.getId()));
        invokeSshKey(userVO, userDO);
        aliyunRAMService.invokeRam(userVO);
        //EncryptionUtil.fingerprint()
        return new BusinessWrapper<>(userVO);
    }

    private void invokeSshKey(UserVO userVO, UserDO userDO) {
        if (!StringUtils.isEmpty(userDO.getRsaKey())) {
            String key = userDO.getRsaKey();
            String[] keys = key.split(" +");
            SshKey sshKey;
            if (keys.length == 3) {
                sshKey = new SshKey(key, keys[2]);
            } else {
                sshKey = new SshKey(key);
            }
            userVO.setSshKey(sshKey);
        }
    }


    @Override
    public TableVO<UserVO> getCmdbUser(String username) {
        UserDO userDO = userDao.getUserByName(username);
        UserVO userVO = new UserVO(userDO);
        if (ldapService.checkUserInLdap(userVO.getUsername()))
            userVO.setLdap(1);
        userVO.setLdapGroups(ldapService.searchUserLdapGroup(userVO.getUsername()));
        return new TableVO<>(1, userVO);
    }

    @Override
    public TableVO<List<UserLeaveVO>> getUserLeavePage(String username, int page, int length) {
        long size = userDao.getUserLeaveSize(username);
        List<UserLeaveDO> list = userDao.getUserLeavePage(username, page * length, length);
        List<UserLeaveVO> listVO = new ArrayList<>();

        for (UserLeaveDO userLeaveDO : list) {
            UserLeaveVO userLeaveVO = new UserLeaveVO(userLeaveDO);
            if (ldapService.checkUserInLdap(userLeaveVO.getUsername()))
                userLeaveVO.setLdap(1);
            userLeaveVO.setLdapGroups(ldapService.searchLdapGroup(userLeaveVO.getUsername()));
            listVO.add(userLeaveVO);
        }

        return new TableVO<>(size, listVO);
    }

    @Override
    public BusinessWrapper<Boolean> delUserLeave(long id) {
        try {
            userDao.delUserLeave(id);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public UserDO getUserDOByName(String username) {
        try {
            UserDO userDO = ldapService.getUserByName(username);
            if (userDO == null) {
                userDao.delUser(username);
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return userDao.getUserByName(username);
    }

    @Override
    public UserDO getUserByDO(UserDO userDO) {
        return userDao.getUser(userDO);
    }

    @Override
    public BusinessWrapper<Boolean> saveUserInfo(UserDO userDO) {
        if (userDO.getUsername().equals(SessionUtils.getUsername()) || authService.isRole(SessionUtils.getUsername(), RoleDO.roleAdmin)) {
            userDao.updateUser(userDO);
            // TODO 如果用户提交pubKey则同步JMS/Gitlab
            if (!StringUtils.isEmpty(userDO.getRsaKey())) {
                jumpserverService.updateUserPubkey(userDO.getUsername());
                gitlabService.pushSSHKey(userDO.getUsername(), userDO.getRsaKey());
            }

            return new BusinessWrapper<>(true);
        } else {
            BusinessWrapper<Boolean> checkWrapper = authService.checkUserHasResourceAuthorize(SessionUtils.getToken(), Permissions.canEditUserInfo);
            if (checkWrapper.isSuccess()) {
                userDao.updateUser(userDO);
                //createKeyFile(userDO.getUsername(), userDO.getRsaKey());
                return new BusinessWrapper<>(true);
            } else {
                return new BusinessWrapper<>(ErrorCode.authenticationFailure);
            }
        }
    }

    @Override
    public boolean updateUserAuthStatus(UserDO userDO) {
        try {
            userDao.updateUserAuth(userDO);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean saveUserLeave(UserLeaveDO userLeaveDO) {
        try {
            userDao.updateUserLeave(userLeaveDO);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String queryUserMobileByEmail(String email) {
        return "";
    }

    @Override
    public BusinessWrapper<Boolean> addUsersMobile() {
        List<UserDO> list = userDao.getAllUser();
        for (UserDO userDO : list) {
            addUserMobile(userDO, false);
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> addUserMobile(long userId) {
        UserDO userDO = userDao.getUserById(userId);
        if (userDO == null) return new BusinessWrapper<>(false);
        return new BusinessWrapper<>(addUserMobile(userDO, true));
    }

    private boolean addUserMobile(UserDO userDO, boolean refresh) {
        try {
            if (!StringUtils.isEmpty(userDO.getMobile()) && !refresh) return false;
            if (StringUtils.isEmpty(userDO.getMail())) return false;
            String mobile = queryUserMobileByEmail(userDO.getMail());
            if (!StringUtils.isEmpty(mobile)) {
                // 更新mobile
                userDO.setMobile(mobile);
                userDao.updateUser(userDO);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<UserVO> queryUserExcludeApp(String username, long appId) {
        List<UserDO> userList = userDao.queryUserExcludeApp(username, appId);
        List<UserVO> voList = new ArrayList<>();
        for (UserDO userDO : userList)
            voList.add(new UserVO(userDO, true));
        return voList;
    }

    @Override
    public List<UserVO> queryUserByApp(long appId) {
        List<CiAppAuthDO> ciAppAuthList = ciDao.queryCiAppAuthByAppId(appId);
        List<UserVO> userList = new ArrayList<>();
        for (CiAppAuthDO ciAppAuthDO : ciAppAuthList) {
            UserDO userDO = userDao.getUserById(ciAppAuthDO.getUserId());
            if (userDO == null) continue;
            UserVO userVO = new UserVO(userDO, true);
            userList.add(userVO);
        }
        return userList;
    }

    @Override
    public BusinessWrapper<Boolean> addUserByApp(long appId, long userId) {
        CiAppAuthDO ciAppAuthDO = ciDao.getCiAppAuthByAppIdAndUserId(appId, userId);
        if (ciAppAuthDO != null) return new BusinessWrapper<Boolean>(true);
        UserDO userDO = userDao.getUserById(userId);
        CiAppDO ciAppDO = ciDao.getCiApp(appId);
        ciAppAuthDO = new CiAppAuthDO(ciAppDO, userDO);
        try {
            ciDao.addCiAppAuth(ciAppAuthDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delUserByApp(long appId, long userId) {
        try {
            CiAppAuthDO ciAppAuthDO = ciDao.getCiAppAuthByAppIdAndUserId(appId, userId);
            if (ciAppAuthDO != null)
                ciDao.delCiAppAuth(ciAppAuthDO.getId());
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    /**
     * @param displayName username<displayName>
     * @return
     */
    @Override
    public UserDO getUserByDisplayName(String displayName) {
        try {
            String name[] = displayName.split("<");
            String username = name[0];
            UserDO userDO = userDao.getUserByName(username);
            if (userDO != null) return userDO;
        } catch (Exception e) {
            return userDao.getUserByName(displayName);
        }
        return null;
    }


}
