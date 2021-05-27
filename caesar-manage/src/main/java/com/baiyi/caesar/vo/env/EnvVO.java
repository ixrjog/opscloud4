package com.baiyi.caesar.vo.env;

import com.baiyi.caesar.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:33 下午
 * @Version 1.0
 */
public class EnvVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Env extends BaseVO {

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @ApiModelProperty(value = "环境名称")
        private String envName;

        @ApiModelProperty(value = "颜色值")
        private String color;

        @ApiModelProperty(value = "环境值", example = "1")
        private Integer envType;

        @ApiModelProperty(value = "描述")
        private String comment;

    }

    public interface IEnv {

        void setEnv(Env env);

        Integer getEnvType();
    }
}
