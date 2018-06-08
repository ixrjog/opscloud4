package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailQuery;
import com.sdg.cmdb.domain.ip.IPDetailVO;

import java.util.List;

/**
 * Created by zxxiao on 16/9/11.
 */
public interface IPService {

    /**
     * 查询指定条件的ip分页数据
     * @param detailQuery
     * @param page
     * @param length
     * @return
     */
    TableVO<List<IPDetailVO>> getIPDetailPage(IPDetailQuery detailQuery, int page, int length);

    /**
     * 新增 or 更新ip信息
     * @param ipDetailDO
     * @return
     */
    BusinessWrapper<Boolean> saveGroupIP(IPDetailDO ipDetailDO);

    /**
     * 删除指定id的ip信息
     * @param ipId
     * @return
     */
    BusinessWrapper<Boolean> delGroupIP(long ipId);

    /**
     * 保存IP组
     * @param list
     * @return
     */
    BusinessWrapper<Integer> saveGroupIPs(List<IPDetailDO> list);

    /**
     * 检查指定段的ip是否被使用
     * @param groupId
     * @param ip
     * @param serverId
     * @return
     */
    BusinessWrapper<Boolean> checkIPHasUse(long groupId, String ip, long serverId);

    /**
     * 获取指定ipId的详情
     * @param detailDO
     * @return
     */
    IPDetailVO getIPDetail(IPDetailDO detailDO);

    /**
     * 清除指定server占用的ip
     * @param serverId
     */
    void clearServerIP(long serverId);


    /**
     * 查询服务器的所有ip地址
     * @param serverId
     * @return
     */
    List<IPDetailDO> getAllServerIP(long serverId);

}
