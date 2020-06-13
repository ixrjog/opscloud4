package com.baiyi.opscloud.cloud.db.builder;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAttribute;

/**
 * @Author baiyi
 * @Date 2020/2/29 7:31 下午
 * @Version 1.0
 */
public class CloudDbAttributeBuilder {

    public static OcCloudDbAttribute build( String dbInstanceId,String name,Integer value) {
        CloudDbAttributeBO cloudDbAttributeBO = CloudDbAttributeBO.builder()
                .dbInstanceId(dbInstanceId)
                .attributeName(name)
                .attributeValue(String.valueOf(value))
                .build();
        return covert(cloudDbAttributeBO);
    }

    public static OcCloudDbAttribute build( String dbInstanceId,String name,Long value) {
        CloudDbAttributeBO cloudDbAttributeBO = CloudDbAttributeBO.builder()
                .dbInstanceId(dbInstanceId)
                .attributeName(name)
                .attributeValue(String.valueOf(value))
                .build();
        return covert(cloudDbAttributeBO);
    }

    public static OcCloudDbAttribute build( String dbInstanceId,String name,String value) {
        CloudDbAttributeBO cloudDbAttributeBO = CloudDbAttributeBO.builder()
                .dbInstanceId(dbInstanceId)
                .attributeName(name)
                .attributeValue(value)
                .build();
        return covert(cloudDbAttributeBO);
    }

    private static OcCloudDbAttribute covert(CloudDbAttributeBO cloudDbAttributeBO) {
        return BeanCopierUtils.copyProperties(cloudDbAttributeBO, OcCloudDbAttribute.class);
    }

}
