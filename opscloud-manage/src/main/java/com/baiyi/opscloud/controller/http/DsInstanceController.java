package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.datasource.facade.DsInstanceFacade;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.datasource.DsAssetSubscriptionParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetSubscriptionVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetFacade;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetSubscriptionFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/6/11 10:10 上午
 * @Version 1.0
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/api/datasource/instance")
@Tag(name = "数据源实例")
@RequiredArgsConstructor
public class DsInstanceController {

    private final DsInstanceFacade instanceFacade;

    private final DsInstanceAssetFacade assetFacade;

    private final DsInstanceAssetSubscriptionFacade assetSubscriptionFacade;

    @Operation(summary = "分页查询数据源资产列表")
    @PostMapping(value = "/asset/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DsAssetVO.Asset>> queryAssetPage(@RequestBody @Valid DsAssetParam.AssetPageQuery pageQuery) {
        return new HttpResult<>(assetFacade.queryAssetPage(pageQuery));
    }

    @Operation(summary = "设置数据源资产是否有效")
    @PutMapping(value = "/asset/active/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setAssetActiveByAssetId(@RequestParam @Valid int assetId) {
        assetFacade.setAssetActiveByAssetId(assetId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "拉取数据源资产信息")
    @PutMapping(value = "/asset/pull", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pullAsset(@RequestBody DsAssetParam.PullAsset pullAsset) {
        instanceFacade.pullAsset(pullAsset);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的资产")
    @DeleteMapping(value = "/asset/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteAssetById(@RequestParam @Valid int id) {
        assetFacade.deleteAssetByAssetId(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除数据源实例下指定类型的所有资产")
    @DeleteMapping(value = "/asset/type/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteAssetByType(@RequestParam @Valid int instanceId, @RequestParam @Valid String assetType) {
        assetFacade.deleteAssetByType(instanceId, assetType);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "设置数据源配置文件")
    @PutMapping(value = "/asset/set/config", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setDsInstanceConfig(@RequestBody DsAssetParam.SetDsInstanceConfig setDsInstanceConfig) {
        instanceFacade.setDsInstanceConfig(setDsInstanceConfig);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "扫描资产与业务对象关系")
    @PutMapping(value = "/asset/business/scan", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> scanAssetBusiness(@RequestBody DsAssetParam.ScanAssetBusiness scanAssetBusiness) {
        instanceFacade.scanAssetBusiness(scanAssetBusiness);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "分页查询数据源资产订阅列表")
    @PostMapping(value = "/asset/subscription/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DsAssetSubscriptionVO.AssetSubscription>> queryAssetSubscriptionPage(@RequestBody @Valid DsAssetSubscriptionParam.AssetSubscriptionPageQuery pageQuery) {
        return new HttpResult<>(assetSubscriptionFacade.queryAssetSubscriptionPage(pageQuery));
    }

    @Operation(summary = "新增数据源资产订阅")
    @PostMapping(value = "/asset/subscription/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addAssetSubscription(@RequestBody @Valid DsAssetSubscriptionParam.AssetSubscription assetSubscription) {
        assetSubscriptionFacade.addAssetSubscription(assetSubscription);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新数据源资产订阅")
    @PutMapping(value = "/asset/subscription/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateAssetSubscription(@RequestBody @Valid DsAssetSubscriptionParam.AssetSubscription assetSubscription) {
        assetSubscriptionFacade.updateAssetSubscription(assetSubscription);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "发布数据源资产订阅")
    @PutMapping(value = "/asset/subscription/publish", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> publishAssetSubscription(@RequestParam @Valid int id) {
        assetSubscriptionFacade.publishAssetSubscriptionById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的数据源资产订阅")
    @DeleteMapping(value = "/asset/subscription/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteAssetSubscriptionById(@RequestParam @Valid int id) {
        assetSubscriptionFacade.deleteAssetSubscriptionById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "推送数据源资产信息")
    @PutMapping(value = "/asset/push", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pushAsset(@RequestBody DsAssetParam.PushAsset pushAsset) {
        instanceFacade.pushAsset(pushAsset);
        return HttpResult.SUCCESS;
    }

}