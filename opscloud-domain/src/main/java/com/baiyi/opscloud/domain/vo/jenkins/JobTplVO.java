package com.baiyi.opscloud.domain.vo.jenkins;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/7/24 1:50 下午
 * @Version 1.0
 */
public class JobTplVO {

    @Data
    @NoArgsConstructor
    @Schema
    public static class JobTpl {

        private Integer id;
        private Integer jenkinsInstanceId;
        private String name;
        private String tplName;
        private String tplType;
        private Integer tplVersion;
        private String tplHash;
        private Date updateTime;
        private Date createTime;
        private String tplContent;
        private String parameterYaml;
        private Boolean supportRollback;
        private Integer rollbackType;
        private String comment;

    }

}