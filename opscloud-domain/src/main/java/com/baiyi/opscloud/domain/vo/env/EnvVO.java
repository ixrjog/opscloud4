package com.baiyi.opscloud.domain.vo.env;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:33 下午
 * @Version 1.0
 */
public class EnvVO {

    public interface IEnv {
        
        /**
         * 设置环境
         * @param env
         */
        void setEnv(Env env);

        /**
         * 取环境类型
         * @return
         */
        Integer getEnvType();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Env extends BaseVO implements Serializable {

        @Serial
        private static final long serialVersionUID = 5444243347465574812L;

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "环境名称")
        private String envName;

        @Schema(description = "颜色值")
        private String color;

        @Schema(description = "终端提示色")
        private Integer promptColor;

        @Schema(description = "环境值", example = "1")
        private Integer envType;

        @Schema(description = "有效", example = "true")
        private Boolean isActive;

        @Schema(description = "顺序")
        private Integer seq;

        @Schema(description = "描述")
        private String comment;

    }

}