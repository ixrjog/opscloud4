package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/11/11 3:23 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NacosDsInstanceConfig extends BaseDsInstanceConfig {

    private Nacos nacos;


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Nacos {

        private String version;
        private String url;
        private String username;
        private String password;

        public Map<String, String> getLoginParam() {
            Map<String, String> loginParam = Maps.newHashMap();
            loginParam.put("username", this.username);
            loginParam.put("password", this.password);
            return loginParam;
        }

    }


}


