package com.baiyi.opscloud.workorder.entry;

import com.baiyi.opscloud.common.util.JSONUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2023/6/5 14:59
 * @Version 1.0
 */
public class NewApplicationEntry {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NewApplication implements Serializable {

        @Serial
        private static final long serialVersionUID = 1436284707624901947L;

        @Schema(description = "应用级别标签")
        private String levelTag;

        private Integer id;

        /**
         * 资产父关系
         */
        private Integer parentId = 0;

        /**
         * 数据源实例uuid
         */
        private String instanceUuid;

        /**
         * 资产名称
         */
        private String name;

        /**
         * 资产id
         */
        private String assetId;

        /**
         * 资产类型
         */
        private String assetType;

        /**
         * 资产分类
         */
        private String kind;

        /**
         * 资产版本
         */
        private String version;

        /**
         * 有效
         */
        private Boolean isActive = true;

        /**
         * 资产关键字1
         */
        private String assetKey;

        /**
         * 资产关键字2
         */
        private String assetKey2;

        /**
         * 区域
         */
        private String zone;

        /**
         * 地区id
         */
        private String regionId;

        /**
         * 资产状态
         */
        private String assetStatus;

        private String description;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }
    }

}