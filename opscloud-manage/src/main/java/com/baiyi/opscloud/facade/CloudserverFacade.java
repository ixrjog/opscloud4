package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloudserver.CloudserverParam;
import com.baiyi.opscloud.domain.vo.cloudserver.OcCloudserverVO;

/**
 * @Author baiyi
 * @Date 2020/2/17 9:39 上午
 * @Version 1.0
 */
public interface CloudserverFacade {

    DataTable<OcCloudserverVO.OcCloudserver> queryCloudserverPage(CloudserverParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> deleteCloudserverById(int id);

    BusinessWrapper<Boolean> syncCloudserverByKey(String key);

}
