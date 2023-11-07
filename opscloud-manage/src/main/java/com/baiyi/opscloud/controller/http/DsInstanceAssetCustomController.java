package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.datasource.DsCustomAssetParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.facade.datasource.instance.ApolloAssetFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author baiyi
 * @Date 2023/7/17 10:45
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/datasource/instance")
@Tag(name = "数据源实例")
@RequiredArgsConstructor
public class DsInstanceAssetCustomController {

    private final ApolloAssetFacade apolloAssetFacade;

    @Operation(summary = "分页查询Apollo:release资产列表")
    @PostMapping(value = "/apollo/release/asset/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DsAssetVO.Asset>> queryApolloReleaseAssetPage(@RequestBody @Valid DsCustomAssetParam.ApolloReleaseAssetPageQuery pageQuery) {
        return new HttpResult<>(apolloAssetFacade.queryApolloReleaseAssetPage(pageQuery));
    }

}