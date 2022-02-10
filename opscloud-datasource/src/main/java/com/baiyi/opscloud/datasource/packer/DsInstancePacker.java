package com.baiyi.opscloud.datasource.packer;

import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/19 5:20 下午
 * @Version 1.0
 */
@Component
public class DsInstancePacker {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceService dsInstanceService;

    public static DsInstanceVO.Instance toVO(DatasourceInstance datasourceInstance) {
        return BeanCopierUtil.copyProperties(datasourceInstance, DsInstanceVO.Instance.class);
    }

    public void wrap(DsInstanceVO.IDsInstance iDsInstance) {
        if (StringUtils.isEmpty(iDsInstance.getInstanceUuid())) return;
        DatasourceInstance datasourceInstance = dsInstanceService.getByUuid(iDsInstance.getInstanceUuid());
        if (datasourceInstance == null) return;
        iDsInstance.setInstance(toVO(datasourceInstance));
    }

    @TagsWrapper
    public void wrap(DsInstanceVO.Instance instance, IExtend iExtend) {
        if (iExtend.getExtend()) {
            List<String> assetTypes = dsInstanceAssetService.queryInstanceAssetTypes(instance.getUuid());
            instance.setAssetDetails(
                    assetTypes.stream().map(e ->
                            DsInstanceVO.AssetDetail.builder()
                                    .assetType(e)
                                    .assetSize(dsInstanceAssetService.countByInstanceAssetType(instance.getUuid(), e))
                                    .build()
                    ).collect(Collectors.toList())
            );
        }
    }


}