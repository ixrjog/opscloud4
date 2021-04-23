package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.param.account.AccountParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAccountMapper extends Mapper<OcAccount> {

    List<OcAccount> queryOcAccountByParam(AccountParam.AccountPageQuery pageQuery);
}