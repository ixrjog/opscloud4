package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.datasource.DsAssetSubscriptionParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetSubscriptionVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetSubscriptionFacade;
import com.baiyi.opscloud.facade.datasource.DsInstanceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/6/11 10:10 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/datasource/instance")
@Api(tags = "数据源实例")
public class DatasourceInstanceController {

    @Resource
    private DsInstanceFacade dsInstanceFacade;

    @Resource
    private DsInstanceAssetSubscriptionFacade dsInstanceAssetSubscriptionFacade;

    @ApiOperation(value = "分页查询数据源资产列表")
    @PostMapping(value = "/asset/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DsAssetVO.Asset>> queryAssetPage(@RequestBody @Valid DsAssetParam.AssetPageQuery pageQuery) {
        return new HttpResult<>(dsInstanceFacade.queryAssetPage(pageQuery));
    }

    @ApiOperation(value = "拉取数据源资产信息")
    @PutMapping(value = "/asset/pull", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pullAsset(@RequestBody DsAssetParam.PullAsset pullAssetParam) {
        dsInstanceFacade.pullAsset(pullAssetParam);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的资产")
    @DeleteMapping(value = "/asset/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteAssetById(@RequestParam @Valid int id) {
        dsInstanceFacade.deleteAssetById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "设置数据源配置文件")
    @PutMapping(value = "/asset/set/config", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setDsInstanceConfig(@RequestBody DsAssetParam.SetDsInstanceConfig setDsInstanceConfig) {
        dsInstanceFacade.setDsInstanceConfig(setDsInstanceConfig);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "扫描资产与业务对象关系")
    @PutMapping(value = "/asset/business/scan", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> scanAssetBusiness(@RequestBody DsAssetParam.ScanAssetBusiness scanAssetBusiness) {
        dsInstanceFacade.scanAssetBusiness(scanAssetBusiness);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询数据源资产订阅列表")
    @PostMapping(value = "/asset/subscription/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DsAssetSubscriptionVO.AssetSubscription>> queryAssetSubscriptionPage(@RequestBody @Valid DsAssetSubscriptionParam.AssetSubscriptionPageQuery pageQuery) {
        return new HttpResult<>(dsInstanceAssetSubscriptionFacade.queryAssetSubscriptionPage(pageQuery));
    }

    @ApiOperation(value = "新增数据源资产订阅")
    @PostMapping(value = "/asset/subscription/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addAssetSubscription(@RequestBody @Valid DsAssetSubscriptionVO.AssetSubscription assetSubscription) {
        dsInstanceAssetSubscriptionFacade.addAssetSubscription(assetSubscription);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新数据源资产订阅")
    @PutMapping(value = "/asset/subscription/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateAssetSubscription(@RequestBody @Valid DsAssetSubscriptionVO.AssetSubscription assetSubscription) {
        dsInstanceAssetSubscriptionFacade.updateAssetSubscription(assetSubscription);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "发布数据源资产订阅")
    @PutMapping(value = "/asset/subscription/publish", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> publishAssetSubscription(@RequestParam @Valid int id) {
        dsInstanceAssetSubscriptionFacade.publishAssetSubscriptionById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的数据源资产订阅")
    @DeleteMapping(value = "/asset/subscription/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteAssetSubscriptionById(@RequestParam @Valid int id) {
        dsInstanceAssetSubscriptionFacade.deleteAssetSubscriptionById(id);
        return HttpResult.SUCCESS;
    }


}
