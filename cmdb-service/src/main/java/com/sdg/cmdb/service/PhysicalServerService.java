package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.server.PhysicalServerVO;
import com.sdg.cmdb.domain.server.ServerStatisticsDO;

import java.util.List;

/**
 * Created by liangjian on 2017/2/13.
 */

public interface PhysicalServerService {

    /**
     * 获取物理服务器分页数据
     * @param serverName
     * @param useType
     * @param page
     * @param length
     * @return
     */
    TableVO<List<PhysicalServerVO>> getPhysicalServerPage(String serverName, int useType, int page, int length);

    /**
     * 统计
     * @return
     */
    ServerStatisticsDO statistics();


}
