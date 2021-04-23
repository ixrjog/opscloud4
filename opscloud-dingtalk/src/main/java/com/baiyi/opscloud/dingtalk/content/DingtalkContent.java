package com.baiyi.opscloud.dingtalk.content;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/13 6:17 下午
 * @Since 1.0
 */

@Data
@Builder
public class DingtalkContent implements Serializable {
    private static final long serialVersionUID = 2492194282507637512L;
    /**
     * 通知地址
     */
    private String webHook;

    private String msg;
}
