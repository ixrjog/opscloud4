package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.server.ServerCostVO;
import com.sdg.cmdb.domain.server.ServerStatisticsDO;
import com.sdg.cmdb.domain.server.ServerVO;


import java.util.List;

/**
 * Created by liangjian on 2017/2/23.
 */
public interface ServerCostService {

    /**
     * 获取服务器成本数据
     * @param year
     * @param month
     * @return
     */
    List<ServerCostVO> getServerPage(int year, int month);

    /**
     * 统计
     * @return
     */
    ServerStatisticsDO statistics(int year, int month);


}
