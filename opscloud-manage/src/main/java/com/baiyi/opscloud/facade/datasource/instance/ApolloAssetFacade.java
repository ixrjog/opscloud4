package com.baiyi.opscloud.facade.datasource.instance;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsCustomAssetParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/7/17 10:54
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApolloAssetFacade {

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsInstanceService dsInstanceService;

    private final DsAssetPacker dsAssetPacker;

    public DataTable<DsAssetVO.Asset> queryApolloReleaseAssetPage(DsCustomAssetParam.ApolloReleaseAssetPageQuery pageQuery) {
        DatasourceInstance dsInstance = dsInstanceService.getById(pageQuery.getInstanceId());
        pageQuery.setInstanceUuid(dsInstance.getUuid());
        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryApolloAssetPageByParam(pageQuery);
        List<DsAssetVO.Asset> data = BeanCopierUtil.copyListProperties(table.getData(), DsAssetVO.Asset.class)
                .stream()
                .peek(e -> dsAssetPacker.wrap(e, pageQuery)
                ).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

}
