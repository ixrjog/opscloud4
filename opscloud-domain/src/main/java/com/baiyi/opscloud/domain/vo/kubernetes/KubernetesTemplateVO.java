package com.baiyi.opscloud.domain.vo.kubernetes;

import com.baiyi.opscloud.domain.vo.env.EnvVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/6/30 1:51 下午
 * @Version 1.0
 */
public class KubernetesTemplateVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Template {

        private EnvVO.Env env;

        private Integer id;
        private String name;
        private String templateType;
        private Integer envType;
        private Date updateTime;
        private Date createTime;
        private String templateYaml;
        private String comment;

    }
}
