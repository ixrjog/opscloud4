package com.baiyi.opscloud.common.constant.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2020/7/23 11:12 上午
 * @Version 1.0
 */
public enum JobTypeEnum {

    IOS("IOS"),
    ANDROID("ANDROID"),
    ANDROID_AAR("ANDROID_AAR"),
    SPRINGBOOT("SPRINGBOOT"),
    HTML5("HTML5"),
    JAVA("JAVA"),
    JAVA_DEPLOYMENT("JAVA_DEPLOYMENT"),
    ANDROID_REINFORCE("ANDROID_REINFORCE");

    @Getter
    private final String type;

    JobTypeEnum(String type) {
        this.type = type;
    }

}
