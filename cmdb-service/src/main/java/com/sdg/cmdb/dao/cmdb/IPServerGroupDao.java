package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.ip.IPNetworkDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/9/14.
 */
@Component
public interface IPServerGroupDao {
    /**
     * 获取指定serverGroupId关联的networkId集合
     * @param serverGroupId
     * @return
     */
    long queryIPGroupByServerGroupSize(@Param("serverGroupId") long serverGroupId);

    /**
     * 获取指定serverGroupId关联的networkId集合分页数据
     * @param serverGroupId
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<IPNetworkDO> queryIPGroupByServerGroupPage(
            @Param("serverGroupId") long serverGroupId, @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 清除指定serverGroupId的Network关系
     * @param serverGroupId
     * @return
     */
    int delServerIPGroups(@Param("serverGroupId") long serverGroupId);

    /**
     * 保存serverGroup&networkId的关系
     * @param list
     * @return
     */
    int addServerIPGroups(@Param("list") List<IPNetworkDO> list);
}
