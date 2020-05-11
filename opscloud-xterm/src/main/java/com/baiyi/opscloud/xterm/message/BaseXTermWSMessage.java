package com.baiyi.opscloud.xterm.message;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:45 上午
 * @Version 1.0
 */
@Data
public class BaseXTermWSMessage {

    private String id;
    private String token;
    private String status;

    private Integer xtermWidth;
    private Integer xtermHeight;
}
