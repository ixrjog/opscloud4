package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCParam;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCSecurityGroupParam;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCVSwitchParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudVPCSecurityGroupVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudVPCVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudVSwitchVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 7:02 下午
 * @Version 1.0
 */
public interface CloudVPCFacade {

    DataTable<CloudVPCVO.CloudVpc> fuzzyQueryCloudVPCPage(CloudVPCParam.PageQuery pageQuery);

    DataTable<CloudVPCVO.CloudVpc> queryCloudVPCPage(CloudVPCParam.PageQuery pageQuery);

    DataTable<CloudVPCSecurityGroupVO.SecurityGroup> queryCloudVPCSecurityGroupPage(CloudVPCSecurityGroupParam.PageQuery pageQuery);

    DataTable<CloudVSwitchVO.VSwitch> queryCloudVPCVSwitchPage(CloudVPCVSwitchParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> syncCloudVPCByKey(String key);

    BusinessWrapper<Boolean> deleteCloudVPCById(int id);

    BusinessWrapper<Boolean> setCloudVPCActive(int id);

    BusinessWrapper<Boolean> setCloudVPCSecurityGroupActive(int id);

    BusinessWrapper<Boolean> setCloudVPCVSwitchActive(int id);

    List<CloudVSwitchVO.VSwitch> updateOcCloudVpcVSwitch(CloudInstanceTemplateVO.InstanceTemplate instanceTemplate, List<CloudInstanceTemplateVO.VSwitch> vswitchList);
}
