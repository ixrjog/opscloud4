package com.baiyi.opscloud.domain.vo.sys;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/9/3 1:49 下午
 * @Version 1.0
 */
public class InstanceVO {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class Health {
        private String status;
        private boolean isHealth;
    }

    /**
     * 注册的实例
     */
    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class RegisteredInstance extends BaseVO {

        private SystemVO.Info systemInfo; // 系统信息

        private Integer id;

        /**
         * 实例名
         */
        private String name;

        /**
         * 主机名
         */
        private String hostname;

        /**
         * 主机ip
         */
        private String hostIp;

        /**
         * 实例状态
         */
        private Integer status;

        /**
         * 有效
         */
        private Boolean isActive;

        /**
         * 说明
         */
        private String comment;

        private String license;

    }
}
