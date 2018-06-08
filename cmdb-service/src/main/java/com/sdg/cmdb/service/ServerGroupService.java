package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.server.ServerGroupUseTypeDO;
import com.sdg.cmdb.domain.server.ServerGroupVO;

import java.util.List;

/**
 * Created by zxxiao on 16/9/1.
 */
public interface ServerGroupService {

    /**
     * 查询服务器组分页信息
     *
     * @param page
     * @param length
     * @param name
     * @param useType
     * @return
     */
    TableVO<List<ServerGroupVO>> queryServerGroupPage(int page, int length, String name, int useType);

    /**
     * 查询项目管理服务器组分页信息
     *
     * @param page
     * @param length
     * @param name
     * @param useType
     * @return
     */
    TableVO<List<ServerGroupVO>> queryProjectServerGroupPage(int page, int length, String name, int useType);

    /**
     * 堡垒机服务器组分页数据查询
     *
     * @param name
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ServerGroupVO>> queryKeyboxServerGroupPage(String name, int page, int length);

    /**
     * 更新服务器组信息
     *
     * @param serverGroupDO
     * @return
     */
    boolean updateServerGroupInfo(ServerGroupDO serverGroupDO);

    /**
     * 删除服务器信息
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> delServerGroupInfo(long id);

    /**
     * 服务器组绑定指定的ip组
     *
     * @param serverGroupId
     * @param ipGroupId
     * @param ipType
     * @return
     */
    BusinessWrapper<Boolean> addIPGroup(long serverGroupId, long ipGroupId, int ipType);

    /**
     * 服务器组解绑指定的ip组
     *
     * @param serverGroupId
     * @param ipGroupId
     * @return
     */
    BusinessWrapper<Boolean> delIPGroup(long serverGroupId, long ipGroupId);

    /**
     * 获取指定id的group
     *
     * @param serverGroupId
     * @return
     */
    ServerGroupDO queryServerGroupById(long serverGroupId);

    /**
     * 获取指定name的group
     *
     * @param serverName
     * @return
     */
    ServerGroupDO queryServerGroupByName(String serverName);

    /**
     * 获取指定用户名关联的服务器组列表
     *
     * @param username
     * @return
     */
    List<ServerGroupDO> getServerGroupsByUsername(String username);


    /**
     * 查询服务器组使用类型分页信息
     *
     * @param typeName
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ServerGroupUseTypeDO>> queryServerGroupUseTypePage(String typeName, int page, int length);

    /**
     * 查询服务器组使用类型列表
     * @return
     */
    List<ServerGroupUseTypeDO> queryServerGroupUseType();

    /**
     * 保存服务器组使用类型
     *
     * @param serverGroupUseTypeDO
     * @return
     */
    BusinessWrapper<Boolean> saveServerGroupUseType(ServerGroupUseTypeDO serverGroupUseTypeDO);


    BusinessWrapper<Boolean> delServerGroupUseType(long id);
}
