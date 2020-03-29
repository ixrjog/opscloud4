package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCParam;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCSecurityGroupParam;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCVSwitchParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudVPCSecurityGroupVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudVPCVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudVSwitchVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 7:02 下午
 * @Version 1.0
 */
public interface CloudVPCFacade {

    DataTable<OcCloudVPCVO.CloudVpc> fuzzyQueryCloudVPCPage(CloudVPCParam.PageQuery pageQuery);

    DataTable<OcCloudVPCVO.CloudVpc> queryCloudVPCPage(CloudVPCParam.PageQuery pageQuery);

    DataTable<OcCloudVPCSecurityGroupVO.SecurityGroup> queryCloudVPCSecurityGroupPage(CloudVPCSecurityGroupParam.PageQuery pageQuery);

    DataTable<OcCloudVSwitchVO.VSwitch> queryCloudVPCVSwitchPage(CloudVPCVSwitchParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> syncCloudVPCByKey(String key);

    BusinessWrapper<Boolean> deleteCloudVPCById(int id);

    BusinessWrapper<Boolean> setCloudVPCActive(int id);

    BusinessWrapper<Boolean> setCloudVPCSecurityGroupActive(int id);

    BusinessWrapper<Boolean> setCloudVPCVSwitchActive(int id);

    List<OcCloudVSwitchVO.VSwitch> updateOcCloudVpcVSwitch(OcCloudInstanceTemplateVO.InstanceTemplate instanceTemplate, List<OcCloudInstanceTemplateVO.VSwitch> vswitchList);
}
