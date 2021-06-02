package com.baiyi.caesar.facade.server;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.server.ServerAccountParam;
import com.baiyi.caesar.domain.vo.server.ServerAccountVO;

/**
 * @Author baiyi
 * @Date 2021/5/25 11:26 上午
 * @Version 1.0
 */
public interface ServerAccountFacade {

    DataTable<ServerAccountVO.Account> queryServerAccountPage(ServerAccountParam.ServerAccountPageQuery pageQuery);

    void addServerAccount(ServerAccountVO.Account account);

    void updateServerAccount(ServerAccountVO.Account account);

    void updateServerAccountPermission(ServerAccountParam.UpdateServerAccountPermission updatePermission);
}
