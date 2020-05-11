package com.baiyi.opscloud.xterm.message;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/11 7:24 下午
 * @Version 1.0
 */
@Data
public class XTermBatchCommandMSMessage extends BaseXTermWSMessage {

    private Boolean isBatch; // 会话批量指令

}