package com.baiyi.opscloud.common.constants.enums;


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

    /**
     * 数据源类型
     */
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
    DINGTALK_ROBOT(13, "DINGTALK_ROBOT"),
    DINGTALK_APP(14, "DINGTALK_APP"),
    CONSUL(15, "CONSUL"),
    /**
     * 阿里云
     */
    ALIYUN(50, "ALIYUN"),
    AWS(51, "AWS"),
    /**
     * 华为云
     */
    HUAWEICLOUD(52, "HUAWEICLOUD"),
    /**
     * 领先互联
     */
    LXHL(53, "LXHL"),
    ALIYUN_DEVOPS(54, "ALIYUN_DEVOPS"),
    METER_SPHERE(55, "METER_SPHERE"),
    APOLLO(56, "APOLLO"),
    SER_DEPLOY(57, "SER_DEPLOY"),
    ALIYUN_ARMS(58,"ALIYUN_ARMS"),
    /**
     * 阿里云事件总线
     */
    ALIYUN_EVENTBRIDGE(59,"ALIYUN_EVENTBRIDGE")
    ;

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