package com.baiyi.opscloud.domain.vo.ansible;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/9/1 10:57 上午
 * @Version 1.0
 */
public class AnsiblePlaybookVO {


    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Playbook extends BaseVO {

        private Integer id;


        private String playbookUuid;

        /**
         * 名称
         */
        private String name;


        /**
         * 剧本内容
         */
        private String playbook;

        /**
         * 标签配置
         */
        private String tags;

        /**
         * 外部变量
         */
        private String vars;

        /**
         * 描述
         */
        private String comment;
    }
}
