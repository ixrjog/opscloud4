package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTypeParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTypeVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudVSwitchVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:41 下午
 * @Version 1.0
 */
public interface CloudInstanceFacade {

    DataTable<OcCloudInstanceTemplateVO.CloudInstanceTemplate> fuzzyQueryCloudInstanceTemplatePage(CloudInstanceTemplateParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> saveCloudInstanceTemplate(OcCloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate);

    BusinessWrapper<Boolean> createCloudInstance( CloudInstanceTemplateParam.CreateCloudInstance createCloudInstance);

    BusinessWrapper<Boolean> saveCloudInstanceTemplateYAML(OcCloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate);

    DataTable<OcCloudInstanceTypeVO.CloudInstanceType> fuzzyQueryCloudInstanceTypePage(CloudInstanceTypeParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> deleteCloudInstanceTemplateById(int id);

    BusinessWrapper<Boolean> syncInstanceType(int cloudType);

    List<String> queryCloudRegionList(int cloudType);

    List<Integer> queryCpuCoreList(int cloudType);

    List<OcCloudVSwitchVO.VSwitch> queryCloudInstanceTemplateVSwitch(int templateId, String zoneId);
}
