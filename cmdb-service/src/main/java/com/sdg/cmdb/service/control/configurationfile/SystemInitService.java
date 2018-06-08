package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.server.ServerDO;

/**
 * Created by liangjian on 2017/7/27.
 */
public interface SystemInitService {

    /**
     * 获取系统主机配置文件
     *
     * @param serverDO
     * @return
     */
    String acqSystemInitHostConfig(ServerDO serverDO);


}
