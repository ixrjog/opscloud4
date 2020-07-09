package com.baiyi.opscloud.domain.vo.kubernetes;

import com.baiyi.opscloud.domain.vo.env.EnvVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/6/28 2:05 下午
 * @Version 1.0
 */
public class KubernetesClusterNamespaceVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Namespace {

        private KubernetesClusterVO.Cluster cluster;

        private EnvVO.Env env;

        private Integer id;
        private Integer clusterId;
        private String namespace;
        private Integer envType;
        private Date createTime;
        private Date updateTime;
        private String comment;
    }

}
