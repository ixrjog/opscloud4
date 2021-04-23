package com.baiyi.opscloud.facade.it.impl;

import com.baiyi.opscloud.builder.it.ItAssetApplyBuilder;
import com.baiyi.opscloud.common.base.ItAssetApplyType;
import com.baiyi.opscloud.common.base.ItAssetStatus;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.decorator.it.ItAssetDecorator;
import com.baiyi.opscloud.decorator.it.ItAssetMonthStatsDecorator;
import com.baiyi.opscloud.decorator.it.ItAssetTypeTreeDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;
import com.baiyi.opscloud.domain.vo.it.ItAssetVO;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.event.handler.ItAssetApplyEventHandler;
import com.baiyi.opscloud.facade.export.ExportTaskFacade;
import com.baiyi.opscloud.facade.it.ItAssetFacade;
import com.baiyi.opscloud.service.it.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 3:23 下午
 * @Since 1.0
 */

@Slf4j
@Component("ItAssetFacade")
public class ItAssetFacadeImpl implements ItAssetFacade {

    @Resource
    private OcItAssetService ocItAssetService;

    @Resource
    private OcItAssetTypeService ocItAssetTypeService;

    @Resource
    private OcItAssetNameService ocItAssetNameService;

    @Resource
    private OcItAssetApplyService ocItAssetApplyService;

    @Resource
    private OcItAssetCompanyService ocItAssetCompanyService;

    @Resource
    private OcItAssetDisposeService ocItAssetDisposeService;

    @Resource
    private OcItAssetDashboardService ocItAssetDashboardService;

    @Resource
    private ItAssetDecorator itAssetDecorator;

    @Resource
    private ItAssetTypeTreeDecorator itAssetTypeTreeDecorator;

    @Resource
    private ItAssetMonthStatsDecorator itAssetMonthStatsDecorator;

    @Resource
    private ItAssetApplyEventHandler itAssetApplyEventHandler;

    @Resource
    private ExportTaskFacade exportTaskFacade;


    @Override
    public DataTable<ItAssetVO.Asset> queryOcItAssetPage(ItAssetParam.PageQuery pageQuery) {
        DataTable<OcItAsset> table = ocItAssetService.queryOcItAssetPage(pageQuery);
        List<ItAssetVO.Asset> page = itAssetDecorator.decoratorVOList(table.getData());
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<ItAssetVO.Asset> queryAssetById(int assetId) {
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetById(assetId);
        if (ocItAsset == null)
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_NOT_EXIST);
        return new BusinessWrapper<>(itAssetDecorator.decoratorVO(ocItAsset));
    }

    @Override
    public BusinessWrapper<List<TreeVO.Tree>> queryAssetTypeTree() {
        return new BusinessWrapper<>(itAssetTypeTreeDecorator.getAssetTypeTree());
    }

    @Override
    public BusinessWrapper<List<TreeVO.Tree>> refreshAssetTypeTree() {
        itAssetTypeTreeDecorator.evictPreview();
        return new BusinessWrapper<>(itAssetTypeTreeDecorator.getAssetTypeTree());
    }

    private OcItAssetType addAssetType(String assetType) {
        OcItAssetType ocItAssetType = new OcItAssetType();
        ocItAssetType.setAssetType(assetType);
        try {
            ocItAssetTypeService.addOcItAssetType(ocItAssetType);
            return ocItAssetType;
        } catch (Exception e) {
            log.error("新增assetType失败，assetType:{}", assetType, e);
            return null;
        }
    }

    @Override
    public BusinessWrapper<List<OcItAssetType>> queryOcItAssetTypeList() {
        List<OcItAssetType> typeList = ocItAssetTypeService.queryOcItAssetTypeAll();
        return new BusinessWrapper<>(typeList);
    }

    @Override
    public BusinessWrapper<Boolean> addAssetName(ItAssetParam.AddAssetName param) {
        OcItAssetType ocItAssetType = ocItAssetTypeService.queryOcItAssetTypeByType(param.getAssetType());
        if (ocItAssetType == null)
            ocItAssetType = addAssetType(param.getAssetType());
        if (ocItAssetType == null)
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_NAME_ADD_FAIL);
        OcItAssetName ocItAssetName = ocItAssetNameService.queryOcItAssetNameByName(param.getAssetName());
        if (ocItAssetName != null)
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_NAME_EXIST);
        OcItAssetName newOcItAssetName = new OcItAssetName();
        newOcItAssetName.setAssetTypeId(ocItAssetType.getId());
        newOcItAssetName.setAssetName(param.getAssetName());
        newOcItAssetName.setRamark(param.getRemark());
        try {
            ocItAssetNameService.addOcItAssetName(newOcItAssetName);
            itAssetTypeTreeDecorator.evictPreview();
            return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            log.error("新增assetName失败，assetName:{}", param.getAssetName(), e);
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_NAME_ADD_FAIL);
        }
    }

    @Override
    public DataTable<ItAssetVO.AssetApply> queryOcItAssetApplyPage(ItAssetParam.ApplyPageQuery pageQuery) {
        DataTable<OcItAssetApply> table = ocItAssetApplyService.queryOcItAssetApplyPage(pageQuery);
        List<ItAssetVO.AssetApply> page = itAssetDecorator.decoratorApplyVOList(table.getData());
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> saveAsset(ItAssetVO.Asset asset) {
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetById(asset.getId());
        OcItAsset newOcItAsset = BeanCopierUtils.copyProperties(asset, OcItAsset.class);
        newOcItAsset.setAssetAddTime(new Date(asset.getAssetAddTimestamp()));
        if (ocItAsset == null) {
            try {
                ocItAssetService.addOcItAsset(newOcItAsset);
                return BusinessWrapper.SUCCESS;
            } catch (Exception e) {
                log.error("新增资产失败，assetCode:{}", asset.getAssetCode(), e);
                return new BusinessWrapper<>(ErrorEnum.IT_ASSET_SAVE_FAIL);
            }
        } else {
            newOcItAsset.setId(ocItAsset.getId());
            ocItAssetService.updateOcItAsset(newOcItAsset);
            return BusinessWrapper.SUCCESS;
        }
    }

    @Override
    public BusinessWrapper<Boolean> delAsset(Integer id) {
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetById(id);
        if (ocItAsset == null)
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_NOT_EXIST);
        OcItAssetApply ocItAssetApply = ocItAssetApplyService.queryOcItAssetApplyByAssetId(id);
        if (ocItAssetApply != null)
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_HAS_USED);
        ocItAssetService.deleteOcItAssetById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> applyAsset(ItAssetParam.ApplyAsset applyAsset) {
        OcItAssetApply ocItAssetApply = ItAssetApplyBuilder.build(applyAsset);
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetById(applyAsset.getAssetId());
        if (ocItAsset == null)
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_NOT_EXIST);
        if (!ocItAsset.getAssetStatus().equals(ItAssetStatus.FREE.getType()))
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_STATUS_NOT_FREE);
        if (applyAsset.getApplyType().equals(ItAssetApplyType.USE.getType()))
            ocItAsset.setAssetStatus(ItAssetStatus.USED.getType());
        if (applyAsset.getApplyType().equals(ItAssetApplyType.BORROW.getType())) {
            ocItAsset.setAssetStatus(ItAssetStatus.BORROW.getType());
            if (applyAsset.getExpectReturnTime() != null)
                ocItAssetApply.setExpectReturnTime(new Date(applyAsset.getExpectReturnTime()));
        }
        ocItAsset.setUseTime(new Date(applyAsset.getApplyTime()));
        try {
            ocItAssetApplyService.addOcItAssetApply(ocItAssetApply);
            ocItAssetService.updateOcItAsset(ocItAsset);
            itAssetApplyEventHandler.eventPost(applyAsset);
            return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            log.error("分配资产失败，assetId:{}", applyAsset.getAssetId(), e);
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_APPLY_SAVE_FAIL);
        }
    }

    @Override
    public BusinessWrapper<Boolean> returnAsset(ItAssetParam.ReturnAsset returnAsset) {
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetById(returnAsset.getAssetId());
        if (ocItAsset == null)
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_NOT_EXIST);
        if (ocItAsset.getAssetStatus().equals(ItAssetStatus.DISPOSE.getType()))
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_HAS_DISABLE);
        ocItAsset.setAssetStatus(ItAssetStatus.FREE.getType());
        ocItAsset.setUseTime(null);
        ocItAssetService.updateOcItAsset(ocItAsset);
        OcItAssetApply ocItAssetApply = ocItAssetApplyService.queryOcItAssetApplyByAssetId(returnAsset.getAssetId());
        ocItAssetApply.setIsReturn(true);
        ocItAssetApply.setReturnTime(new Date(returnAsset.getReturnTime()));
        ocItAssetApplyService.updateOcItAssetApply(ocItAssetApply);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateReturnAsset(ItAssetParam.ReturnAsset returnAsset) {
        OcItAssetApply ocItAssetApply = ocItAssetApplyService.queryOcItAssetApplyById(returnAsset.getId());
        ocItAssetApply.setReturnTime(new Date(returnAsset.getReturnTime()));
        ocItAssetApplyService.updateOcItAssetApply(ocItAssetApply);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> disposeAsset(ItAssetParam.DisposeAsset disposeAsset) {
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetById(disposeAsset.getAssetId());
        if (!ocItAsset.getAssetStatus().equals(ItAssetStatus.FREE.getType()))
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_HAS_USED);
        OcItAssetDispose ocItAssetDispose = BeanCopierUtils.copyProperties(disposeAsset, OcItAssetDispose.class);
        ocItAssetDispose.setDisposeTime(new Date(disposeAsset.getDisposeTime()));
        try {
            ocItAssetDisposeService.addOcItAssetDispose(ocItAssetDispose);
            ocItAsset.setAssetStatus(ItAssetStatus.DISPOSE.getType());
            ocItAssetService.updateOcItAsset(ocItAsset);
            return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            log.error("处置资产失败，资产ID:{}", disposeAsset.getAssetId(), e);
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_DISPOSE_FAIL);
        }
    }

    @Override
    public BusinessWrapper<Boolean> ableAsset(Integer id) {
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetById(id);
        if (!ocItAsset.getAssetStatus().equals(ItAssetStatus.DISPOSE.getType()))
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_NOT_DISABLE);
        OcItAssetDispose ocItAssetDispose = ocItAssetDisposeService.queryOcItAssetDisposeByAssetId(ocItAsset.getId());
        if (ocItAssetDispose == null) {
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_ABLE_FAIL);
        }
        ocItAssetDisposeService.delOcItAssetDispose(ocItAssetDispose.getId());
        ocItAsset.setAssetStatus(ItAssetStatus.FREE.getType());
        ocItAssetService.updateOcItAsset(ocItAsset);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateAssetApply(ItAssetVO.AssetApply assetApply) {
        OcItAssetApply ocItAssetApply = BeanCopierUtils.copyProperties(assetApply, OcItAssetApply.class);
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetById(assetApply.getAssetId());

        if (assetApply.getApplyType().equals(ItAssetApplyType.USE.getType())) {
            ocItAssetApply.setExpectReturnTime(null);
            ocItAsset.setAssetStatus(ItAssetStatus.USED.getType());
        }
        if (assetApply.getApplyType().equals(ItAssetApplyType.BORROW.getType())) {
            ocItAsset.setAssetStatus(ItAssetStatus.BORROW.getType());
        }
        try {
            ocItAssetApplyService.updateOcItAssetApply(ocItAssetApply);
            ocItAssetService.updateOcItAsset(ocItAsset);
            return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            log.error("更新派发资产失败", e);
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_APPLY_UPDATE_FAIL);
        }
    }

    @Override
    public BusinessWrapper<List<OcItAssetCompany>> queryOcItAssetCompanyAll() {
        List<OcItAssetCompany> companyList = ocItAssetCompanyService.queryOcItAssetCompanyAll();
        return new BusinessWrapper<>(companyList);
    }

    @Override
    public BusinessWrapper<Boolean> assetCodeCheck(String assetCode) {
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetByAssetCode(assetCode);
        if (ocItAsset != null)
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_CODE_EXIST);
        if (!RegexUtils.isAssetCodeRule(assetCode))
            return new BusinessWrapper<>(ErrorEnum.IT_ASSET_CODE_ERR);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<List<OcItAssetName>> queryOcItAssetNameAll() {
        List<OcItAssetName> nameList = ocItAssetNameService.queryOcItAssetNameAll();
        return new BusinessWrapper<>(nameList);
    }

    @Override
    public DataTable<ItAssetVO.AssetDispose> queryOcItAssetDisposePage(ItAssetParam.DisposePageQuery pageQuery) {
        DataTable<OcItAssetDispose> table = ocItAssetDisposeService.queryOcItAssetDisposePage(pageQuery);
        List<ItAssetVO.AssetDispose> page = itAssetDecorator.decoratorDisposeVOList(table.getData());
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<ItAssetVO.AssetStats> queryItAssetStatistics() {
        ItAssetVO.AssetStats statistics = new ItAssetVO.AssetStats();
        List<ItAssetVO.StatsData> nameStatistics = ocItAssetDashboardService.queryItAssetNameStatistics();
        List<ItAssetVO.StatsData> typeStatistics = ocItAssetDashboardService.queryItAssetTypeStatistics();
        statistics.setNameStatistics(nameStatistics);
        statistics.setTypeStatistics(typeStatistics);
        return new BusinessWrapper<>(statistics);
    }

    @Override
    public BusinessWrapper<ItAssetVO.AssetMonthStats> queryItAssetMonthStatistics() {
        return new BusinessWrapper<>(itAssetMonthStatsDecorator.queryItAssetMonthStatistics());
    }

    @Override
    public BusinessWrapper<ItAssetVO.AssetMonthStats> refreshAssetMonthStatistics() {
        itAssetMonthStatsDecorator.evictPreview();
        return new BusinessWrapper<>(itAssetMonthStatsDecorator.queryItAssetMonthStatistics());
    }

    @Override
    public BusinessWrapper<ItAssetVO.AssetTotalStats> queryItAssetTotalStats() {
        ItAssetVO.AssetTotalStats totalStats = new ItAssetVO.AssetTotalStats();
        totalStats.setTotal(ocItAssetService.countOcItAsset());
        totalStats.setFreeTotal(ocItAssetService.countOcItAssetByStatus(ItAssetStatus.FREE.getType()));
        totalStats.setUsedTotal(ocItAssetService.countOcItAssetByStatus(ItAssetStatus.USED.getType()));
        totalStats.setBorrowTotal(ocItAssetService.countOcItAssetByStatus(ItAssetStatus.BORROW.getType()));
        totalStats.setDisposeTotal(ocItAssetService.countOcItAssetByStatus(ItAssetStatus.DISPOSE.getType()));
        return new BusinessWrapper<>(totalStats);
    }

    @Override
    public BusinessWrapper<ItAssetVO.AssetCompanyTypeStats> queryItAssetCompanyTypeStats() {
        List<ItAssetVO.AssetCompanyStats> companyStats = ocItAssetDashboardService.queryAssetCompanyStats();
        Integer purchaseTotal = companyStats.stream()
                .filter(x -> x.getType().equals(1))
                .mapToInt(ItAssetVO.AssetCompanyStats::getValue)
                .sum();
        Integer leaseTotal = companyStats.stream()
                .filter(x -> x.getType().equals(2))
                .mapToInt(ItAssetVO.AssetCompanyStats::getValue)
                .sum();
        ItAssetVO.AssetCompanyTypeStats companyTypeStats = new ItAssetVO.AssetCompanyTypeStats();
        companyTypeStats.setLeaseTotal(leaseTotal);
        companyTypeStats.setPurchaseTotal(purchaseTotal);
        companyTypeStats.setAssetCompanyStats(companyStats);
        return new BusinessWrapper<>(companyTypeStats);
    }

    @Override
    public BusinessWrapper<Boolean> exportItAsset() {
        exportTaskFacade.exportAssetAll(SessionUtils.getUsername());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> exportItAssetApply() {
        exportTaskFacade.exportAssetApplyAll(SessionUtils.getUsername());
        return BusinessWrapper.SUCCESS;
    }
}
