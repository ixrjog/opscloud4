package com.baiyi.opscloud.packer.task;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTaskMember;
import com.baiyi.opscloud.domain.vo.task.ServerTaskMemberVO;
import com.baiyi.opscloud.packer.task.delegate.ServerTaskMemberPackerDelegate;
import com.baiyi.opscloud.service.task.ServerTaskMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/24 2:05 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ServerTaskMemberPacker {

    private final ServerTaskMemberService serverTaskMemberService;

    private final ServerTaskMemberPackerDelegate delegate;

    public void wrap(ServerTaskMemberVO.IServerTaskMembers iServerTaskMembers) {
        List<ServerTaskMember> members = serverTaskMemberService.queryByServerTaskId(iServerTaskMembers.getServerTaskId());
        List<ServerTaskMemberVO.Member> serverTaskMembers = members.stream().map(e -> {
            ServerTaskMemberVO.Member vo = BeanCopierUtil.copyProperties(e, ServerTaskMemberVO.Member.class);
            delegate.wrap(vo);
            return vo;
        }).collect(Collectors.toList());
        iServerTaskMembers.setServerTaskMembers(serverTaskMembers);
    }

}
