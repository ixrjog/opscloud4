package com.baiyi.caesar.packer.datasource;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.vo.datasource.DsAccountVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/11 11:10 上午
 * @Version 1.0
 */
public class DsAccountPacker {

    public static List<DsAccountVO.Account> wrapVOList(List<DatasourceAccount> data, IExtend iExtend) {
        List<DsAccountVO.Account> voList = BeanCopierUtil.copyListProperties(data, DsAccountVO.Account.class);
        return voList;
    }
}
