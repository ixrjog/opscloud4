package com.baiyi.opscloud.datasource.ansible.play.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/9/26 5:18 下午
 * @Version 1.0
 */
public enum TaskMessageState {

    /**
     *
     */
    LOGIN("会话初始建立"),
    PLAY("播放");

    @Getter
    private final String desc;

    TaskMessageState(String desc) {
        this.desc = desc;
    }

}
