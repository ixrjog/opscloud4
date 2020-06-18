package com.baiyi.opscloud.bo;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudImage;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcSecurityGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/3/30 11:12 上午
 * @Version 1.0
 */
@Data
@Builder
public class CreateCloudInstanceBO{

   private CloudInstanceTemplateParam.CreateCloudInstance createCloudInstance;
   private CloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate;
   private OcCloudImage ocCloudImage;
   private OcCloudVpcSecurityGroup ocCloudVpcSecurityGroup;
   private OcServerGroup ocServerGroup;

}
