package com.baiyi.opscloud.domain.vo.server;

import com.baiyi.opscloud.domain.vo.cloud.CloudServerVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:56 下午
 * @Version 1.0
 */
public class ServerVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Server {

        private List<TagVO.Tag> tags;

        private EnvVO.Env env;

        private ServerGroupVO.ServerGroup serverGroup;

        private CloudServerVO.CloudServer cloudServer;

        @ApiModelProperty(value = "主键",example="1")
        private Integer id;

        @ApiModelProperty(value = "服务器名称")
        private String name;

        @ApiModelProperty(value = "服务器组id",example="1")
        private Integer serverGroupId;

        @ApiModelProperty(value = "登录类型",example="1")
        private Integer loginType;

        @ApiModelProperty(value = "登录账户")
        private String loginUser;

        @ApiModelProperty(value = "环境类型",example="1")
        private Integer envType;

        @ApiModelProperty(value = "公网ip")
        private String publicIp;

        @ApiModelProperty(value = "私网ip")
        private String privateIp;

        @ApiModelProperty(value = "服务器类型",example="1")
        private Integer serverType;

        @ApiModelProperty(value = "地区")
        private String area;

        @ApiModelProperty(value = "序号",example="1")
        private Integer serialNumber;

        @ApiModelProperty(value = "监控状态",example="1")
        private Integer monitorStatus;

        @ApiModelProperty(value = "资源描述")
        private String comment;

        @ApiModelProperty(value = "服务器状态",example="1")
        private Integer serverStatus;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "云主机id,云主机录入专用")
        private Integer cloudServerId;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;

    }
}
