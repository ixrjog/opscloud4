package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.domain.server.serverStatus.ServerStatusVO;

import java.util.List;

/**
 * Created by zxxiao on 16/9/6.
 */
public interface ServerService {

    /**
     * 获取服务器分页数据
     *
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param queryIp
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ServerVO>> getServerPage(long serverGroupId, String serverName, int useType, int envType, String queryIp, int page, int length);


    ServerVO acqServerVO(ServerDO serverDO);
    /**
     * 获取服务器分页数据
     *
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param queryIp
     * @param zabbixStatus
     * @param zabbixMonitor
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ServerVO>> getZabbixServerPage(long serverGroupId, String serverName, int useType, int envType, String queryIp, int zabbixStatus, int zabbixMonitor, String tomcatVersion,int page, int length);


    /**
     * 保存服务器组的指定服务器信息
     *
     * @param serverVO
     * @return
     */
    BusinessWrapper<Boolean> saveServer(ServerVO serverVO);

    /**
     * 删除指定servergroup 的 server信息
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> delServerGroupServer(long id);

    /**
     * 获取指定的serverid信息
     *
     * @param serverId
     * @return
     */
    ServerDO getServerById(long serverId);

    /**
     * 获取指定服务器组下的服务器数目
     *
     * @param serverGroupId
     * @return
     */
    long getGroupServers(long serverGroupId);


    /**
     * 标记删除
     *
     * @param insideIp
     * @return
     */
    BusinessWrapper<Boolean> setStatus(String insideIp);

    /**
     * 获取指定服务器组的服务器集合
     * @param groupId
     * @return
     */
    List<ServerDO> getServersByGroupId(long groupId);


    /**
     * 找出envType相同的其余服务器
     * @param serverDOList
     * @return
     */
    List<ServerDO> acqOtherServers(List<ServerDO> serverDOList);

    /**
     * 设置nginx upstream
     * @param ip
     * @param serviceAction
     * @return
     */
    BusinessWrapper<Boolean> setUpstream(String ip,String serviceAction);

    /**
     * 首页服务器统计信息
     * @return
     */
    ServerStatusVO status();

}
