package com.sdg.cmdb.service;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceResponse;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.server.CreateEcsVO;
import com.sdg.cmdb.domain.server.EcsServerDO;
import com.sdg.cmdb.domain.server.EcsServerVO;

import java.util.List;

/**
 * Created by liangjian on 2017/4/18.
 */
public interface EcsCreateService {


    /**
     * 创建ecs实例
     *
     * @param regionId
     * @return
     */
    CreateInstanceResponse createEcs(String regionId, CreateEcsVO template);

    /**
     * 新增
     * @param regionId
     * @param template
     * @return
     */
    HttpResult create(String regionId, CreateEcsVO template);


    boolean queryImages(String regionId, String imageName);

    /**
     * web调用分配公网ip
     * @param instanceId
     * @return
     */
    BusinessWrapper<Boolean> allocateIpAddress(String instanceId);

}
