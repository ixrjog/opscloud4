package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.CloudServerParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudServerVO;

/**
 * @Author baiyi
 * @Date 2020/2/17 9:39 上午
 * @Version 1.0
 */
public interface CloudServerFacade {

    DataTable<CloudServerVO.CloudServer> queryCloudServerPage(CloudServerParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> deleteCloudServerById(int id);

    void syncCloudServerByKey(String key);

    void updateCloudServerStatus(int id, int serverId, int cloudServerStatus);

    BusinessWrapper<Boolean> deleteCloudServer(CloudServerParam.DeleteInstance param);
}
