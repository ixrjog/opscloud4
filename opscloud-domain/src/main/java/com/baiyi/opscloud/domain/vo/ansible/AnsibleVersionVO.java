package com.baiyi.opscloud.domain.vo.ansible;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/4/20 1:00 下午
 * @Version 1.0
 */
public class AnsibleVersionVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AnsibleVersion {

        @ApiModelProperty(value = "Ansible版本详情")
        private String version;

        @ApiModelProperty(value = "AnsiblePlaybook版本详情")
        private String playbookVersion;

    }

}
