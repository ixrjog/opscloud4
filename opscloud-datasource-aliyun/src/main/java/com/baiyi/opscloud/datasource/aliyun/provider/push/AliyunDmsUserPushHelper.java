package com.baiyi.opscloud.datasource.aliyun.provider.push;

import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.aliyun.converter.DmsAssetConverter;
import com.baiyi.opscloud.datasource.aliyun.dms.entity.DmsUser;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/12/16 7:30 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunDmsUserPushHelper {

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    private final BusinessTagService bizTagService;

    private final TagService tagService;

    /**
     * 获取需要推送的资产
     *
     * @param dsInstanceContext
     * @return
     */
    public List<DmsUser.User> getPushAssets(DsInstanceContext dsInstanceContext) {
        List<DatasourceInstanceAsset> ramUsers = queryValidRamUsers(dsInstanceContext.getDsInstance().getUuid());
        if (CollectionUtils.isEmpty(ramUsers)) {
            return Collections.emptyList();
        }
        List<DatasourceInstanceAsset> dmsUsers = queryDmsUsers(dsInstanceContext.getDsInstance().getUuid());
        Map<String, DatasourceInstanceAsset> dmsUserMap = dmsUsers.stream().collect(Collectors.toMap(DatasourceInstanceAsset::getAssetKey, a -> a, (k1, k2) -> k1));
        List<DmsUser.User> dmsUserList = Lists.newArrayList();
        ramUsers.forEach(ramUser -> {
            // assetId = RAM_USER_ID
            if (dmsUserMap.containsKey(ramUser.getAssetId())) {
                return; // 账户已存在
            }
            // 查询用户手机号
            Optional<DatasourceInstanceAssetProperty> optionalProperty
                    = dsInstanceAssetPropertyService.queryByAssetId(ramUser.getId()).stream().filter(e -> "mobilePhone".equals(e.getName())).findFirst();
            if (optionalProperty.isPresent()) {
                dmsUserList.add(DmsAssetConverter.toDmsUser(ramUser,
                        optionalProperty.map(DatasourceInstanceAssetProperty::getValue).orElse(null)));
            }
        });
        return dmsUserList;
    }

    /**
     * 查询有效的RAM用户（过滤系统标签）
     *
     * @param instanceUuid
     * @return
     */
    private List<DatasourceInstanceAsset> queryValidRamUsers(String instanceUuid) {
        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(instanceUuid)
                .assetType(DsAssetTypeConstants.RAM_USER.name())
                .page(1)
                .length(10000)
                .build();
        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);
        Tag tag = tagService.getByTagKey(TagConstants.SYSTEM.name());
        return table.getData().stream().filter(e -> {
            if (tag == null) {
                return true;
            }
            BusinessTag businessTag = BusinessTag.builder()
                    .businessId(e.getId())
                    .businessType(BusinessTypeEnum.ASSET.getType())
                    .tagId(tag.getId())
                    .build();
            return bizTagService.countByBusinessTag(businessTag) == 0;
        }).collect(Collectors.toList());
    }

    private List<DatasourceInstanceAsset> queryDmsUsers(String instanceUuid) {
        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(instanceUuid)
                .assetType(DsAssetTypeConstants.DMS_USER.name())
                .page(1)
                .length(10000)
                .build();
        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);
        return table.getData();
    }

}