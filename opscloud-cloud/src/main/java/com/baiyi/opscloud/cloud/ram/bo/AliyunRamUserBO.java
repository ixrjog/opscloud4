package com.baiyi.opscloud.cloud.ram.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/6/9 1:35 下午
 * @Version 1.0
 */
@Builder
@Data
public class AliyunRamUserBO {

    private Integer id;
    private String accountUid;
    private String ramUserId;
    private String ramUsername;
    private String ramDisplayName;
    private String mobile;
    @Builder.Default
    private Integer accessKeys = 0;
    @Builder.Default
    private Integer ramType = 0;
    private String comment;
    private Date createDate;
    private Date updateDate;

}
