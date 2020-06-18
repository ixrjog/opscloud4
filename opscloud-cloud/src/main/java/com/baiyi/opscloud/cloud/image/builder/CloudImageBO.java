package com.baiyi.opscloud.cloud.image.builder;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/18 10:00 上午
 * @Version 1.0
 */
@Data
@Builder
public class CloudImageBO {

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
    private Date creationTime;
    private String osType;
    private String platform;
    @Builder.Default
    private Integer isActive = 0;
    @Builder.Default
    private Integer isDeleted = 0;
    private Date createTime;
    private Date updateTime;
    private String comment;
    private String imageDetail;

}
