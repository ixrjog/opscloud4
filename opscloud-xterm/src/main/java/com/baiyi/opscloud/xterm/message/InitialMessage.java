package com.baiyi.opscloud.xterm.message;

import lombok.Data;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/5/9 5:07 下午
 * @Version 1.0
 */
@Data
public class InitialMessage {

    // {"uuid":"43621c089207449799c6ef6bb1de0061","loginUserType":0,"instanceIds":["vm-springboot-3","vm-springboot-1","vm-springboot-2"],"status":"INITIAL"}
    private String uuid;
    private String token;
    // 0 普通账户 1 管理员账户
    private Integer loginUserType;
    private Set<String> instanceIds;

    private Integer xtermWidth;
    private Integer xtermHeight;

    // INITIAL
    private String status;

}
