package com.baiyi.opscloud.facade.sys;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.domain.param.sys.RegisteredInstanceParam;
import com.baiyi.opscloud.domain.vo.sys.InstanceVO;

import java.net.UnknownHostException;

/**
 * @Author baiyi
 * @Date 2021/9/3 1:30 下午
 * @Version 1.0
 */
public interface InstanceFacade {

    boolean isHealth();

    InstanceVO.Health checkHealth();

    Instance getInstance() throws UnknownHostException;

    DataTable<InstanceVO.RegisteredInstance> queryRegisteredInstancePage(RegisteredInstanceParam.RegisteredInstancePageQuery pageQuery);

    void setRegisteredInstanceActive(int id);

}