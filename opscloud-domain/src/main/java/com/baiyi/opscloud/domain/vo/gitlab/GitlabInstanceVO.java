package com.baiyi.opscloud.domain.vo.gitlab;

import com.baiyi.opscloud.domain.vo.server.ServerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/7/20 8:44 上午
 * @Version 1.0
 */
public class GitlabInstanceVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Instance {

        @ApiModelProperty(value = "jenkins服务器")
        private ServerVO.Server server;

        @ApiModelProperty(value = "版本详情")
        private Version version;

        @ApiModelProperty(value = "项目数量")
        private Integer projectSize;

        private Integer id;

        private String name;

        private String url;

        private String token;

        private Boolean isActive;

        private Integer serverId;

        private Date createTime;

        private Date updateTime;

        private String comment;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Version {
        private String version;
        private String revision;
    }
}
