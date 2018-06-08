package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ip.IPGroupSearchVO;
import com.sdg.cmdb.domain.ip.IPNetworkDO;

import java.util.List;

/**
 * Created by zxxiao on 16/9/9.
 */
public interface IPGroupService {
    /**
     * 获取指定条件的分页数据
     * @param searchVO
     * @param page
     * @param length
     * @return
     */
    TableVO<List<IPNetworkDO>> getIPGroupPage(IPGroupSearchVO searchVO, int page, int length);

    /**
     * 保存 or 更新IP组信息
     * @param ipNetworkDO
     * @return
     */
    BusinessWrapper<Boolean> saveIPGroupInfo(IPNetworkDO ipNetworkDO);

    /**
     * 删除指定的IP组信息
     * @param ipGroupId
     * @return
     */
    boolean delIPGroupInfo(long ipGroupId);

    /**
     * 创建ip集合
     * @param ipGroupId
     * @return
     */
    BusinessWrapper createIp(long ipGroupId);

    /**
     * 查询指定id的IP组信息
     * @param ipGroupId
     * @return
     */
    IPNetworkDO queryIPGroupById(long ipGroupId);

    /**
     * 查询指定服务器组id的IP组集合信息
     * @param serverGroupId
     * @param page
     * @param length
     * @return
     */
    TableVO<List<IPNetworkDO>> queryIPGroupByServerGroupId(long serverGroupId, int page, int length);
}
