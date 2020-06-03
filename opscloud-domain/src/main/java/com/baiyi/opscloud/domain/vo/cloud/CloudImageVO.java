package com.baiyi.opscloud.domain.vo.cloud;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/18 11:18 上午
 * @Version 1.0
 */
public class CloudImageVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudImage {

        @ApiModelProperty(value = "主键")
        private Integer id;
        private String uid;
        private String accountName;
        private String regionId;
        private String imageId;
        private String imageName;
        private Integer imageSize;
        private Integer cloudType;
        private String imageOwnerAlias;
        private Integer isSupportioOptimized;
        private Integer isSupportCloudinit;
        private String osName;
        private String osNameEn;
        private String architecture;
        private String imageStatus;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date creationTime;
        private String osType;
        private String platform;
        private Integer isActive;
        private Integer isDeleted;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
        private String comment;
        private String imageDetail;

    }

}