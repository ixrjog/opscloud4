package com.baiyi.opscloud.domain.vo.kubernetes;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/7/3 2:11 下午
 * @Version 1.0
 */
public class BaseKubernetesApplicationVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BaseProperty {

        private KubernetesApplicationVO.Application application;
        private KubernetesApplicationVO.Instance instance;

        private Integer applicationId;
        private Integer instanceId;
    }

}
