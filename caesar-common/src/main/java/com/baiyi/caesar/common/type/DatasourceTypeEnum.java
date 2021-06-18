package com.baiyi.caesar.common.type;


import com.baiyi.caesar.domain.vo.common.OptionsVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:22 下午
 * @Version 1.0
 */
public enum DatasourceTypeEnum {

    LDAP(1, "LDAP"),
    JENKINS(2, "JENKINS"),
    GITLAB(3, "GITLAB"),
    SONAR(4,"SONAR"),
    ANSIBLE(5,"ANSIBLE"),
    JUMPSERVER(6,"JUMPSERVER"),
    ZABBIX(7,"ZABBIX"),
    PROMETHEUS(8,"PROMETHEUS"),
    GUACAMOLE(9,"GUACAMOLE"),
    NEXUS(10,"NEXUS"),
    ALIYUN(50,"ALIYUN"),
    AWS(51,"AWS")
    ;

    private int type;
    private String name;

    DatasourceTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(int type) {
        for (DatasourceTypeEnum typeEnum : DatasourceTypeEnum.values())
            if (typeEnum.type == type)
                return typeEnum.getName();
        return "Null";
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static OptionsVO.Options toOptions() {
        List<OptionsVO.Option> optionList = Lists.newArrayList();
        for (DatasourceTypeEnum e : DatasourceTypeEnum.values()) {
            OptionsVO.Option o = OptionsVO.Option.builder()
                    .label(e.getName())
                    .value(e.getType())
                    .build();
            optionList.add(o);
        }
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }
}
