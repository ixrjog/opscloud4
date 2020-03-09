package com.baiyi.opscloud.jumpserver.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/9 11:28 上午
 * @Version 1.0
 */
@Data
@Builder
public class PermsAssetpermissionBO {

    private String id;
    private String name;
    @Builder.Default
    private Boolean isActive = true;
    private Date dateExpired;
    @Builder.Default
    private String createdBy ="opscloud";
    @Builder.Default
    private Date dateCreated = new Date();
    @Builder.Default
    private Date dateStart = new Date();
    @Builder.Default
    private String orgId = "";
    @Builder.Default
    private String comment = "";
}
