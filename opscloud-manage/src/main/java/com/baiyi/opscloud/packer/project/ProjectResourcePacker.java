package com.baiyi.opscloud.packer.project;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.FunctionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IRelation;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.project.ProjectResourceVO;
import com.baiyi.opscloud.packer.IWrapperRelation;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2023/5/18 5:48 PM
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class ProjectResourcePacker implements IWrapperRelation<ProjectResourceVO.Resource> {

    private final DsInstanceAssetService assetService;

    private final DsAssetPacker dsAssetPacker;

    @Override
    public void wrap(ProjectResourceVO.Resource vo, IExtend iExtend, IRelation iRelation) {
        DatasourceInstanceAsset asset = assetService.getById(vo.getBusinessId());
        FunctionUtil.trueFunction(asset != null)
                .withTrue(() -> {
                    DsAssetVO.Asset assetVO = BeanCopierUtil.copyProperties(asset, DsAssetVO.Asset.class);
                    dsAssetPacker.wrap(assetVO, iExtend, iRelation);
                    vo.setAsset(assetVO);
                });
    }

    @Override
    public void wrap(DsAssetVO.Asset asset, IExtend iExtend) {
    }

}
