package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/1/18 10:35 AM
 * @Version 1.0
 */
@Getter
public enum SubscribeStatusConstants {

    CREATE("创建人"),
    AUDIT("审批人"),
    CARBON_COPY("抄送");

    SubscribeStatusConstants(String comment) {
        this.comment = comment;
    }

    private final String comment;

}