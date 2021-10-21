package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/10/21 4:35 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AwsDsInstanceConfig extends BaseDsInstanceConfig {

    private Aws aws;
    
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Aws {
        private Account account;
        private String apRegionId;
        private Ec2 ec2;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Account {

        private String uid;
        private String name;
        private String company; // 公司
        private String accessKeyId;
        private String secretKey;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Ec2 {

        private String instances;

    }

}

