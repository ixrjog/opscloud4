package com.baiyi.opscloud.domain.vo.ansible;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2021/9/1 10:57 上午
 * @Version 1.0
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
    @ApiModel
    public static class Playbook extends BaseVO {

        private Integer id;

        @ApiModelProperty(value = "剧本UUID")
        private String playbookUuid;

        @ApiModelProperty(value = "剧本名称")
        @NotNull(message = "剧本名称不能为空")
        private String name;

        @NotNull(message = " 剧本内容不能为空")
        private String playbook;

        /**
         * 标签配置
         */
        @ApiModelProperty(value = "剧本标签")
        private String tags;

        @ApiModelProperty(value = "剧本外部变量")
        private String vars;

        /**
         * 描述
         */
        private String comment;
    }
}
