package com.baiyi.caesar.facade.datasource;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.datasource.DsAccountGroupParam;
import com.baiyi.caesar.domain.param.datasource.DsAccountParam;
import com.baiyi.caesar.domain.param.datasource.DsAssetParam;
import com.baiyi.caesar.domain.vo.datasource.DsAccountGroupVO;
import com.baiyi.caesar.domain.vo.datasource.DsAccountVO;
import com.baiyi.caesar.domain.vo.datasource.DsAssetVO;

/**
 * @Author baiyi
 * @Date 2021/6/11 10:18 上午
 * @Version 1.0
 */
public interface DsInstanceFacade {

//    void pullAccount(int id);
//
//    void pullAccountGroup(int id);

    DataTable<DsAccountVO.Account> queryAccountPage(DsAccountParam.AccountPageQuery pageQuery);

    DataTable<DsAccountGroupVO.Group> queryAccountGroupPage(DsAccountGroupParam.GroupPageQuery pageQuery);

    DataTable<DsAssetVO.Asset> queryAssetPage(DsAssetParam.AssetPageQuery pageQuery);
}
