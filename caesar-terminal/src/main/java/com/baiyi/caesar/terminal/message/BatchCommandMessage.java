package com.baiyi.caesar.terminal.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2020/5/11 7:24 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BatchCommandMessage extends BaseMessage {

    private Boolean isBatch; // 会话批量指令

}