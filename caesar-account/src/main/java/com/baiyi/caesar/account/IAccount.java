package com.baiyi.caesar.account;

import com.baiyi.caesar.domain.vo.datasource.DsInstanceVO;

/**
 * @Author baiyi
 * @Date 2021/6/10 10:27 上午
 * @Version 1.0
 */
public interface IAccount {

    void pullAccount(DsInstanceVO.Instance dsInstance);

    String getKey();
}
