package com.baiyi.opscloud.jumpserver.bo;

import com.baiyi.opscloud.common.base.Global;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/8 5:09 下午
 * @Version 1.0
 */
@Data
@Builder
public class UsersUsergroupBO {
    private String id;
    private String name;
    @Builder.Default
    private Date dateCreated = new Date();
    @Builder.Default
    private String createdBy = Global.CREATED_BY;
    @Builder.Default
    private String orgId = "";
    private String comment;
}
