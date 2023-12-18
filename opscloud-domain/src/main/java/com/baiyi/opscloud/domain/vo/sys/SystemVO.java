package com.baiyi.opscloud.domain.vo.sys;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/9/6 1:28 下午
 * @Version 1.0
 */
public class SystemVO {

    /**
     * 系统信息
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Info implements Serializable {
        @Serial
        private static final long serialVersionUID = 2152909622535993214L;
        private Cpu cpu;
        private Mem mem;
    }


    /**
     * CPU信息
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Cpu implements Serializable {
        @Serial
        private static final long serialVersionUID = 916243397197915613L;
        /**
         * 核心数
         */
        private int cpuNum;

        /**
         * CPU总的使用率
         */
        private double total;

        /**
         * CPU系统使用率
         */
        private double sys;

        /**
         * CPU用户使用率
         */
        private double used;

        /**
         * CPU当前等待率
         */
        private double wait;

        /**
         * CPU当前空闲率
         */
        private double free;

    }

    /**
     * 內存信息
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Mem implements Serializable {
        @Serial
        private static final long serialVersionUID = -2293050628937451167L;
        /**
         * 内存总量
         */
        private double total;

        /**
         * 已用内存
         */
        private double used;

        /**
         * 剩余内存
         */
        private double free;
    }

}