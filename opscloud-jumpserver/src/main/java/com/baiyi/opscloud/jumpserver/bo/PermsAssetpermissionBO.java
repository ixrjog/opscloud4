package com.baiyi.opscloud.jumpserver.bo;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.jumpserver.center.impl.JumpserverCenterImpl;
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
    @Builder.Default
    private Date dateExpired = TimeUtils.gmtToDate(JumpserverCenterImpl.DATE_EXPIRED);
    @Builder.Default
    private String createdBy = Global.CREATED_BY;
    @Builder.Default
    private Date dateCreated = TimeUtils.gmtToDate(JumpserverCenterImpl.DATE_START);
    @Builder.Default
    private Date dateStart = TimeUtils.gmtToDate(JumpserverCenterImpl.DATE_START);
    @Builder.Default
    private String orgId = "";
    @Builder.Default
    private String comment = "";
    @Builder.Default
    private Integer actions = 255;
}
