package com.baiyi.opscloud.domain.vo.monitor;

import com.baiyi.opscloud.domain.vo.account.AccountVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/11/24 10:41 上午
 * @Version 1.0
 */
public class MonitorVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Host implements Serializable {

        private static final long serialVersionUID = 8324856060529761933L;
        private List<TagVO.Tag> tags;

        private EnvVO.Env env;

        private ServerGroupVO.ServerGroup serverGroup;

        @ApiModelProperty(value = "主机宏配置")
        private List<Macro> macros;

        /**
         * 前端按钮使用
         */
        private Boolean isCreating = false;

        /**
         * 前端按钮使用
         */
        private Boolean isPushing = false;

        /**
         * 前端按钮使用
         */
        private Boolean isSetting = false;
        /**
         * (readonly) Availability of Zabbix agent.
         * 0 - (default) unknown;
         * 1 - available;
         * 2 - unavailable.
         */
        @ApiModelProperty(value = "ZabbixAgent可用性")
        private Integer agentAvailable;

        @ApiModelProperty(value = "监控模版")
        private List<Template> templates;

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @ApiModelProperty(value = "服务器名称")
        private String name;

        @ApiModelProperty(value = "服务器组id", example = "1")
        private Integer serverGroupId;

        @ApiModelProperty(value = "登录类型", example = "1")
        private Integer loginType;

        @ApiModelProperty(value = "登录账户")
        private String loginUser;

        @ApiModelProperty(value = "环境类型", example = "1")
        private Integer envType;

        @ApiModelProperty(value = "公网ip")
        private String publicIp;

        @ApiModelProperty(value = "私网ip")
        private String privateIp;

        @ApiModelProperty(value = "服务器类型", example = "1")
        private Integer serverType;

        @ApiModelProperty(value = "地区")
        private String area;

        @ApiModelProperty(value = "序号", example = "1")
        private Integer serialNumber;

        @ApiModelProperty(value = "监控状态", example = "1")
        private Integer monitorStatus;

        @ApiModelProperty(value = "资源描述")
        private String comment;

        @ApiModelProperty(value = "服务器状态", example = "1")
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

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Template implements Serializable {

        private static final long serialVersionUID = 5558404696969471062L;
        @ApiModelProperty(value = "模版名称")
        private String name;

        @ApiModelProperty(value = "启用")
        private Boolean isActive;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Macro implements Serializable {

        private static final long serialVersionUID = 4762644329294871331L;

        @ApiModelProperty(value = "宏名称")
        private String macro;

        @ApiModelProperty(value = "宏值")
        private String value;

        @ApiModelProperty(value = "启用")
        private Boolean isActive;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class User extends AccountVO.Account implements Serializable {

        private static final long serialVersionUID = -7010728998580936113L;

        private List<Usrgrp> usrgrps;

        private List<Media> medias;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Usrgrp implements Serializable {

        private static final long serialVersionUID = -7028888023422971982L;

        private String usrgrpid;

        private String name;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Media implements Serializable {

        private static final long serialVersionUID = -595521665468177823L;

        private Integer mediaid;

        private Integer mediatypeid;

        private String sendto;

        private Boolean active;
    }


}
