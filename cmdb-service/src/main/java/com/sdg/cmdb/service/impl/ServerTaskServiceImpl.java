package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.AnsibleTaskService;
import com.sdg.cmdb.service.ServerTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/4/7.
 */
@Service
public class ServerTaskServiceImpl implements ServerTaskService {

    private static final Logger logger = LoggerFactory.getLogger(ServerTaskServiceImpl.class);

    @Resource
    private AnsibleTaskService ansibleTaskService;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGropuDao;

    @Override
    public BusinessWrapper<String> initializationSystem(long serverId) {
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        ServerGroupDO serverGroupDO = serverGropuDao.queryServerGroupById(serverDO.getServerGroupId());
        try {
            // ansibleTaskService.taskInitializationSystem(serverDO,serverGroupDO);
            String invokeStr = "" ;
            return new BusinessWrapper<>(invokeStr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

}
