package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.facade.datasource.DsFacade;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetFacade;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/7/5 11:01 上午
 * @Version 1.0
 */
@Service
public class DsInstanceAssetFacadeImpl implements DsInstanceAssetFacade {

    @Resource
    private DsFacade dsFacade;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsAssetPacker dsAssetPacker;

    @Override
    public List<DsAssetVO.Asset> querySshKeyAssets(String username) {
        DsInstanceParam.DsInstanceQuery instanceQuery = DsInstanceParam.DsInstanceQuery.builder()
                .instanceType(DsTypeEnum.GITLAB.getName())
                .build();
        List<DsInstanceVO.Instance> instances = dsFacade.queryDsInstance(instanceQuery);
        List<DsAssetVO.Asset> result = Lists.newArrayList();
        instances.forEach(i -> {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .assetType(DsAssetTypeEnum.GITLAB_SSHKEY.getType())
                    .instanceUuid(i.getUuid())
                    .name(username)
                    .build();
            List<DatasourceInstanceAsset> sshKeyAssets = dsInstanceAssetService.queryAssetByAssetParam(asset);

            result.addAll(sshKeyAssets.stream().map(a ->
                    dsAssetPacker.wrap(i, a)
            ).collect(Collectors.toList()));

        });
        return result;
    }


}
