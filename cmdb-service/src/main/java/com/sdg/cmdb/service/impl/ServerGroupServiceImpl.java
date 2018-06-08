package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.KeyboxDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.server.ServerGroupUseTypeDO;
import com.sdg.cmdb.domain.server.ServerGroupVO;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxxiao on 16/9/1.
 */
@Service
public class ServerGroupServiceImpl implements ServerGroupService {

    private static final Logger logger = LoggerFactory.getLogger(ServerGroupServiceImpl.class);

    @Resource
    private AuthService authService;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ServerService serverService;

    @Resource
    private ConfigService configService;

    @Resource
    private KeyBoxService keyBoxService;

    @Resource
    private KeyboxDao keyboxDao;

    @Resource
    private ServerDao serverDao;

    @Resource
    private UserDao userDao;

    @Override
    public TableVO<List<ServerGroupVO>> queryServerGroupPage(int page, int length, String name, int useType) {
        List<String> filterGroups = authService.getUserGroup(SessionUtils.getUsername());

        long size = serverGroupDao.queryServerGroupSize(filterGroups, name, useType);
        List<ServerGroupDO> list = serverGroupDao.queryServerGroupPage(filterGroups, page * length, length, name, useType);

        List<ServerGroupVO> groupVOList = acqServerGroupVOList(list);

        return new TableVO<>(size, groupVOList);
    }


    @Override
    public TableVO<List<ServerGroupVO>> queryProjectServerGroupPage(int page, int length, String name, int useType) {
        long size = serverGroupDao.queryProjectServerGroupSize(name, useType);
        List<ServerGroupDO> list = serverGroupDao.queryProjectServerGroupPage(page * length, length, name, useType);

        List<ServerGroupVO> groupVOList = acqServerGroupVOList(list);
//        for (ServerGroupDO groupDO : list) {
//            ServerGroupVO groupVO = new ServerGroupVO(groupDO, configService.getPropertyGroupByGroupId(groupDO.getId()));
//            //设置服务器组的服务器数量
//            groupVO.setServerCnt(serverDao.getServersByGroupId(groupDO.getId()));
//            //设置服务器组的堡垒机用户数量
//            groupVO.setKeyboxCnt(keyboxDao.getServerGroupSize(groupDO.getId()));
//            groupVOList.add(groupVO);
//        }

        return new TableVO<>(size, groupVOList);
    }

    private List<ServerGroupVO> acqServerGroupVOList(List<ServerGroupDO> list) {
        List<ServerGroupVO> groupVOList = new ArrayList<>();
        for (ServerGroupDO groupDO : list) {
            ServerGroupVO groupVO = new ServerGroupVO(groupDO, configService.getPropertyGroupByGroupId(groupDO.getId()));
            //设置服务器组的服务器数量
            groupVO.setServerCnt(serverDao.getServersByGroupId(groupDO.getId()));
            //设置服务器组的堡垒机用户数量
            groupVO.setKeyboxCnt(keyboxDao.getServerGroupSize(groupDO.getId()));
            ServerGroupUseTypeDO useTypeDO = getUseType(groupDO.getUseType());
            groupVO.setServerGroupUseTypeDO(useTypeDO);
            groupVOList.add(groupVO);
        }
        return groupVOList;
    }

    @Override
    public TableVO<List<ServerGroupVO>> queryKeyboxServerGroupPage(String name, int page, int length) {
        List<String> filterGroups = authService.getUserGroup(SessionUtils.getUsername());
        long size = serverGroupDao.queryServerGroupSize(filterGroups, name, 0);
        List<ServerGroupVO> list = serverGroupDao.queryKeyboxServerGroupPage(name, page * length, length);
        HashMap<String, String> userMap = new HashMap<>();
        for (ServerGroupVO groupVO : list) {
            groupVO.setServerCnt(serverDao.getServersByGroupId(groupVO.getId()));
            List<KeyboxUserServerDO> keyboxUserList = keyboxDao.getUserServerByGroupId(groupVO.getId());
            List<String> users = new ArrayList<>();
            for (KeyboxUserServerDO user : keyboxUserList) {
                if (!userMap.containsKey(user.getUsername())) {
                    String displayName = userDao.getDisplayNameByUserName(user.getUsername());
                    userMap.put(user.getUsername(), displayName);
                }
                users.add(user.getUsername() + "<" + userMap.get(user.getUsername()) + ">");
            }
            groupVO.setUsers(users);
        }
        return new TableVO<>(size, list);
    }


    @Override
    public boolean updateServerGroupInfo(ServerGroupDO serverGroupDO) {
        try {
            if (serverGroupDO.getId() == 0) {
                serverGroupDao.addServerGroupInfo(serverGroupDO);
            } else {
                serverGroupDao.updateServerGroupInfo(serverGroupDO);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public BusinessWrapper<Boolean> delServerGroupInfo(long id) {
        try {
            //判断是否还有服务器&是否有堡垒机依赖
            KeyboxUserServerDO userServerDO = new KeyboxUserServerDO();
            userServerDO.setServerGroupId(id);

            long groupServers = serverService.getGroupServers(id);
            long userServers = keyBoxService.getUserServerSize(userServerDO);

            if (groupServers != 0) {
                return new BusinessWrapper<>(ErrorCode.serverGroupHasServer);
            } else if (userServers != 0) {
                return new BusinessWrapper<>(ErrorCode.serverGroupHasUser);
            } else {
                serverGroupDao.delServerGroupInfo(id);
                return new BusinessWrapper<>(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> addIPGroup(long serverGroupId, long ipGroupId, int ipType) {
        try {
            serverGroupDao.addServerIPGroup(serverGroupId, ipGroupId, ipType);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delIPGroup(long serverGroupId, long ipGroupId) {
        try {
            serverGroupDao.delServerIPGroup(serverGroupId, ipGroupId);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public ServerGroupDO queryServerGroupById(long serverGroupId) {
        return serverGroupDao.queryServerGroupById(serverGroupId);
    }

    @Override
    public ServerGroupDO queryServerGroupByName(String serverName) {
        return serverGroupDao.queryServerGroupByName(serverName);
    }

    @Override
    public List<ServerGroupDO> getServerGroupsByUsername(String username) {
        return serverGroupDao.getGroupsByName(username);
    }


    @Override
    public TableVO<List<ServerGroupUseTypeDO>> queryServerGroupUseTypePage(String typeName, int page, int length) {
        long size = serverGroupDao.queryUseTypeSize(typeName);
        List<ServerGroupUseTypeDO> list = serverGroupDao.queryUseTypePage(typeName, page * length, length);
        return new TableVO<>(size, list);
    }


    @Override
    public List<ServerGroupUseTypeDO> queryServerGroupUseType() {
        List<ServerGroupUseTypeDO> list = serverGroupDao.queryUseTypePage("", 0, 100);
        return list;
    }

    @Override
    public BusinessWrapper<Boolean> saveServerGroupUseType(ServerGroupUseTypeDO serverGroupUseTypeDO) {
        if (serverGroupUseTypeDO.getId() == 0) {
            try {
                serverGroupDao.addServerGroupUseType(serverGroupUseTypeDO);
            } catch (Exception e) {
                e.printStackTrace();
                return new BusinessWrapper<Boolean>(false);
            }
        } else {
            try {
                serverGroupDao.updateServerGroupUseType(serverGroupUseTypeDO);
            } catch (Exception e) {
                e.printStackTrace();
                return new BusinessWrapper<Boolean>(false);
            }
        }
        return new BusinessWrapper<Boolean>(true);
    }


    @Override
    public BusinessWrapper<Boolean> delServerGroupUseType(long id) {
        try {
            serverGroupDao.delServerGroupUseType(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }


    private ServerGroupUseTypeDO getUseType(int useType) {
        ServerGroupUseTypeDO useTypeDO = serverGroupDao.getServerGroupUseTypeByUseType(useType);
        if (useTypeDO == null)
            return new ServerGroupUseTypeDO();
        return useTypeDO;
    }
}
