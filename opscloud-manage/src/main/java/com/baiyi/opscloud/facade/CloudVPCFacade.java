package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudVPCVO;

/**
 * @Author baiyi
 * @Date 2020/3/18 7:02 下午
 * @Version 1.0
 */
public interface CloudVPCFacade {

    DataTable<OcCloudVPCVO.CloudVpc> fuzzyQueryCloudVPCPage(CloudVPCParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> syncCloudVPCByKey(String key);

    BusinessWrapper<Boolean> deleteCloudVPCById(int id);
}
