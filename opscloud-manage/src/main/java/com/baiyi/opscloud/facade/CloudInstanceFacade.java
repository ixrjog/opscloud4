package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTypeParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTypeVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudVSwitchVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:41 下午
 * @Version 1.0
 */
public interface CloudInstanceFacade {

    DataTable<CloudInstanceTemplateVO.CloudInstanceTemplate> fuzzyQueryCloudInstanceTemplatePage(CloudInstanceTemplateParam.PageQuery pageQuery);

    BusinessWrapper<CloudInstanceTemplateVO.CloudInstanceTemplate> saveCloudInstanceTemplate(CloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate);

    BusinessWrapper<Integer> createCloudInstance( CloudInstanceTemplateParam.CreateCloudInstance createCloudInstance);

    BusinessWrapper<CloudInstanceTemplateVO.CloudInstanceTemplate> saveCloudInstanceTemplateYAML(CloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate);

    DataTable<CloudInstanceTypeVO.CloudInstanceType> fuzzyQueryCloudInstanceTypePage(CloudInstanceTypeParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> deleteCloudInstanceTemplateById(int id);

    BusinessWrapper<Boolean> syncInstanceType(int cloudType);

    List<String> queryCloudRegionList(int cloudType);

    List<Integer> queryCpuCoreList(int cloudType);

    List<CloudVSwitchVO.VSwitch> queryCloudInstanceTemplateVSwitch(int templateId, String zoneId);
}
