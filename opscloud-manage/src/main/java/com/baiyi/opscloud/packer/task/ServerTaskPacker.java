package com.baiyi.opscloud.packer.task;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.annotation.DurationWrapper;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.task.ServerTaskVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.packer.user.UserPacker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/9/24 1:51 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ServerTaskPacker implements IWrapper<ServerTaskVO.ServerTask> {

    private final ServerTaskMemberPacker serverTaskMemberPacker;

    private final AnsiblePlaybookPacker ansiblePlaybookPacker;

    private final UserPacker userPacker;

    @Override
    @AgoWrapper
    @DurationWrapper
    public void wrap(ServerTaskVO.ServerTask serverTask, IExtend iExtend) {
        if (iExtend.getExtend()) {
            serverTaskMemberPacker.wrap(serverTask);
            // playbook
            ansiblePlaybookPacker.wrap(serverTask);
            // user
            userPacker.wrap(serverTask, SimpleExtend.NOT_EXTEND);
        }
    }

}
