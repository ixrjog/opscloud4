package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailVO;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.domain.server.serverStatus.ServerCreateByMonthVO;
import com.sdg.cmdb.domain.server.serverStatus.ServerEnvTypeVO;
import com.sdg.cmdb.domain.server.serverStatus.ServerStatusVO;
import com.sdg.cmdb.domain.server.serverStatus.ServerTypeVO;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxxiao on 16/9/6.
 */
@Service
public class ServerServiceImpl implements ServerService {

    private static final Logger logger = LoggerFactory.getLogger(ServerServiceImpl.class);

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private IPService ipService;

    @Resource
    private AuthService authService;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGropuDao;

    @Resource
    private VmService vmService;

    @Resource
    private ConfigService configService;

    @Resource
    private EcsService ecsService;

    @Resource
    private KeyBoxService keyBoxService;

    @Override
    public TableVO<List<ServerVO>> getServerPage(long serverGroupId, String serverName, int useType, int envType, String queryIp, int page, int length) {
        List<Long> groupFilter = authService.getUserGroupIds(SessionUtils.getUsername());
        long size = serverDao.getServerSize(groupFilter, serverGroupId, serverName, useType, envType, queryIp);
        List<ServerDO> list = serverDao.getServerPage(groupFilter, serverGroupId, serverName, useType, envType, queryIp, page * length, length);
        List<ServerVO> voList = new ArrayList<>();
        for (ServerDO serverDO : list) {
            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(serverDO.getServerGroupId());
            IPDetailVO publicIP = ipService.getIPDetail(new IPDetailDO(serverDO.getPublicIpId()));
            IPDetailVO insideIP = ipService.getIPDetail(new IPDetailDO(serverDO.getInsideIpId()));
            ServerVO serverVO = new ServerVO(serverDO, serverGroupDO, publicIP, insideIP);
            ServerGroupUseTypeDO userTypeDO = serverGropuDao.getServerGroupUseTypeByUseType(serverGroupDO.getUseType());
            serverVO.setServerGroupUseTypeDO(userTypeDO);
            keyBoxService.invokeServerInfo(serverVO);
            voList.add(serverVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public ServerVO acqServerVO(ServerDO serverDO){
        ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(serverDO.getServerGroupId());
        IPDetailVO publicIP = ipService.getIPDetail(new IPDetailDO(serverDO.getPublicIpId()));
        IPDetailVO insideIP = ipService.getIPDetail(new IPDetailDO(serverDO.getInsideIpId()));
        ServerVO serverVO = new ServerVO(serverDO, serverGroupDO, publicIP, insideIP);
        return serverVO;
    }

    @Override
    public TableVO<List<ServerVO>> getZabbixServerPage(long serverGroupId, String serverName, int useType, int envType, String queryIp, int zabbixStatus, int zabbixMonitor, String tomcatVersion, int page, int length) {
        List<Long> groupFilter = authService.getUserGroupIds(SessionUtils.getUsername());
        long size = serverDao.getZabbixServerSize(groupFilter, serverGroupId, serverName, useType, envType, queryIp, zabbixStatus, zabbixMonitor, tomcatVersion);
        List<ServerDO> list = serverDao.getZabbixServerPage(groupFilter, serverGroupId, serverName, useType, envType, queryIp, zabbixStatus, zabbixMonitor, tomcatVersion, page * length, length);
        List<ServerVO> voList = new ArrayList<>();
        for (ServerDO serverDO : list) {
            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(serverDO.getServerGroupId());
            IPDetailVO publicIP = ipService.getIPDetail(new IPDetailDO(serverDO.getPublicIpId()));
            IPDetailVO insideIP = ipService.getIPDetail(new IPDetailDO(serverDO.getInsideIpId()));
            ServerVO serverVO = new ServerVO(serverDO, serverGroupDO, publicIP, insideIP);
            voList.add(serverVO);
        }
        return new TableVO<>(size, voList);
    }


    @Override
    public BusinessWrapper<Boolean> saveServer(ServerVO serverVO) {
        try {
            ServerDO serverDO = new ServerDO(serverVO);

            //服务器使用类型跟随服务器组
            serverDO.setUseType(serverVO.getServerGroupDO().getUseType());

            // 若序列号为空则自动增加序列号
            if (StringUtils.isEmpty(serverDO.getSerialNumber())) {
                long cnt = serverDao.getServerSizeByServerGroupIdAndEnvType(serverDO.getServerGroupId(), serverDO.getEnvType());
                serverDO.setSerialNumber(String.valueOf(cnt + 1));
            }

            IPDetailDO publicDO = null;
            IPDetailDO insideDO = null;

            //ip存在,则获取其id,不存在,则创建并建立网关关系后,获取id
            if (serverVO.getPublicIP() != null && !StringUtils.isEmpty(serverVO.getPublicIP().getIp())) {
                publicDO = new IPDetailDO(serverVO.getPublicIP().getIpNetworkDO().getId(), serverVO.getPublicIP().getIp(), IPDetailDO.publicIP);
                IPDetailVO publicVO = ipService.getIPDetail(publicDO);

                if (publicVO != null && publicVO.getId() != 0 && publicVO.getServerId() != serverVO.getId()) {
                    return new BusinessWrapper<>(ErrorCode.ipHasUse.getCode(), "公网ip被使用!");
                }

                if (publicVO == null) {
                    ipService.saveGroupIP(publicDO);
                } else {
                    publicDO.setId(publicVO.getId());
                }
                serverDO.setPublicIpId(publicVO == null ? publicDO.getId() : publicVO.getId());
            }

            if (serverVO.getInsideIP() != null) {
                insideDO = new IPDetailDO(serverVO.getInsideIP().getIpNetworkDO().getId(), serverVO.getInsideIP().getIp(), IPDetailDO.insideIP);
                IPDetailVO insideVO = ipService.getIPDetail(insideDO);

                if (insideVO != null && insideVO.getServerId() != 0 && insideVO.getServerId() != serverVO.getId()) {
                    return new BusinessWrapper<>(ErrorCode.ipHasUse.getCode(), "内网ip被使用!");
                }

                if (insideVO == null) {
                    ipService.saveGroupIP(insideDO);
                } else {
                    insideDO.setId(insideVO.getId());
                }
                serverDO.setInsideIpId(insideVO == null ? insideDO.getId() : insideVO.getId());
            }

            final IPDetailDO finalPublicDO = publicDO;
            final IPDetailDO finalInsideDO = insideDO;

            boolean result = transactionTemplate.execute((TransactionStatus status) -> {
                try {
                    if (serverDO.getId() == 0) {
                        serverDao.addServerGroupServer(serverDO);
                    } else {
                        // 清除server占用的ip
                        ipService.clearServerIP(serverDO.getId());

                        serverDao.updateServerGroupServer(serverDO);
                    }
                    // 标记server占用新的ip
                    if (finalPublicDO != null) {
                        finalPublicDO.setServerId(serverDO.getId());
                        ipService.saveGroupIP(finalPublicDO);
                    }

                    if (finalInsideDO != null) {
                        finalInsideDO.setServerId(serverDO.getId());
                        ipService.saveGroupIP(finalInsideDO);
                    }

                    // 服务器关联
                    if (serverDO.getServerType() == ServerDO.ServerTypeEnum.vm.getCode())
                        vmService.updateVmServerForServer(serverDO);

                    if (serverDO.getServerType() == ServerDO.ServerTypeEnum.ecs.getCode())
                        ecsService.updateEcsServerForServer(serverDO);

                    return true;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    status.setRollbackOnly();
                    return false;
                }
            });
            if (result) {
                // 变更配置文件
                configService.invokeServerConfig(serverDO.getServerGroupId(), serverDO.getEnvType());
                return new BusinessWrapper<>(true);
            } else {
                return new BusinessWrapper<>(ErrorCode.serverFailure);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delServerGroupServer(long id) {
        return transactionTemplate.execute(status -> {
            try {
                ServerDO serverDO = serverDao.getServerInfoById(id);
                serverDao.delServerGroupServer(id);
                //清除server占用的ip
                ipService.clearServerIP(id);
                //清除ecs扩展属性
                ecsService.delEcsProperty(serverDO);
                //变更配置文件
                configService.invokeDelServerConfig(serverDO.getServerGroupId(), serverDO.getEnvType());
                return new BusinessWrapper<>(true);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                status.setRollbackOnly();
                return new BusinessWrapper<>(ErrorCode.serverFailure);
            }
        });
    }

    @Override
    public ServerDO getServerById(long serverId) {
        return serverDao.getServerInfoById(serverId);
    }

    @Override
    public long getGroupServers(long serverGroupId) {
        return serverDao.getServersByGroupId(serverGroupId);
    }


    @Override
    public BusinessWrapper<Boolean> setStatus(String ip) {
        EcsServerDO ecs = serverDao.queryEcsByInsideIp(ip);
        if (ecs != null) {
            ecs.setStatus(EcsServerDO.statusDel);

        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public List<ServerDO> getServersByGroupId(long groupId) {
        return serverDao.acqServersByGroupId(groupId);
    }


    /**
     * 找出envType相同的其余服务器
     *
     * @param serverDOList
     * @return
     */
    @Override
    public List<ServerDO> acqOtherServers(List<ServerDO> serverDOList) {
        if (serverDOList == null || serverDOList.size() == 0) return new ArrayList<ServerDO>();

        // 所有服务器
        List<ServerDO> servers = serverDao.getServersByGroupIdAndEnvType(serverDOList.get(0).getServerGroupId(), serverDOList.get(0).getEnvType());

        for (ServerDO server : serverDOList) {
            for (ServerDO serverDO : servers) {
                if (server.getId() == serverDO.getId()) {
                    servers.remove(serverDO);
                    break;
                }
            }
        }
        return servers;
    }

    @Override
    public BusinessWrapper<Boolean> setUpstream(String ip, String serviceAction) {
        ServerDO serverDO = serverDao.queryServerByInsideIp(ip);
        if (serverDO == null) return new BusinessWrapper<>(true);
        ServerUpstreamDO serverUpstreamDO = serverDao.queryServerUpstreamByIp(ip);
        if (serverUpstreamDO == null) {
            serverUpstreamDO = new ServerUpstreamDO(serverDO, serviceAction, TimeUtils.nowDate());
            serverDao.addServerUpstream(serverUpstreamDO);
        } else {
            serverUpstreamDO.setActionTime(TimeUtils.nowDate());
            serverUpstreamDO.setServiceAction(serviceAction);
            serverDao.updateServerUpstream(serverUpstreamDO);
        }

        return new BusinessWrapper<>(true);
    }

    @Override
    public ServerStatusVO status() {
        ServerStatusVO statusVO = new ServerStatusVO();
        List<ServerEnvTypeVO> serverEnvTypeList = serverDao.statusServerEnvType();
        statusVO.setServerEnvTypeList(serverEnvTypeList);

        List<ServerTypeVO> serverTypeList = serverDao.statusServerType();
        statusVO.setServerTypeList(serverTypeList);

        // 每月新增统计
        List<ServerCreateByMonthVO> serverCreateList = serverDao.statusServerCreateByMonth();
        statusVO.setServerCreateList(serverCreateList);


        return statusVO;
    }


}
