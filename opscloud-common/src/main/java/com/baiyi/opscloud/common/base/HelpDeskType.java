package com.baiyi.opscloud.common.base;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2020/11/23 3:03 下午
 * @Version 1.0
 */
@Getter
public enum HelpDeskType {

    TYPE0(0, "桌面维护"),
    TYPE1(1, "资产管理"),
    TYPE2(2, "网络管理"),
    TYPE3(3, "用户管理"),
    TYPE4(4, "邮箱管理"),
    TYPE5(5, "IT知识库"),
    TYPE6(6, "监控管理"),
    TYPE7(7, "行为管理");

    private int type;
    private String desc;

    HelpDeskType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
