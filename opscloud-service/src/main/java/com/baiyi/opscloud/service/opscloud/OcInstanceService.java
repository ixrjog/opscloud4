package com.baiyi.opscloud.service.opscloud;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcInstance;
import com.baiyi.opscloud.domain.param.opscloud.OpscloudInstanceParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/24 5:19 下午
 * @Since 1.0
 */
public interface OcInstanceService {

    DataTable<OcInstance> queryOcInstanceByParam(OpscloudInstanceParam.PageQuery pageQuery);

    void addOcInstance(OcInstance ocInstance);

    void updateOcInstance(OcInstance ocInstance);

    OcInstance queryOcInstanceByHostIp(String hostIp);

    OcInstance queryOcInstanceById(int id);

    void delOcInstance(int id);
}
