package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;

/**
 * Created by liangjian on 2017/4/7.
 */
public interface ServerTaskService {


    /**
     * 初始化系统
     * @param serverId
     * @return
     */
    BusinessWrapper<String> initializationSystem(long serverId);

}
