package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.ip.IPGroupSearchVO;
import com.sdg.cmdb.domain.ip.IPNetworkDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/9/9.
 */
@Component
public interface IPGroupDao {

    /**
     * 查询指定条件下的IP组信息数目
     * @param searchVO
     * @return
     */
    long queryIpGroupSize(IPGroupSearchVO searchVO);

    /**
     * 查询指定条件下的IP组信息分页数据
     * @param searchVO
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<IPNetworkDO> queryIpGroupPage(
            @Param("searchVO")IPGroupSearchVO searchVO, @Param("pageStart")long pageStart, @Param("pageLength") int pageLength);

    /**
     * 新增IP组信息
     * @param ipNetworkDO
     * @return
     */
    int addIPGroup(IPNetworkDO ipNetworkDO);

    /**
     * 更新指定IP组信息
     * @param ipNetworkDO
     * @return
     */
    int updateIPGroup(IPNetworkDO ipNetworkDO);

    /**
     * 删除指定的IP组信息
     * @param id
     * @return
     */
    int delIPGroup(@Param("id") long id);

    /**
     * 查询指定id的ipGroup信息
     * @param id
     * @return
     */
    IPNetworkDO queryIPGroupInfo(@Param("id") long id);
}
