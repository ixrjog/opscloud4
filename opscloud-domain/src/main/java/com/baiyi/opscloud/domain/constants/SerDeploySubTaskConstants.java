package com.baiyi.opscloud.domain.constants;

import lombok.Getter;

/**
 * @Author 修远
 * @Date 2023/6/13 5:00 PM
 * @Since 1.0
 */

@Getter
public enum SerDeploySubTaskConstants {

    CREATE("CREATE", "新建"),
    DEPLOYING("DEPLOYING", "发布中"),
    FINISH("FINISH", "完成")
    ;

    private final String name;
    private final String desc;

    SerDeploySubTaskConstants(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

}