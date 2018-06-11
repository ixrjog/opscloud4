package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.KeyboxDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.config.ConfigFileDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.GetwayItemEnum;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailVO;
import com.sdg.cmdb.domain.keybox.*;
import com.sdg.cmdb.domain.keybox.keyboxStatus.KeyboxServerVO;
import com.sdg.cmdb.domain.keybox.keyboxStatus.KeyboxStatusVO;
import com.sdg.cmdb.domain.keybox.keyboxStatus.KeyboxUserVO;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.template.getway.Getway;
import com.sdg.cmdb.util.EncryptionUtil;
import com.sdg.cmdb.util.IOUtils;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeViewUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxxiao on 2016/11/20.
 */
@Service
public class KeyBoxServiceImpl implements KeyBoxService {

    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");
    private static final Logger logger = LoggerFactory.getLogger(KeyBoxServiceImpl.class);

    //private static final String cmdLineStr = "/usr/local/prometheus/tools/getway_account_interface";

    @Resource
    private KeyboxDao keyboxDao;

    @Resource
    private UserService userService;

    @Resource
    private UserDao userDao;

    @Resource
    private ServerService serverService;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private IPService ipService;

    @Resource
    private SchedulerManager schedulerManager;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private AuthService authService;

    @Resource
    private AnsibleTaskService ansibleTaskService;

    @Resource
    private ConfigCenterService configCenterService;


    @Resource
    private CiUserGroupService ciUserGroupService;

    private HashMap<String, String> configMap;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.GETWAY.getItemKey());
    }


    @Override
    public TableVO<List<KeyboxUserServerVO>> getUserServerPage(KeyboxUserServerDO userServerDO, int page, int length) {
        long size = keyboxDao.getUserServerSize(userServerDO);
        List<KeyboxUserServerDO> userServerDOList = keyboxDao.getUserServerPage(userServerDO, page * length, length);
        List<KeyboxUserServerVO> userServerVOList = new ArrayList<>();
        for (KeyboxUserServerDO userServerDOItem : userServerDOList) {
            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(userServerDOItem.getServerGroupId());
            KeyboxUserServerVO userServerVO = new KeyboxUserServerVO(userServerDOItem, serverGroupDO);
            userServerVOList.add(userServerVO);
        }
        return new TableVO<>(size, userServerVOList);
    }

    @Override
    public BusinessWrapper<String> launchUserGetway(String username) {

        HashMap<String, String> configMap = acqConifMap();
        String getwayUserConfPath = configMap.get(GetwayItemEnum.GETWAY_USER_CONF_PATH.getItemKey());

        try {
            String value = IOUtils.readFile(getwayUserConfPath + "/" + username + "/getway.conf");
            return new BusinessWrapper<>(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure.getCode(), "文件不存在!");
        }
    }

    @Override
    public long getUserServerSize(KeyboxUserServerDO userServerDO) {
        return keyboxDao.getUserServerSize(userServerDO);
    }

    /**
     * 用户授权
     *
     * @param username
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> authUserKeybox(String username) {
        coreLogger.info(SessionUtils.getUsername() + " invoke method, auth user:" + username);

        UserDO userDO = userService.getUserDOByName(username);
        if (userDO == null) {
            return new BusinessWrapper<>(ErrorCode.userNotExist);
        }

        if (StringUtils.isEmpty(userDO.getPwd())) {
            return new BusinessWrapper<>(ErrorCode.userPwdNotInput);
        }

        BusinessWrapper<Boolean> wrapper = ansibleTaskService.taskGetwayAddAccount(userDO.getUsername(), userDO.getPwd());
        userDO.setAuthed(UserDO.AuthType.authed.getCode());
        userService.updateUserAuthStatus(userDO);
        return wrapper;
    }

    @Override
    public BusinessWrapper<Boolean> delUserKeybox(String username) {
        coreLogger.info(SessionUtils.getUsername() + " invoke method, del user:" + username);

        UserDO userDO = userService.getUserDOByName(username);
        if (userDO == null) {
            // return new BusinessWrapper<>(ErrorCode.userNotExist);
            userDO = new UserDO();
            userDO.setUsername(username);
        }

        delKeyFile(username);


        try {
            BusinessWrapper<Boolean> wrapper = ansibleTaskService.taskGetwayDelAccount(userDO.getUsername());
            userDO.setAuthed(UserDO.AuthType.noAuth.getCode());
            userService.updateUserAuthStatus(userDO);
            return wrapper;
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> saveUserGroup(KeyboxUserServerVO userServerVO) {
        KeyboxUserServerDO userServerDO = new KeyboxUserServerDO();
        userServerDO.setServerGroupId(userServerVO.getServerGroupId());
        userServerDO.setUsername(userServerVO.getUsername());

        try {
            if (userServerDO.getId() == 0) {
                keyboxDao.addUserServer(userServerDO);
            } else {
                keyboxDao.updateUserServer(userServerDO);
            }
            UserDO userDO = userDao.getUserByName(userServerDO.getUsername());
            // 异步变更ldap
            schedulerManager.registerJob(() -> {
                ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(userServerDO.getServerGroupId());
                if (userServerVO.isCiChoose())
                    ciUserGroupService.userAddGroup(userDO.getId(), serverGroupDO.getName());
                //authService.addMemberToGroup(userDO, serverGroupDO);
            });

            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delUserGroup(KeyboxUserServerDO userServerDO) {
        try {
            // userServerDO.getServerGroupId() = 0 则删除所有用户

            // List<ServerGroupDO> list = keyboxDao.getGroupListByUsername(userServerDO.getUsername());
            UserDO userDO = userDao.getUserByName(userServerDO.getUsername());
            // 异步变更ldap
//            schedulerManager.registerJob(() -> {
//                if (userServerDO.getServerGroupId() == 0) {
//                    List<ServerGroupDO> list = keyboxDao.getGroupListByUsername(userServerDO.getUsername());
//                    for (ServerGroupDO serverGroupDO : list) {
//                        authService.delMemberToGroup(userDO, serverGroupDO);
//                    }
//                } else {
//                    ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(userServerDO.getServerGroupId());
//                    authService.delMemberToGroup(userDO, serverGroupDO);
//                }
//            });
            keyboxDao.delUserServer(userServerDO);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> createUserGroupConfigFile(String username) {
        coreLogger.info(SessionUtils.getUsername() + " create user :" + username + " getway config file!");

        HashMap<String, String> configMap = acqConifMap();
        String configFilePath = configMap.get(GetwayItemEnum.GETWAY_USER_CONF_PATH.getItemKey());


        try {
            UserDO userDO = userService.getUserDOByName(username);
            if (userDO == null) {
                return new BusinessWrapper<>(ErrorCode.userNotExist);
            }

            List<ServerGroupDO> groupDOList = keyboxDao.getGroupListByUsername(username);
            if (groupDOList == null || groupDOList.size() == 0) {
                IOUtils.delFile(configFilePath + "/" + username + "/getway.conf");
            } else {
                Getway gw = new Getway(userDO, groupDOList);
                IOUtils.writeFile(gw.toString(), configFilePath + "/" + username + "/getway.conf");
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> createAllUserGroupConfigFile() {
        coreLogger.info("create all user getway config file!");
        HashMap<String, String> configMap = acqConifMap();
        String configFilePath = configMap.get(GetwayItemEnum.GETWAY_USER_CONF_PATH.getItemKey());

        try {
            List<UserDO> users = userDao.getAllUser();
            if (users == null || users.size() == 0) return new BusinessWrapper<>(ErrorCode.serverFailure);
            for (UserDO userDO : users) {
                List<ServerGroupDO> groupDOList = keyboxDao.getGroupListByUsername(userDO.getUsername());
                if (groupDOList == null || groupDOList.size() == 0) continue;
                Getway gw = new Getway(userDO, groupDOList);
                IOUtils.writeFile(gw.toString(), configFilePath + "/" + userDO.getUsername() + "/getway.conf");
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }


    @Override
    public TableVO<List<ServerVO>> getServerList(long serverGroupId, int envType, int page, int length) {
        //List<ServerDO> doList = new ArrayList<>();
        if (authService.isRole(SessionUtils.getUsername(), RoleDO.roleAdmin)) {
            return serverService.getServerPage(serverGroupId, null, 0, envType, null, page, length);
        }
        long size = keyboxDao.getBoxServerSize(SessionUtils.getUsername(), serverGroupId, envType);
        List<ServerDO> doList = keyboxDao.getBoxServerPage(SessionUtils.getUsername(), serverGroupId, envType, page * length, length);
        List<ServerVO> voList = new ArrayList<>();
        for (ServerDO serverDO : doList) {
            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(serverDO.getServerGroupId());

            IPDetailVO publicIP = ipService.getIPDetail(new IPDetailDO(serverDO.getPublicIpId()));
            IPDetailVO insideIP = ipService.getIPDetail(new IPDetailDO(serverDO.getInsideIpId()));

            ServerVO serverVO = new ServerVO(serverDO, serverGroupDO, publicIP, insideIP);
            //插入服务器配置信息
            invokeServerInfo(serverVO);

            voList.add(serverVO);
        }

        return new TableVO<>(size, voList);
    }

    /**
     * 插入服务器配置信息
     *
     * @param serverVO
     */
    @Override
    public void invokeServerInfo(ServerVO serverVO) {
        if (serverVO.getServerType() == ServerDO.ServerTypeEnum.vm.getCode()) {
            VmServerDO vmServerDO = serverDao.queryVmServerByInsideIp(serverVO.getInsideIP().getIp());
            serverVO.setVmServerDO(vmServerDO);
        }

        if (serverVO.getServerType() == ServerDO.ServerTypeEnum.ecs.getCode()) {
            EcsServerDO ecsServerDO = serverDao.queryEcsByInsideIp(serverVO.getInsideIP().getIp());
            serverVO.setEcsServerDO(ecsServerDO);
        }

    }


    @Override
    public BusinessWrapper<Boolean> checkUser() {
        HashMap<String, Boolean> userMap = new HashMap<>();

        List<KeyboxUserServerDO> list = keyboxDao.getKeyboxUserServerAll();
        for (KeyboxUserServerDO keyboxUserServerDO : list) {
            if (!userMap.containsKey(keyboxUserServerDO.getUsername())) {
                UserDO userDO = userDao.getUserByName(keyboxUserServerDO.getUsername());
                if (userDO == null) {
                    userMap.put(keyboxUserServerDO.getUsername(), true);
                } else {
                    userMap.put(keyboxUserServerDO.getUsername(), false);
                }
            }
            if (userMap.get(keyboxUserServerDO.getUsername())) {
                coreLogger.info(SessionUtils.getUsername() + " delete keybox user:" + keyboxUserServerDO.getUsername());
                keyboxDao.delUserServer(keyboxUserServerDO);
            }
        }
        return new BusinessWrapper<>(true);
    }


    /**
     * 堡垒机服务器组用户管理分页数据查询
     *
     * @param serverGorupId
     * @param page
     * @param length
     * @return
     */
    public TableVO<List<KeyboxUserServerVO>> getBoxGroupUserPage(long serverGorupId, int page, int length) {
        int size = keyboxDao.getServerGroupSize(serverGorupId);
        List<KeyboxUserServerDO> list = keyboxDao.getBoxGroupUserPage(serverGorupId, page * length, length);
        List<KeyboxUserServerVO> voList = new ArrayList<KeyboxUserServerVO>();
        for (KeyboxUserServerDO keyboxUserServerDO : list) {
            UserDO userDO = userDao.getUserByName(keyboxUserServerDO.getUsername());
            if (userDO == null) continue;
            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(keyboxUserServerDO.getServerGroupId());
            KeyboxUserServerVO keyboxUserServerVO = new KeyboxUserServerVO(keyboxUserServerDO, serverGroupDO, userDO);
            voList.add(keyboxUserServerVO);
        }
        return new TableVO<>(size, voList);
    }

    /**
     * 删除key file
     *
     * @param username
     */
    private void delKeyFile(String username) {

        HashMap<String, String> configMap = acqConifMap();
        String keyPath = configMap.get(GetwayItemEnum.GETWAY_KEY_PATH.getItemKey());
        String keyFile = configMap.get(GetwayItemEnum.GETWAY_KEY_FILE.getItemKey());

        String path = keyPath + username + keyFile;
        IOUtils.delFile(path);
    }

    @Override
    public ApplicationKeyVO getApplicationKey() {
        ApplicationKeyDO applicationKeyDO = keyboxDao.getApplicationKey();
        if (applicationKeyDO == null) return new ApplicationKeyVO();
        return new ApplicationKeyVO(applicationKeyDO);
    }

    @Override
    public BusinessWrapper<Boolean> saveApplicationKey(ApplicationKeyVO applicationKeyVO) {
        try {
            ApplicationKeyDO applicationKeyDO = keyboxDao.getApplicationKey();
            applicationKeyDO.setPublicKey(applicationKeyVO.getPublicKey());
            // 加密privateKey
            String privateKey = EncryptionUtil.encrypt(applicationKeyVO.getOriginalPrivateKey());
            applicationKeyDO.setPrivateKey(privateKey);
            System.err.println(applicationKeyVO);
            System.err.println(applicationKeyDO);
            keyboxDao.updateApplicationKey(applicationKeyDO);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> getwayStatus(String username, String ip) {
        coreLogger.info("Getway status : Username={};Ip={}", username, ip);

        if (StringUtils.isEmpty(username) && StringUtils.isEmpty(ip)) return new BusinessWrapper<>(false);
        ServerDO serverDO = serverDao.queryServerByInsideIp(ip);
        if (serverDO == null) return new BusinessWrapper<>(ErrorCode.serverNotExist);
        KeyboxLoginStatusDO keyboxStatusDO = new KeyboxLoginStatusDO(serverDO, username, KeyboxLoginStatusDO.LoginTypeEnum.getway.getCode());
        try {
            keyboxDao.addKeyboxStatus(keyboxStatusDO);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public KeyboxStatusVO keyboxStatus() {
        KeyboxStatusVO statusVO = new KeyboxStatusVO();
        List<KeyboxServerVO> topServerList = keyboxDao.statusKeyboxServer();
        statusVO.setTopServerList(topServerList);

        List<KeyboxUserVO> topUserList = keyboxDao.statusKeyboxUser();
        statusVO.setTopUserList(topUserList);

        int authedUserCnt = keyboxDao.getKeyboxAuthedUserCnt();
        statusVO.setAuthedUserCnt(authedUserCnt);

        int keyboxLoginCnt = keyboxDao.getKeyboxLoginCnt();
        statusVO.setKeyboxLoginCnt(keyboxLoginCnt);

        List<KeyboxLoginStatusDO> lastLoginByGetwayListDO = keyboxDao.getLastLogin(KeyboxLoginStatusDO.LoginTypeEnum.getway.getCode());
        List<KeyboxLoginStatusDO> lastLoginByKeyboxListDO = keyboxDao.getLastLogin(KeyboxLoginStatusDO.LoginTypeEnum.keybox.getCode());

        statusVO.setLastLoginByGetway(getLastLogin(lastLoginByGetwayListDO));
        statusVO.setLastLoginByKeybox(getLastLogin(lastLoginByKeyboxListDO));

        statusVO.setKeyboxLoginCharts(keyboxDao.statusKeyboxLoginByCnt(30));

        return statusVO;
    }

    private List<KeyboxLoginStatusVO> getLastLogin(List<KeyboxLoginStatusDO> lastLoginList) {
        List<KeyboxLoginStatusVO> lastLoginListDO = new ArrayList<>();
        for (KeyboxLoginStatusDO keyboxLoginStatusDO : lastLoginList) {
            ServerDO serverDO = serverDao.getServerInfoById(keyboxLoginStatusDO.getServerId());
            UserDO userDO = userDao.getUserByName(keyboxLoginStatusDO.getUsername());
            String timeView = "";
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
                Date createDate = format.parse(keyboxLoginStatusDO.getGmtCreate());
                timeView = TimeViewUtils.format(createDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            KeyboxLoginStatusVO keyboxLoginStatusVO = new KeyboxLoginStatusVO(keyboxLoginStatusDO, userDO, serverDO, timeView);
            lastLoginListDO.add(keyboxLoginStatusVO);
        }
        return lastLoginListDO;
    }

    @Override
    public BusinessWrapper<Boolean> saveKey(String keyPath) {
        ApplicationKeyDO applicationKeyDO = keyboxDao.getApplicationKey();
        if (applicationKeyDO == null) return new BusinessWrapper<>(false);
        try {
            // 解密
            String key = EncryptionUtil.decrypt(applicationKeyDO.getPrivateKey());
            System.err.println(keyPath);
            System.err.println(key);
            IOUtils.writeFile(key, keyPath);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(false);
        }
    }

}
