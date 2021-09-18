package com.baiyi.opscloud.service.task.impl;

import com.baiyi.opscloud.domain.generator.opscloud.ServerTaskMember;
import com.baiyi.opscloud.mapper.opscloud.ServerTaskMemberMapper;
import com.baiyi.opscloud.service.task.ServerTaskMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/9/18 5:32 下午
 * @Version 1.0
 */
@Service
public class ServerTaskMemberServiceImpl implements ServerTaskMemberService {

    @Resource
    private ServerTaskMemberMapper serverTaskMemberMapper;

    @Override
    public void add(ServerTaskMember serverTaskMember) {
        serverTaskMemberMapper.insert(serverTaskMember);
    }
}
