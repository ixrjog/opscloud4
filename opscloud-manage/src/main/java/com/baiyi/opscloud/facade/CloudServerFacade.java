package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloudserver.CloudServerParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudServerVO;

/**
 * @Author baiyi
 * @Date 2020/2/17 9:39 上午
 * @Version 1.0
 */
public interface CloudServerFacade {

    DataTable<OcCloudServerVO.OcCloudServer> queryCloudServerPage(CloudServerParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> deleteCloudServerById(int id);

    BusinessWrapper<Boolean> syncCloudServerByKey(String key);

}
