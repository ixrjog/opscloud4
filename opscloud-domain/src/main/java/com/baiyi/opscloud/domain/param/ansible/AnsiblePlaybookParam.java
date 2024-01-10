package com.baiyi.opscloud.domain.param.ansible;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/9/1 10:55 上午
 * @Version 1.0
 */
public class AnsiblePlaybookParam {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Playbook {

        private Integer id;

        @Schema(description = "剧本UUID")
        private String playbookUuid;

        @Schema(description = "剧本名称")
        @NotNull(message = "剧本名称不能为空")
        private String name;

        @NotNull(message = " 剧本内容不能为空")
        private String playbook;

        /**
         * 标签配置
         */
        @Schema(description = "剧本标签")
        private String tags;

        @Schema(description = "剧本外部变量")
        private String vars;

        /**
         * 描述
         */
        private String comment;
    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AnsiblePlaybookPageQuery extends PageParam implements IExtend {

        @Schema(description = "剧本名称")
        private String name;

        private Boolean extend;

    }

}