package com.sdg.cmdb.service.configurationProcessor;

import com.sdg.cmdb.dao.cmdb.*;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.ConfigServerGroupService;
import com.sdg.cmdb.service.ConfigService;
import org.apache.commons.lang3.time.FastDateFormat;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class ConfigurationProcessorAbs {

    @Resource
    protected ServerDao serverDao;

    @Resource
    protected ServerGroupDao serverGroupDao;

    @Resource
    protected ConfigDao configDao;

    @Resource
    protected UserDao userDao;

    @Resource
    protected ConfigServerGroupService configServerGroupService;

    @Resource
    protected ConfigService configService;


    @Resource
    protected NginxDao nginxDao;



    /**
     * 文件头信息
     * protected String headInfo
     * @return
     */
    protected String getHeadInfo() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        String headInfo = "# Created by opsCloud on " + fastDateFormat.format(new Date()) + "\n\n";
        return headInfo;
    }

    protected String acqHostLine(ServerDO serverDO) {
        configServerGroupService.invokeGetwayIp(serverDO);
        return serverDO.getInsideIp() + " ansible_ssh_user=" + serverDO.getLoginUser() + " # " + serverDO.getSerialNumber() + "\n";
    }


    /**
     * 取所有的webservice组信息
     */
    protected List<ServerGroupDO> acqServerGroupByWebservice() {
        List<ServerGroupDO> list = serverGroupDao.queryServerGroup();
        List<ServerGroupDO> result = new ArrayList<ServerGroupDO>();
        for (ServerGroupDO serverGroupDO : list) {
            if (serverGroupDO.getUseType() == ServerGroupDO.UseTypeEnum.webservice.getCode())
                result.add(serverGroupDO);
        }
        return result;
    }

    /**
     * 按环境类型取服务器
     *
     * @param serverGroupDO
     * @param envType
     * @return
     */
    protected List<ServerDO> acqServerByGroup(ServerGroupDO serverGroupDO, int envType) {
        List<ServerDO> list = serverDao.acqServersByGroupId(serverGroupDO.getId());
        List<ServerDO> result = new ArrayList<ServerDO>();
        for (ServerDO serverDO : list) {
            if (serverDO.getEnvType() == envType)
                result.add(serverDO);
        }
        return result;
    }

    protected List<ServerDO> acqServerByGroup(ServerGroupDO serverGroupDO) {
        List<ServerDO> list = serverDao.acqServersByGroupId(serverGroupDO.getId());
        return list;
    }




    protected List<ServerGroupDO> getServerGroup() {
        List<ServerGroupDO> list = acqServerGroupByWebservice();
        return list;
    }


}
