package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/9/11.
 */
@Component
public interface IPDao {

    /**
     * 查询指定条件的ip数目
     * @param detailQuery
     * @return
     */
    long queryIPSize(IPDetailQuery detailQuery);

    /**
     * 查询指定条件的ip分页数据
     * @param detailQuery
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<IPDetailDO> queryIPPage(
            @Param("detailQuery")IPDetailQuery detailQuery,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 批量保存指定ip组的ip集合
     * @param list
     * @return
     */
    int saveIPList(@Param("list")List<IPDetailDO> list);

    /**
     * 新增ip信息
     * @param ipDetailDO
     * @return
     */
    int addIP(IPDetailDO ipDetailDO);

    /**
     * 更新指定的ip信息
     * @param ipDetailDO
     * @return
     */
    int updateIP(IPDetailDO ipDetailDO);

    /**
     * 删除指定的ip信息
     * @param id
     * @return
     */
    int delIP(@Param("id") long id);

    /**
     * 检查指定段的ip是否被使用
     * @param groupId
     * @param ip
     * @param serverId
     * @return
     */
    int checkIPHasUse(@Param("groupId") long groupId, @Param("ip") String ip, @Param("serverId") long serverId);

    /**
     * 获取指定ipId的详情
     * @param detailDO
     * @return
     */
    IPDetailDO getIPDetail(IPDetailDO detailDO);

    /**
     * 清除指定server占用的ip
     * @param serverId
     * @return
     */
    void clearServerIP(@Param("serverId") long serverId);

    /**
     * 查询服务器的所有ip
     * @param serverId
     * @return
     */
    List<IPDetailDO> getAllServerIP(@Param("serverId") long serverId);


}
