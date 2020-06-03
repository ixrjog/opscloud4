package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.CloudImageParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudImageVO;

/**
 * @Author baiyi
 * @Date 2020/3/18 11:37 上午
 * @Version 1.0
 */
public interface CloudImageFacade {

    DataTable<CloudImageVO.CloudImage> fuzzyQueryCloudImagePage(CloudImageParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> syncCloudImageByKey(String key);

    BusinessWrapper<Boolean> deleteCloudImageById(int id);

    BusinessWrapper<Boolean> setCloudImageActive(int id);
}
