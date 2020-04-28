package com.baiyi.opscloud.factory.ticket.entry;

import com.baiyi.opscloud.domain.vo.server.OcServerGroupVO;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/27 3:18 下午
 * @Version 1.0
 */
@Builder
@Data
public class ServerGroupEntry implements ITicketEntry{

    private OcServerGroupVO.ServerGroup serverGroup;

    /** 需要管理员账户权限 **/
    private Boolean hasAdministratorAccount;

    public String getName() {
        return this.serverGroup.getName();
    }

}
