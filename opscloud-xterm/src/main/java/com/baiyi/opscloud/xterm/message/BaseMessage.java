package com.baiyi.opscloud.xterm.message;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:45 上午
 * @Version 1.0
 */
@Data
public class BaseMessage {

    private String id;
    private String token;
    private String status;
    // 0 普通账户 1 管理员账户
    private Integer loginUserType;

    private Integer xtermWidth;
    private Integer xtermHeight;
}
