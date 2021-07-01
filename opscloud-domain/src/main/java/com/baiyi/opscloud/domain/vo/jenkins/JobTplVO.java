package com.baiyi.opscloud.domain.vo.jenkins;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/7/24 1:50 下午
 * @Version 1.0
 */
public class JobTplVO {

    public interface IJobTpl {

        void setJobTpl(JobTpl jobTpl);

        Integer getJobTplId();
    }


    @Data
    @NoArgsConstructor
    @ApiModel
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
