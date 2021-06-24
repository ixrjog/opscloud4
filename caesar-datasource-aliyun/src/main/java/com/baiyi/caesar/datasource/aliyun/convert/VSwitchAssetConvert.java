package com.baiyi.caesar.datasource.aliyun.convert;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.baiyi.caesar.common.exception.common.CommonRuntimeException;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.builder.AssetContainerBuilder;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.baiyi.caesar.datasource.aliyun.convert.ComputeAssetConvert.toGmtDate;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/23 2:23 下午
 * @Since 1.0
 */

@Component
public class VSwitchAssetConvert {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    public AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeVSwitchesResponse.VSwitch entry) {
        DatasourceInstanceAsset queryKey = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getVpcId())
                .assetType(DsAssetTypeEnum.VPC.name())
                .assetKey(entry.getVpcId())
                .build();
        DatasourceInstanceAsset vpcAsset = dsInstanceAssetService.getByUniqueKey(queryKey);
        if (vpcAsset == null)
            throw new CommonRuntimeException("请先同步VPC");
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .parentId(vpcAsset.getId())
                .assetId(entry.getVSwitchId()) // 资产id = 实例id
                .name(entry.getVSwitchName())
                .assetKey(entry.getVSwitchId())
                // cidrBlock
                .assetKey2(entry.getCidrBlock())
                .kind("aliyunVSwitch")
                .assetType(DsAssetTypeEnum.V_SWITCH.name())
                .zone(entry.getZoneId())
                .description(entry.getDescription())
                .createdTime(toGmtDate(entry.getCreationTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("isDefault", entry.getIsDefault())

                .build();
    }
}
