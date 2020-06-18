package com.baiyi.opscloud.server.bo;

import com.baiyi.opscloud.common.base.Global;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/9 2:33 下午
 * @Version 1.0
 */
@Data
@Builder
public class AssetsAssetBO {

    private String id;
    private String ip;
    private String hostname;
    @Builder.Default
    private Integer port = 22;
    @Builder.Default
    private Boolean isActive = true;
    private String publicIp;
    private String number;
    private String vendor;
    private String model;
    private String sn;
    private String cpuModel;
    private Integer cpuCount;
    private Integer cpuCores;
    private String memory;
    private String diskTotal;
    private String diskInfo;
    @Builder.Default
    private String platform ="Linux";
    private String os;
    private String osVersion;
    private String osArch;
    private String hostnameRaw;
    @Builder.Default
    private String createdBy = "opscloud";
    private Date dateCreated;
    private String adminUserId;
    private String domainId;
    @Builder.Default
    private String protocol = "ssh";
    @Builder.Default
    private String orgId = "";
    private Integer cpuVcpus;
    @Builder.Default
    private String comment = Global.CREATED_BY;
    @Builder.Default
    private String protocols ="ssh/22";
    @Builder.Default
    private Integer platformId = 1;

}
