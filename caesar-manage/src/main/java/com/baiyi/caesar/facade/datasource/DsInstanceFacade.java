package com.baiyi.caesar.facade.datasource;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.datasource.DsAccountParam;
import com.baiyi.caesar.domain.vo.datasource.DsAccountVO;

/**
 * @Author baiyi
 * @Date 2021/6/11 10:18 上午
 * @Version 1.0
 */
public interface DsInstanceFacade {

    void pullAccount(int id);

    DataTable<DsAccountVO.Account> queryAccountPage(DsAccountParam.AccountPageQuery pageQuery);
}
