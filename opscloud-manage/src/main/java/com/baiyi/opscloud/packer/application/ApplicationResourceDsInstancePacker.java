package com.baiyi.opscloud.packer.application;

import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/2/23 10:04 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApplicationResourceDsInstancePacker implements IWrapper<ApplicationResourceVO.Resource> {

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsInstancePacker dsInstancePacker;

    /**
     * 注入数据源实例信息
     * @param resource
     * @param iExtend
     */
    @Override
    public void wrap(ApplicationResourceVO.Resource resource, IExtend iExtend) {
        if (resource.getBusinessType() == BusinessTypeEnum.ASSET.getType()) {
            DatasourceInstanceAsset asset = dsInstanceAssetService.getById(resource.getBusinessId());
            resource.setInstanceUuid(asset.getInstanceUuid());
            dsInstancePacker.wrap(resource);
        }
    }

}
