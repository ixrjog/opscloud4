package com.baiyi.opscloud.factory.ticket.entry;

import com.baiyi.opscloud.domain.vo.auth.RoleVO;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/27 6:10 下午
 * @Version 1.0
 */
@Builder
@Data
public class AuthRoleEntry implements ITicketEntry {

    private RoleVO.Role role;

    public String getName() {
        return this.role.getRoleName();
    }

}
