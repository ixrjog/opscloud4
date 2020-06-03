package com.baiyi.opscloud.domain.vo.cloud;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/3/1 12:07 下午
 * @Version 1.0
 */
public class CloudDBVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudDB {

        // 账户权限关键字 ['ReadOnly', 'ReadWrite', 'DDLOnly', 'DMLOnly']
        private List<String> privileges;

        private List<CloudDBAccountVO.CloudDBAccount> accounts;

        private List<CloudDBDatabaseVO.CloudDBDatabase> databases;

        // 属性 attributeName,attributeValue
        private Map<String, String> attributeMap;

        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "云账户uid")
        private String uid;

        @ApiModelProperty(value = "云账户名称")
        private String accountName;

        @ApiModelProperty(value = "云数据库类型")
        private Integer cloudDbType;

        @ApiModelProperty(value = "地域id")
        private String regionId;

        @ApiModelProperty(value = "实例id")
        private String dbInstanceId;

        @ApiModelProperty(value = "实例描述系统获取")
        private String dbInstanceDescription;

        @ApiModelProperty(value = "实例类型")
        private String dbInstanceType;

        @ApiModelProperty(value = "数据库类型")
        private String engine;

        @ApiModelProperty(value = "数据库版本")
        private String engineVersion;

        @ApiModelProperty(value = "可用区id")
        private String zone;

        @ApiModelProperty(value = "实例的付费类型")
        private String payType;

        @ApiModelProperty(value = "实例状态")
        private String dbInstanceStatus;

        @ApiModelProperty(value = "到期时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;

        @ApiModelProperty(value = "实例的网络类型")
        private String instanceNetworkType;

        @ApiModelProperty(value = "实例的访问模式")
        private String connectionMode;

        @ApiModelProperty(value = "实例的网络连接类型")
        private String dbInstanceNetType;

        @ApiModelProperty(value = "实例储存类型")
        private String dbInstanceStorageType;

        @ApiModelProperty(value = "实例规格")
        private String dbInstanceClass;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createdTime;

        @ApiModelProperty(value = "实例系列")
        private String category;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    }
}
