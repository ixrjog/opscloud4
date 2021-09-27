package com.baiyi.opscloud.ansible.play.message;

import com.baiyi.opscloud.ansible.play.message.base.BasePlayMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/9/26 5:31 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServerTaskPlayMessage extends BasePlayMessage {

    private Set<Integer> serverTaskMemberIds;
}
