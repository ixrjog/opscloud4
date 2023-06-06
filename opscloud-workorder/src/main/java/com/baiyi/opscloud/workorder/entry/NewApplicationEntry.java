package com.baiyi.opscloud.workorder.entry;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2023/6/5 14:59
 * @Version 1.0
 */
public class NewApplicationEntry {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NewApplication extends DatasourceInstanceAsset implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @Schema(description = "应用名称")
        private String applicationName;

        @Schema(description = "应用描述")
        private String applicationComment;

        @Schema(description = "应用级别标签")
        private String levelTag;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }
    }

}
