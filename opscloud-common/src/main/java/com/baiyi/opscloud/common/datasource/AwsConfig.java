package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.base.IToURL;
import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/10/21 4:35 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AwsConfig extends BaseDsConfig {

    private Aws aws;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Aws {
        private Account account;
        private String regionId;
        private Set<String> regionIds; // 可用区
        private Ec2 ec2;
        private SerDeploy serDeploy;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Account {

        private String id;
        private String name;
        private String company; // 可选项公司
        private String accessKeyId;
        private String secretKey;
        private String loginUrl; // IAM登录地址

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Ec2 extends IToURL {

        private String instances;

        @Override
        protected String acqURL() {
            return instances;
        }
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class SerDeploy {

        private String bucketName;

    }

}