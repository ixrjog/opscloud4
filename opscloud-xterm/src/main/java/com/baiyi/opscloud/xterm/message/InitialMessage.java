package com.baiyi.opscloud.xterm.message;

import lombok.Data;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/5/9 5:07 下午
 * @Version 1.0
 */
@Data
public class InitialMessage extends BaseMessage {

    // {"uuid":"43621c089207449799c6ef6bb1de0061","loginUserType":0,"instanceIds":["vm-springboot-3","vm-springboot-1","vm-springboot-2"],"status":"INITIAL"}
    private Set<String> instanceIds;

    // 服务器树鉴权uuid
    private String uuid;

}
