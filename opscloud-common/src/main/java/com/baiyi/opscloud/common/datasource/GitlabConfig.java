package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:57 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GitlabConfig extends BaseDsConfig {

    private Gitlab gitlab;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Gitlab {

        private String url;
        private String token;
        private SystemHooks systemHooks;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class SystemHooks {
        private String token;  // 回调token
    }
}

