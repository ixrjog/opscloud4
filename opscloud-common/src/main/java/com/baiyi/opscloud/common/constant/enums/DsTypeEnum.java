package com.baiyi.opscloud.common.constant.enums;


import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:22 下午
 * @Version 1.0
 */
@Getter
public enum DsTypeEnum {

    LOCAL(0, "LOCAL"),
    LDAP(1, "LDAP"),
    JENKINS(2, "JENKINS"),
    GITLAB(3, "GITLAB"),
    SONAR(4, "SONAR"),
    ANSIBLE(5, "ANSIBLE"),
    KUBERNETES(6, "KUBERNETES"),
    ZABBIX(7, "ZABBIX"),
    PROMETHEUS(8, "PROMETHEUS"),
    GUACAMOLE(9, "GUACAMOLE"),
    NEXUS(10, "NEXUS"),
    TENCENT_EXMAIL(11, "TENCENT_EXMAIL"),
    NACOS(12, "NACOS"),
    DINGTALK(13, "DINGTALK"),
    DINGTALK_APP(14, "DINGTALK_APP"),
    ALIYUN(50, "ALIYUN"),
    AWS(51, "AWS");

    private final int type;

    private final String name;

    DsTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(int type) {
        return Arrays.stream(DsTypeEnum.values()).filter(typeEnum -> typeEnum.type == type).findFirst().map(DsTypeEnum::getName).orElse("Null");
    }

    public static OptionsVO.Options toOptions() {
        return OptionsVO.Options.builder()
                .options(Arrays.stream(DsTypeEnum.values()).map(e -> OptionsVO.Option.builder()
                        .label(e.getName())
                        .value(e.getType())
                        .build()).collect(Collectors.toList()))
                .build();
    }

}
