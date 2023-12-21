package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author 修远
 * @Date 2023/6/15 3:50 PM
 * @Since 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SerDeployConfig extends BaseDsConfig {

    private SerDeployInstance serDeployInstance;

    /**
     * envName , uri
     */
    private Map<String, String> serDeployURI;

    /**
     * current-full-ser-files/{appName}/{envName}
     */
    private String currentSerPath;


    @Data
    @NoArgsConstructor
    @Schema
    public static class SerDeployInstance {

        private String InstanceUuid;

        private String regionId;

        private String bucketName;

    }

}