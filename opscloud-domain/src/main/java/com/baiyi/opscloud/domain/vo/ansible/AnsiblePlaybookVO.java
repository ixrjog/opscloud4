package com.baiyi.opscloud.domain.vo.ansible;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2021/9/1 10:57 上午  &#064;Version  1.0
 */
public class AnsiblePlaybookVO {

    public interface IPlaybook {

        Integer getAnsiblePlaybookId();

        void setPlaybook(Playbook playbook);

        void setTaskName(String taskName);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Playbook extends BaseVO {

        private Integer id;

        @Schema(description = "剧本UUID")
        private String playbookUuid;

        @Schema(description = "剧本名称")
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

}