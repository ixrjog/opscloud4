package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.AuthDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.*;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.GetwayItemEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.ShadowsocksItemEnum;
import com.sdg.cmdb.domain.ldap.LdapDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import com.sdg.cmdb.service.AuthService;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.ServerGroupService;
import com.sdg.cmdb.service.UserService;
import com.sdg.cmdb.util.IOUtils;
import com.sdg.cmdb.util.SessionUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxxiao on 2016/11/22.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final static String API_AUTH_KEY="02a966823690516ebee262e541404454";

    @Resource
    private UserDao userDao;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private AuthService authService;

    @Resource
    private AuthDao authDao;

    @Resource
    private ConfigCenterService configCenterService;

    @Resource
    private LDAPFactory ldapFactory;

    private HashMap<String, String> getwayConfigMap;

    private HashMap<String, String> acqGetwayConifMap() {
        if (getwayConfigMap != null) return getwayConfigMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.GETWAY.getItemKey());
    }

    private HashMap<String, String> shadowsocksConfigMap;

    private HashMap<String, String> acqShadowsocksConifMap() {
        if (shadowsocksConfigMap != null) return getwayConfigMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.SHADOWSOCKS.getItemKey());
    }

    private HashMap<String, String> ldapConfigMap;

    private HashMap<String, String> acqConifMap() {
        if (ldapConfigMap != null) return ldapConfigMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.LDAP.getItemKey());
    }

    private LdapTemplate acqLdapTemplate(String ldapType) {
        HashMap<String, String> configMap = acqConifMap();
        if (ldapType.equalsIgnoreCase(LdapDO.LdapTypeEnum.cmdb.getDesc()))
            return ldapFactory.buildLdapTemplate(new LdapDO(configMap, ldapType));

        if (ldapType.equalsIgnoreCase(LdapDO.LdapTypeEnum.mail.getDesc()))
            return ldapFactory.buildLdapTemplate(new LdapDO(configMap, ldapType));

        return null;
    }

    @Override
    public TableVO<List<UserDO>> getUserPage(String username, int page, int length) {
        long size = userDao.getUserSize(username);
        List<UserDO> list = userDao.getUserPage(username, page * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public TableVO<List<UserVO>> getSafeUserPage(String username, int page, int length){
        long size = userDao.getUserSize(username);
        List<UserDO> list = userDao.getUserPage(username, page * length, length);
        List<UserVO> listVO = new ArrayList<>();
        for (UserDO userDO : list) {
            boolean safe = true;
            UserVO userVO = new UserVO(userDO,safe);
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
            if (authService.checkUserInLdap(userVO.getUsername()))
                userVO.setLdap(1);
            userVO.setLdapGroups(authService.searchLdapGroup(userVO.getUsername()));
            invokeLdapGroups(userVO);
            if (!StringUtils.isEmpty(userVO.getMail()))
                userVO.setMailAccountStatus(authService.getMailAccountStatus(userVO.getMail()));
            // 加入角色信息
            userVO.setRoleDOList(getUserRoles(userVO.getUsername()));
            listVO.add(userVO);
        }
        return new TableVO<>(size, listVO);
    }

    @Override
    public  TableVO<List<UserVO>> getCmdbApiUserPage(String authKey,String username, int page, int length){
        if(!authKey.equals(API_AUTH_KEY))
            return new TableVO(0,ErrorCode.authenticationFailure);

        long size = userDao.getUserSize(username);
        List<UserDO> list = userDao.getUserPage(username, page * length, length);
        List<UserVO> listVO = new ArrayList<>();

        for (UserDO userDO : list) {
            UserVO userVO = new UserVO(userDO);
           //if (authService.checkUserInLdap(userVO.getUsername()))
           //     userVO.setLdap(1);
           // userVO.setLdapGroups(authService.searchLdapGroup(userVO.getUsername()));
           // invokeLdapGroups(userVO);
            if (!StringUtils.isEmpty(userVO.getMail()))
                userVO.setMailAccountStatus(authService.getMailAccountStatus(userVO.getMail()));
            // 加入角色信息
            userVO.setRoleDOList(getUserRoles(userVO.getUsername()));
            userVO.setPwd("");
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
        HashMap<String, String> configMap = acqShadowsocksConifMap();
        String shadowsocksServer1 = configMap.get(ShadowsocksItemEnum.SHADOWSOCKS_SERVER_1.getItemKey());
        String shadowsocksServer2 = configMap.get(ShadowsocksItemEnum.SHADOWSOCKS_SERVER_2.getItemKey());

        UserDO userDO = userDao.getUserByName(username);
        if (userDO == null) {
            return new BusinessWrapper<>(ErrorCode.userNotExist);
        }

        List<ServerGroupDO> groupDOList = serverGroupService.getServerGroupsByUsername(username);
        UserVO userVO = new UserVO(userDO, groupDOList);
        userVO.setShadowsocksServer1(shadowsocksServer1);
        userVO.setShadowsocksServer2(shadowsocksServer2);
        if (authService.checkUserInLdap(userVO.getUsername()))
            userVO.setLdap(1);
        userVO.setLdapGroups(authService.searchLdapGroup(userVO.getUsername()));
        invokeLdapGroups(userVO);
        return new BusinessWrapper<>(userVO);
    }


    @Override
    public TableVO<UserVO> getCmdbUser(String username) {
        UserDO userDO = userDao.getUserByName(username);
        UserVO userVO = new UserVO(userDO);
        if (authService.checkUserInLdap(userVO.getUsername()))
            userVO.setLdap(1);
        userVO.setLdapGroups(authService.searchLdapGroup(userVO.getUsername()));
        invokeLdapGroups(userVO);
        //ddd
        if (!StringUtils.isEmpty(userVO.getMail()))
            userVO.setMailAccountStatus(authService.getMailAccountStatus(userVO.getMail()));
        return new TableVO<>(1, userVO);
    }


    public void invokeLdapGroups(UserVO userVO) {
        if (userVO.getLdapGroups().size() == 0) return;
        List<LdapGroupVO> bambooLdapGroups = new ArrayList<LdapGroupVO>();
        List<LdapGroupVO> otherLdapGroups = new ArrayList<LdapGroupVO>();

        UserLdapGroupVO userLdapGroupVO = new UserLdapGroupVO();
        boolean check;
        for (String name : userVO.getLdapGroups()) {
            check = false;
            for (LdapGroupVO.LdapGroupTypeEnum type : LdapGroupVO.LdapGroupTypeEnum.values()) {
                if (name.indexOf(type.getDesc()) >= 0) {

                    check = true;
                    LdapGroupVO ldapGroupVO = new LdapGroupVO(name, type.getCode());
                    if (type.getCode() == LdapGroupVO.LdapGroupTypeEnum.bamboo.getCode()) {
                        bambooLdapGroups.add(ldapGroupVO);
                    } else {
                        userLdapGroupVO.setLdapGroup(ldapGroupVO);
                        otherLdapGroups.add(ldapGroupVO);
                    }
                    break;
                }
            }
            if (!check) {
                otherLdapGroups.add(new LdapGroupVO(name, -1));
            }

        }
        userVO.setBambooLdapGroups(bambooLdapGroups);
        userVO.setOtherLdapGroups(otherLdapGroups);
        //用于子菜单
        userVO.setUserLdapGroupVO(userLdapGroupVO);
    }


    @Override
    public TableVO<List<UserLeaveVO>> getUserLeavePage(String username, int page, int length) {
        long size = userDao.getUserLeaveSize(username);
        List<UserLeaveDO> list = userDao.getUserLeavePage(username, page * length, length);
        List<UserLeaveVO> listVO = new ArrayList<>();

        for (UserLeaveDO userLeaveDO : list) {
            UserLeaveVO userLeaveVO = new UserLeaveVO(userLeaveDO);
            if (authService.checkUserInLdap(userLeaveVO.getUsername()))
                userLeaveVO.setLdap(1);
            userLeaveVO.setLdapGroups(authService.searchLdapGroup(userLeaveVO.getUsername()));
            if (!StringUtils.isEmpty(userLeaveVO.getMail()))
                userLeaveVO.setMailAccountStatus(authService.getMailAccountStatus(userLeaveVO.getMail()));
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
        LdapTemplate ldapTemplate = acqLdapTemplate(LdapDO.LdapTypeEnum.cmdb.getDesc());

        String dn = "cn=" + username + ",ou=users,ou=system";
        try {
            DirContextAdapter adapter = (DirContextAdapter) ldapTemplate.lookup(dn);
            if (adapter == null) {
                userDao.delUser(username);
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return userDao.getUserByName(username);
    }


    @Override
    public BusinessWrapper<Boolean> saveUserInfo(UserDO userDO) {
        if (userDO.getUsername().equals(SessionUtils.getUsername())) {
            userDao.updateUser(userDO);
            createKeyFile(userDO.getUsername(), userDO.getRsaKey());

            return new BusinessWrapper<>(true);
        } else {
            BusinessWrapper<Boolean> checkWrapper = authService.checkUserHasResourceAuthorize(SessionUtils.getToken(), Permissions.canEditUserInfo);
            if (checkWrapper.isSuccess()) {
                userDao.updateUser(userDO);
                createKeyFile(userDO.getUsername(), userDO.getRsaKey());

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


    /**
     * 创建key file
     *
     * @param username
     * @param rsaKey
     */
    private void createKeyFile(String username, String rsaKey) {
        HashMap<String, String> configMap = acqGetwayConifMap();
        String keyPath = configMap.get(GetwayItemEnum.GETWAY_KEY_PATH.getItemKey());
        String keyFile = configMap.get(GetwayItemEnum.GETWAY_KEY_FILE.getItemKey());

        String path = keyPath + "/"+username + keyFile;

        IOUtils.writeFile(rsaKey, path);
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


}
