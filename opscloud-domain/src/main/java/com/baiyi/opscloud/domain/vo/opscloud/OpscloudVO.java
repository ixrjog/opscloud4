package com.baiyi.opscloud.domain.vo.opscloud;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/24 4:34 下午
 * @Since 1.0
 */
public class OpscloudVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Instance {

        private Integer id;

        private String name;

        private String hostname;

        private String hostIp;

        private Integer instanceStatus;

        private Boolean isActive;

        private Date createTime;

        private Date updateTime;

        private String comment;
    }
}
