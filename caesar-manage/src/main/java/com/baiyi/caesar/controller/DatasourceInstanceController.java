package com.baiyi.caesar.controller;

import com.baiyi.caesar.common.HttpResult;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.datasource.DsAssetParam;
import com.baiyi.caesar.domain.vo.datasource.DsAssetVO;
import com.baiyi.caesar.facade.datasource.DsInstanceFacade;
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
@RequestMapping("/datasource/instance")
@Api(tags = "数据源实例")
public class DatasourceInstanceController {

    @Resource
    private DsInstanceFacade dsInstanceFacade;

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

}
