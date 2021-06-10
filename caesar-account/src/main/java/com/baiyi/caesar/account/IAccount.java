package com.baiyi.caesar.account;

import com.baiyi.caesar.common.annotation.SingleTask;
import com.baiyi.caesar.domain.vo.datasource.DatasourceInstanceVO;

/**
 * @Author baiyi
 * @Date 2021/6/10 10:27 上午
 * @Version 1.0
 */
public interface IAccount {

    @SingleTask(name = "PullLdapAccount", lockSecond = 5 * 60)
    void pullAccount(DatasourceInstanceVO.Instance dsInstance);

    String getKey();
}
