package com.baiyi.opscloud.controller.it;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetCompany;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetName;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetType;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;
import com.baiyi.opscloud.domain.vo.it.ItAssetVO;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.facade.it.ItAssetFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 4:00 下午
 * @Since 1.0
 */


@RestController
@RequestMapping("/it/asset")
@Api(tags = "IT资产平台")
public class ItAssetController {

    @Resource
    private ItAssetFacade itAssetFacade;

    @ApiOperation(value = "分页查询资产信息")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ItAssetVO.Asset>> queryOcItAssetPage(@RequestBody @Valid ItAssetParam.PageQuery pageQuery) {
        return new HttpResult<>(itAssetFacade.queryOcItAssetPage(pageQuery));
    }

    @ApiOperation(value = "查询资产")
    @GetMapping(value = "/id/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ItAssetVO.Asset> queryAssetById(@RequestParam Integer assetId) {
        return new HttpResult<>(itAssetFacade.queryAssetById(assetId));
    }

    @ApiOperation(value = "查询所有资产归属公司")
    @GetMapping(value = "/company/all/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcItAssetCompany>> queryOcItAssetCompanyAll() {
        return new HttpResult<>(itAssetFacade.queryOcItAssetCompanyAll());
    }

    @ApiOperation(value = "查询所有资产名称")
    @GetMapping(value = "/name/all/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcItAssetName>> queryOcItAssetNameAll() {
        return new HttpResult<>(itAssetFacade.queryOcItAssetNameAll());
    }

    @ApiOperation(value = "分页查询资产使用信息")
    @PostMapping(value = "/apply/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ItAssetVO.AssetApply>> queryOcItAssetApplyPage(@RequestBody @Valid ItAssetParam.ApplyPageQuery pageQuery) {
        return new HttpResult<>(itAssetFacade.queryOcItAssetApplyPage(pageQuery));
    }

    @ApiOperation(value = "分页查询资产处置信息")
    @PostMapping(value = "/dispose/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ItAssetVO.AssetDispose>> queryOcItAssetDisposePage(@RequestBody @Valid ItAssetParam.DisposePageQuery pageQuery) {
        return new HttpResult<>(itAssetFacade.queryOcItAssetDisposePage(pageQuery));
    }

    @ApiOperation(value = "查询资产分类树")
    @GetMapping(value = "/type/tree/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<TreeVO.Tree>> queryAssetTypeTree() {
        return new HttpResult<>(itAssetFacade.queryAssetTypeTree());
    }

    @ApiOperation(value = "刷新资产分类树")
    @GetMapping(value = "/type/tree/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<TreeVO.Tree>> refreshAssetTypeTree() {
        return new HttpResult<>(itAssetFacade.refreshAssetTypeTree());
    }

    @ApiOperation(value = "查询资产分类列表")
    @GetMapping(value = "/type/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcItAssetType>> queryOcItAssetTypeList() {
        return new HttpResult<>(itAssetFacade.queryOcItAssetTypeList());
    }

    @ApiOperation(value = "新增资产名称")
    @PostMapping(value = "/name/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addAssetName(@RequestBody ItAssetParam.AddAssetName param) {
        return new HttpResult<>(itAssetFacade.addAssetName(param));
    }

    @ApiOperation(value = "保存资产信息")
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveAsset(@RequestBody ItAssetVO.Asset asset) {
        return new HttpResult<>(itAssetFacade.saveAsset(asset));
    }

    @ApiOperation(value = "删除资产信息")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delAsset(@RequestParam Integer id) {
        return new HttpResult<>(itAssetFacade.delAsset(id));
    }

    @ApiOperation(value = "分配资产")
    @PostMapping(value = "/apply", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> applyAsset(@RequestBody ItAssetParam.ApplyAsset applyAsset) {
        return new HttpResult<>(itAssetFacade.applyAsset(applyAsset));
    }

    @ApiOperation(value = "归还资产")
    @PostMapping(value = "/return", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> returnAsset(@RequestBody ItAssetParam.ReturnAsset returnAsset) {
        return new HttpResult<>(itAssetFacade.returnAsset(returnAsset));
    }

    @ApiOperation(value = "更新归还资产")
    @PutMapping(value = "/return/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateReturnAsset(@RequestBody @Valid ItAssetParam.ReturnAsset returnAsset) {
        return new HttpResult<>(itAssetFacade.updateReturnAsset(returnAsset));
    }

    @ApiOperation(value = "处置资产")
    @PostMapping(value = "/dispose", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> disposeAsset(@RequestBody @Valid ItAssetParam.DisposeAsset disposeAsset) {
        return new HttpResult<>(itAssetFacade.disposeAsset(disposeAsset));
    }

    @ApiOperation(value = "还原资产")
    @GetMapping(value = "/able", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> ableAsset(@RequestParam Integer id) {
        return new HttpResult<>(itAssetFacade.ableAsset(id));
    }

    @ApiOperation(value = "更新资产使用信息")
    @PutMapping(value = "/apply/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateAssetApply(@RequestBody ItAssetVO.AssetApply assetApply) {
        return new HttpResult<>(itAssetFacade.updateAssetApply(assetApply));
    }

    @ApiOperation(value = "资产编码检验")
    @GetMapping(value = "/code/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> assetCodeCheck(@RequestParam String assetCode) {
        return new HttpResult<>(itAssetFacade.assetCodeCheck(assetCode));
    }

    @ApiOperation(value = "资产统计")
    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ItAssetVO.AssetStats> queryItAssetStatistics() {
        return new HttpResult<>(itAssetFacade.queryItAssetStatistics());
    }

    @ApiOperation(value = "资产总数统计")
    @GetMapping(value = "/total/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ItAssetVO.AssetTotalStats> queryItAssetTotalStats() {
        return new HttpResult<>(itAssetFacade.queryItAssetTotalStats());
    }

    @ApiOperation(value = "资产新增月度统计")
    @GetMapping(value = "/month/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ItAssetVO.AssetMonthStats> queryItAssetMonthStatistics() {
        return new HttpResult<>(itAssetFacade.queryItAssetMonthStatistics());
    }

    @ApiOperation(value = "刷新资产新增月度统计")
    @GetMapping(value = "/month/stats/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ItAssetVO.AssetMonthStats> refreshAssetMonthStatistics() {
        return new HttpResult<>(itAssetFacade.refreshAssetMonthStatistics());
    }

    @ApiOperation(value = "资产公司类型统计")
    @GetMapping(value = "/company/type/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ItAssetVO.AssetCompanyTypeStats> queryItAssetCompanyTypeStats() {
        return new HttpResult<>(itAssetFacade.queryItAssetCompanyTypeStats());
    }

    @ApiOperation(value = "导出IT资产")
    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> exportItAsset() {
        return new HttpResult<>(itAssetFacade.exportItAsset());
    }

    @ApiOperation(value = "导出IT资产派发记录")
    @GetMapping(value = "/apply/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> exportItAssetApply() {
        return new HttpResult<>(itAssetFacade.exportItAssetApply());
    }
}
