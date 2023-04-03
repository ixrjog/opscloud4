package com.baiyi.opscloud.domain.vo.env;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:33 下午
 * @Version 1.0
 */
public class EnvVO {

    public interface IEnv {
        void setEnv(Env env);

        Integer getEnvType();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Env extends BaseVO implements Serializable {

        private static final long serialVersionUID = 5444243347465574812L;
        @Schema(name = "主键", example = "1")
        private Integer id;

        @Schema(name = "环境名称")
        private String envName;

        @Schema(name = "颜色值")
        private String color;

        @Schema(name = "终端提示色")
        private Integer promptColor;

        @Schema(name = "环境值", example = "1")
        private Integer envType;

        @Schema(name = "有效", example = "true")
        private Boolean isActive;

        @Schema(name = "顺序")
        private Integer seq;

        @Schema(name = "描述")
        private String comment;

    }

}
