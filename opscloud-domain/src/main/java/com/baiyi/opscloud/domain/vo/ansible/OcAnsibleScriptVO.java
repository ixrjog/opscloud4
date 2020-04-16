package com.baiyi.opscloud.domain.vo.ansible;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/16 2:50 下午
 * @Version 1.0
 */
public class OcAnsibleScriptVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AnsibleScript {
        private Integer id;

        private String scriptUuid;

        private String name;

        private String scriptLang;

        private String comment;

        private Integer userId;

        private Integer useType;

        private Integer scriptLock;

        private Date createTime;

        private Date updateTime;

        private String scriptContent;

        private String userDetail;
    }
}
