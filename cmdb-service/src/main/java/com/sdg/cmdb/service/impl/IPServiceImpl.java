package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.IPDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailQuery;
import com.sdg.cmdb.domain.ip.IPDetailVO;
import com.sdg.cmdb.domain.ip.IPNetworkDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.service.IPGroupService;
import com.sdg.cmdb.service.IPService;
import com.sdg.cmdb.service.ServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxxiao on 16/9/11.
 */
@Service
public class IPServiceImpl implements IPService {

    private static final Logger logger = LoggerFactory.getLogger(IPServiceImpl.class);

    @Resource
    private IPGroupService ipGroupService;

    @Resource
    private ServerService serverService;

    @Resource
    private IPDao ipDao;

    @Override
    public TableVO<List<IPDetailVO>> getIPDetailPage(IPDetailQuery detailQuery, int page, int length) {
        long size = ipDao.queryIPSize(detailQuery);
        List<IPDetailDO> list = ipDao.queryIPPage(detailQuery, page * length, length);
        List<IPDetailVO> voList = new ArrayList<>();
        for (IPDetailDO ipDetailDO : list) {
            IPNetworkDO ipNetworkDO = ipGroupService.queryIPGroupById(ipDetailDO.getIpNetworkId());

            ServerDO serverDO = serverService.getServerById(ipDetailDO.getServerId());
            String serverName;
            if (serverDO == null) {
                serverName = "server不存在!";
            } else {
                serverName = serverDO.getServerName();
            }
            IPDetailVO detailVO = new IPDetailVO(ipDetailDO, ipNetworkDO, serverName);
            voList.add(detailVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public BusinessWrapper<Boolean> saveGroupIP(IPDetailDO ipDetailDO) {
        try {
            if (ipDetailDO.getId() <= 0) {
                ipDao.addIP(ipDetailDO);
            } else {
                ipDao.updateIP(ipDetailDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delGroupIP(long ipId) {
        try {
            ipDao.delIP(ipId);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Integer> saveGroupIPs(List<IPDetailDO> list) {
        try {
            if (list.isEmpty()) {
                return new BusinessWrapper<>(0);
            }
            int nums = ipDao.saveIPList(list);
            return new BusinessWrapper<>(nums);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> checkIPHasUse(long groupId, String ip, long serverId) {
        int nums = ipDao.checkIPHasUse(groupId, ip, serverId);
        if (nums == 0) {
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.ipHasUse);
        }
    }

    @Override
    public IPDetailVO getIPDetail(IPDetailDO detailDO) {
        IPDetailDO ipDetailDO = ipDao.getIPDetail(detailDO);

        if (ipDetailDO == null) {
            return null;
        }

        IPNetworkDO ipNetworkDO = ipGroupService.queryIPGroupById(ipDetailDO.getIpNetworkId());

        ServerDO serverDO = serverService.getServerById(ipDetailDO.getServerId());
        String serverName;
        if (serverDO == null) {
            serverName = "server不存在!";
        } else {
            serverName = serverDO.getServerName();
        }
        return new IPDetailVO(ipDetailDO, ipNetworkDO, serverName);
    }

    @Override
    public void clearServerIP(long serverId) {
        ipDao.clearServerIP(serverId);
    }

    @Override
    public List<IPDetailDO> getAllServerIP(long serverId) {
        return ipDao.getAllServerIP(serverId);
    }

}
