package com.baiyi.opscloud.factory.ticket.entry;

import com.baiyi.opscloud.domain.param.gitlab.GitlabGroupParam;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/2/23 3:09 下午
 * @Since 1.0
 */

@Data
public class GitlabGroupMemberEntry implements ITicketEntry {

    private GitlabGroupParam.AddMember addMember;

    @Override
    public String getName() {
        return addMember.getUsername();
    }
}
