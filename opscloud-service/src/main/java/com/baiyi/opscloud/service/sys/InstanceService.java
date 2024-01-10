package com.baiyi.opscloud.service.sys;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.domain.param.sys.RegisteredInstanceParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/3 1:26 下午
 * @Version 1.0
 */
public interface InstanceService {

    void add(Instance instance);

    void update(Instance instance);

    void deleteById(int id);

    Instance getById(Integer id);

    Instance getByHostIp(String hostIp);

    DataTable<Instance> queryRegisteredInstancePage(RegisteredInstanceParam.RegisteredInstancePageQuery pageQuery);

    List<Instance> listActiveInstance();

}