package com.baiyi.opscloud.packer.user.am;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.SimpleRelation;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.AccessManagementVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/2/8 1:13 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AccessManagementPacker {

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsAssetPacker dsAssetPacker;

    public static final Map<String, Function<DsAssetVO.Asset, AccessManagementVO.XAccessManagement>> CONTEXT = new ConcurrentHashMap<>();

    private static final DsAssetTypeConstants[] XAM_ASSET_TYPES = {DsAssetTypeConstants.RAM_USER, DsAssetTypeConstants.IAM_USER};

    /**
     * 包装amMap
     *
     * @param user
     */
    public void wrap(UserVO.User user) {
        Map<String, List<AccessManagementVO.XAccessManagement>> amMap = Maps.newHashMap();
        Arrays.stream(XAM_ASSET_TYPES).forEach(xamAssetType -> {
            List<AccessManagementVO.XAccessManagement> xams = toAms(user, xamAssetType.name());
            if (CollectionUtils.isEmpty(xams)) {
                return;
            }
            if (amMap.containsKey(xamAssetType.name())) {
                amMap.get(xamAssetType.name()).addAll(xams);
            } else {
                amMap.put(xamAssetType.name(), xams);
            }
        });
        user.setAmMap(amMap);
    }

    /**
     * 包装ams
     *
     * @param user
     * @param xamType
     */
    public void wrap(UserVO.User user, String xamType) {
        List<AccessManagementVO.XAccessManagement> xams = toAms(user, xamType);
        if (!CollectionUtils.isEmpty(xams)) {
            user.setAms(xams);
        }
    }

    public List<AccessManagementVO.XAccessManagement> toAms(UserVO.User user, String xamType) {
        if (!CONTEXT.containsKey(xamType)) {
            return Collections.emptyList();
        }
        DatasourceInstanceAsset param = DatasourceInstanceAsset.builder()
                .assetType(xamType)
                .assetKey(user.getUsername())
                .isActive(true)
                .build();
        List<DatasourceInstanceAsset> data = dsInstanceAssetService.acqAssetByAssetParam(param);
        if (CollectionUtils.isEmpty(data)) {
            return Collections.emptyList();
        }

        List<DsAssetVO.Asset> assets = BeanCopierUtil.copyListProperties(data, DsAssetVO.Asset.class).stream().peek(e ->
                dsAssetPacker.wrap(e, SimpleExtend.EXTEND, SimpleRelation.RELATION)
        ).toList();

        Function<DsAssetVO.Asset, AccessManagementVO.XAccessManagement> converter = CONTEXT.get(xamType);
        return assets.stream().map(converter).collect(Collectors.toList());
    }

}
