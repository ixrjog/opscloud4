package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.*;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.LdapItemEnum;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.AuthService;
import com.sdg.cmdb.service.CiUserGroupService;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/8/17.
 */
@Service
public class CiUserGroupServiceImpl implements CiUserGroupService {

    @Resource
    private UserDao userDao;

    @Resource
    private UserService userService;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ConfigCenterService configCenterService;


    @Resource
    private AuthService authService;

    private HashMap<String, String> configMap;

    private HashMap<String, String> acqConfigMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.LDAP.getItemKey());
    }

    @Override
    public TableVO<List<CiUserGroupVO>> getCiUserGroupPage(String groupName, int envType, int page, int length) {
        long size = userDao.getCiUserGroupSize(groupName, envType);
        List<CiUserGroupDO> list = userDao.getCiUserGroupPage(groupName, envType, page * length, length);
        List<CiUserGroupVO> listVO = new ArrayList<CiUserGroupVO>();

        for (CiUserGroupDO ciUserGroupDO : list) {
            CiUserGroupVO ciUserGroupVO;
            if (ciUserGroupDO.getServerGroupId() == 0l) {
                ciUserGroupVO = new CiUserGroupVO(ciUserGroupDO);
            } else {
                ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(ciUserGroupDO.getServerGroupId());
                ciUserGroupVO = new CiUserGroupVO(ciUserGroupDO, serverGroupDO);
            }
            listVO.add(ciUserGroupVO);
        }

        return new TableVO<>(size, listVO);
    }

    @Override
    public BusinessWrapper<Boolean> groupsRefresh() {
        HashMap<String, String> configMap = acqConfigMap();
        // bamboo-
        String groupFilter = configMap.get(LdapItemEnum.LDAP_GROUP_FILTER.getItemKey());
        // group_
        String groupPrefix = configMap.get(LdapItemEnum.LDAP_GROUP_PREFIX.getItemKey());

        // 获取所有的组名称
        List<String> groups = authService.searchBambooGroup();

        for (String bambooGroupName : groups) {
            String serverGroupName = bambooGroupName.replace(groupFilter, groupPrefix);
            ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName(serverGroupName);
            CiUserGroupDO ciUserGroupDO;
            if (serverGroupDO != null) {
                ciUserGroupDO = new CiUserGroupDO(bambooGroupName, serverGroupDO, 0);
            } else {
                ciUserGroupDO = new CiUserGroupDO(bambooGroupName, 0);
            }
            saveCiUserGroup(ciUserGroupDO);
        }

        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> delCigroup(long id) {
        return new BusinessWrapper<>(delCiUserGroup(id));
    }


    /**
     * 保存
     *
     * @param ciUserGroupDO
     * @return
     */
    private boolean saveCiUserGroup(CiUserGroupDO ciUserGroupDO) {
        CiUserGroupDO userGroupDO = userDao.getCiUserGroupByNameAndEnvType(ciUserGroupDO.getGroupName(), ciUserGroupDO.getEnvType());
        if (userGroupDO == null) {
            return addCiUserGroup(ciUserGroupDO);
        } else {
            return updateCiUserGroup(ciUserGroupDO);
        }
    }

    private boolean addCiUserGroup(CiUserGroupDO ciUserGroupDO) {
        try {
            userDao.addCiUserGroup(ciUserGroupDO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean updateCiUserGroup(CiUserGroupDO ciUserGroupDO) {
        try {
            userDao.updateCiUserGroup(ciUserGroupDO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean delCiUserGroup(long id) {
        try {
            userDao.delCiUserGroup(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public TableVO<List<UserVO>> getCiUserPage(String username, int page, int length) {
        long size = userDao.getUserSize(username);
        List<UserDO> list = userDao.getUserPage(username, page * length, length);
        List<UserVO> listVO = new ArrayList<>();

        for (UserDO userDO : list) {
            UserVO userVO = new UserVO(userDO);
            userVO.setCiUsers(getCiUserDO(userDO.getId()));
            listVO.add(userVO);
        }
        return new TableVO<>(size, listVO);
    }

    @Override
    public UserVO getCiUser(String username) {
        UserDO userDO = userDao.getUserByName(username);
        UserVO userVO = new UserVO(userDO);
        userVO.setCiUsers(getCiUserDO(userDO.getId()));
        return userVO;
    }

    private List<CiUserVO> getCiUserDO(long userId) {
        List<CiUserDO> list = userDao.getCiUserByUserId(userId);
        List<CiUserVO> ciUserVOList = new ArrayList<>();

        for (CiUserDO ciUserDO : list) {
            CiUserGroupDO ciUserGroupDO = userDao.getCiUserGroupById(ciUserDO.getUsergroupId());
            CiUserVO ciUserVO = new CiUserVO(ciUserDO, ciUserGroupDO);

            if (ciUserGroupDO != null && ciUserGroupDO.getServerGroupId() != 0) {
                ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(ciUserGroupDO.getServerGroupId());
                if (serverGroupDO != null) {
                    ciUserVO.setServerGroupName(serverGroupDO.getName());
                    if (StringUtils.isEmpty(ciUserVO.getCiUserGroupDO().getContent()) && !StringUtils.isEmpty(serverGroupDO.getContent()))
                        ciUserVO.getCiUserGroupDO().setContent(serverGroupDO.getContent());
                }
            }
            ciUserVOList.add(ciUserVO);
        }
        return ciUserVOList;
    }

    @Override
    public BusinessWrapper<Boolean> usersRefresh() {
        List<UserDO> users = userDao.getAllUser();
        for (UserDO userDO : users) {
            UserVO userVO = new UserVO(userDO);
            userVO.setLdapGroups(authService.searchLdapGroup(userVO.getUsername()));
            userService.invokeLdapGroups(userVO);
            invokeUser(userVO);
        }
        return new BusinessWrapper<>(true);
    }

    private void invokeUser(UserVO userVO) {
        List<LdapGroupVO> bambooLdapGroups = userVO.getBambooLdapGroups();
        if (bambooLdapGroups == null) {
            System.err.println("user null:" + userVO.getUsername());
            return;
        }

        for (LdapGroupVO ldapGroupVO : bambooLdapGroups) {
            CiUserGroupDO ciUserGroupDO = userDao.getCiUserGroupByNameAndEnvType(ldapGroupVO.getName(), 0);
            if (ciUserGroupDO == null) continue;
            CiUserDO ciUserDO = new CiUserDO(userVO.getId(), ciUserGroupDO.getId());
            addCiUser(ciUserDO);
        }

    }


    private boolean addCiUser(CiUserDO ciUserDO) {
        try {
            CiUserDO userDO = userDao.getCiUserByUserIdAndusergroupId(ciUserDO.getUserId(), ciUserDO.getUsergroupId());
            if (userDO == null) {
                userDao.addCiUser(ciUserDO);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public BusinessWrapper<Boolean> saveCiUserGroup(CiUserGroupVO ciUserGroupVO) {
        try {
            CiUserGroupDO ciUserGroupDO = userDao.getCiUserGroupByNameAndEnvType(ciUserGroupVO.getGroupName(), ciUserGroupVO.getEnvType());
            if (ciUserGroupDO == null) {
                ciUserGroupDO = new CiUserGroupDO(ciUserGroupVO);
                userDao.addCiUserGroup(ciUserGroupDO);
            } else {
                ciUserGroupDO.setServerGroupId(ciUserGroupVO.getServerGroupDO().getId());
                ciUserGroupDO.setContent(ciUserGroupVO.getContent());
                ciUserGroupDO.setEnvType(ciUserGroupVO.getEnvType());
                userDao.updateCiUserGroup(ciUserGroupDO);
            }
        } catch (Exception e) {
            return new BusinessWrapper<>(false);
        }
        return new BusinessWrapper<>(true);
    }

    public boolean userAddGroup(long userId, String serverGroupName) {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName(serverGroupName);
        if (serverGroupDO == null) return false;

        CiUserGroupDO ciUserGroupDO = userDao.getCiUserGroupByServerGroupIdAndEnvType(serverGroupDO.getId(), CiUserGroupVO.EnvTypeEnum.all.getCode());
        if (ciUserGroupDO == null) return false;

        BusinessWrapper<Boolean> wrapper = userAddGroup(userId, ciUserGroupDO.getId());
        if (wrapper.isSuccess())
            return true;
        return false;
    }


    @Override
    public BusinessWrapper<Boolean> userAddGroup(long userId, long usergroupId) {
        CiUserGroupDO ciUserGroupDO = userDao.getCiUserGroupById(usergroupId);
        if (ciUserGroupDO == null) return new BusinessWrapper<>(false);
        //ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(ciUserGroupDO.getServerGroupId());
        UserDO userDO = userDao.getUserById(userId);
        if (userDO == null) return new BusinessWrapper<>(false);
        CiUserDO ciUserDO = new CiUserDO(userId, usergroupId);
        try {
            // 持续集成权限组
            authService.addMemberToGroup(userDO, ciUserGroupDO.getGroupName());
            userDao.addCiUser(ciUserDO);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> userDelGroup(long ciuserId) {
        try {
            CiUserDO ciUserDO = userDao.getCiUserById(ciuserId);
            CiUserGroupDO ciUserGroupDO = userDao.getCiUserGroupById(ciUserDO.getUsergroupId());
            //ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(ciUserGroupDO.getServerGroupId());
            UserDO userDO = userDao.getUserById(ciUserDO.getUserId());
            // 持续集成权限组
            authService.delMemberToGroup(userDO, ciUserGroupDO.getGroupName());
            userDao.delCiUser(ciuserId);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(false);
        }
    }

}
