package com.baiyi.opscloud.facade.it;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetCompany;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetName;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetType;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;
import com.baiyi.opscloud.domain.vo.it.ItAssetVO;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 3:22 下午
 * @Since 1.0
 */
public interface ItAssetFacade {

    DataTable<ItAssetVO.Asset> queryOcItAssetPage(ItAssetParam.PageQuery pageQuery);

    BusinessWrapper<ItAssetVO.Asset> queryAssetById(int assetId);

    BusinessWrapper<List<TreeVO.Tree>> queryAssetTypeTree();

    BusinessWrapper<Boolean> addAssetName(ItAssetParam.AddAssetName param);

    BusinessWrapper<List<TreeVO.Tree>> refreshAssetTypeTree();

    BusinessWrapper<List<OcItAssetType>> queryOcItAssetTypeList();

    DataTable<ItAssetVO.AssetApply> queryOcItAssetApplyPage(ItAssetParam.ApplyPageQuery pageQuery);

    BusinessWrapper<Boolean> saveAsset(ItAssetVO.Asset asset);

    BusinessWrapper<Boolean> delAsset(Integer id);

    BusinessWrapper<Boolean> applyAsset(ItAssetParam.ApplyAsset applyAsset);

    BusinessWrapper<Boolean> returnAsset(ItAssetParam.ReturnAsset returnAsset);

    BusinessWrapper<Boolean> updateReturnAsset(ItAssetParam.ReturnAsset returnAsset);

    BusinessWrapper<Boolean> disposeAsset(ItAssetParam.DisposeAsset disposeAsset);

    BusinessWrapper<Boolean> ableAsset(Integer id);

    BusinessWrapper<Boolean> updateAssetApply(ItAssetVO.AssetApply assetApply);

    BusinessWrapper<List<OcItAssetCompany>> queryOcItAssetCompanyAll();

    BusinessWrapper<Boolean> assetCodeCheck(String assetCode);

    BusinessWrapper<List<OcItAssetName>> queryOcItAssetNameAll();

    DataTable<ItAssetVO.AssetDispose> queryOcItAssetDisposePage(ItAssetParam.DisposePageQuery pageQuery);

    BusinessWrapper<ItAssetVO.AssetStats> queryItAssetStatistics();

    BusinessWrapper<ItAssetVO.AssetMonthStats> queryItAssetMonthStatistics();

    BusinessWrapper<ItAssetVO.AssetMonthStats> refreshAssetMonthStatistics();

    BusinessWrapper<ItAssetVO.AssetTotalStats> queryItAssetTotalStats();

    BusinessWrapper<ItAssetVO.AssetCompanyTypeStats> queryItAssetCompanyTypeStats();

    BusinessWrapper<Boolean> exportItAsset();

    BusinessWrapper<Boolean> exportItAssetApply();
}
