package com.baiyi.opscloud.core.provider.asset;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.asset.IAssetConverter;
import com.baiyi.opscloud.core.asset.factory.AssetConverterFactory;
import com.baiyi.opscloud.core.provider.base.asset.IAssetBusinessRelation;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.factory.business.BusinessServiceFactory;
import com.baiyi.opscloud.factory.business.base.IBusinessService;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.SCAN_ASSET_BUSINESS;

/**
 * 资产与业务对象绑定
 *
 * @Author baiyi
 * @Date 2021/8/2 2:00 下午
 * @Version 1.0
 */
@SuppressWarnings("rawtypes")
@Slf4j
public abstract class AbstractAssetBusinessRelationProvider<T> extends BaseAssetProvider<T> implements IAssetBusinessRelation {

    @Resource
    private BusinessAssetRelationService businessAssetRelationService;

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    @Override
    @SingleTask(name = SCAN_ASSET_BUSINESS, lockTime = "2m")
    public void scan(int dsInstanceId) {
        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                .instanceId(dsInstanceId)
                .assetType(getAssetType())
                .page(1)
                .length(10000)
                .build();
        DataTable<DatasourceInstanceAsset> dataTable = dsInstanceAssetService.queryPageByParam(pageQuery);
        dataTable.getData().forEach(a -> scan(toAssetVO(a)));
    }

    private DsAssetVO.Asset toAssetVO(DatasourceInstanceAsset asset) {
        DsAssetVO.Asset assetVO = BeanCopierUtil.copyProperties(asset, DsAssetVO.Asset.class);
        Map<String, String> properties = dsInstanceAssetPropertyService.queryByAssetId(asset.getId())
                .stream().collect(Collectors.toMap(DatasourceInstanceAssetProperty::getName, DatasourceInstanceAssetProperty::getValue, (k1, k2) -> k1));
        assetVO.setProperties(properties);
        return assetVO;
    }

    @Override
    public void scan(DsAssetVO.Asset asset) {
        IAssetConverter iAssetConvert = AssetConverterFactory.getConverterByAssetType(getAssetType());
        if (iAssetConvert == null) {
            return;
        }
        // 获取可转换的业务对象
        List<BusinessTypeEnum> businessTypeEnums = iAssetConvert.getBusinessTypes();
        businessTypeEnums.forEach(t -> bind(t, asset));
    }

    /**
     * 资产于业务对象绑定
     *
     * @param businessTypeEnum
     * @param asset
     */
    private void bind(BusinessTypeEnum businessTypeEnum, DsAssetVO.Asset asset) {
        IBusinessService iBusinessService = BusinessServiceFactory.getIBusinessServiceByBusinessType(businessTypeEnum.getType());
        if (iBusinessService != null) {
            bind(asset, iBusinessService.toBusinessAssetRelation(asset));
        }
    }

    private void bind(DsAssetVO.Asset asset, BusinessAssetRelationVO.IBusinessAssetRelation iBusinessAssetRelation) {
        if (iBusinessAssetRelation == null) {
            return;
        }
        iBusinessAssetRelation.setAssetId(asset.getId());
        BusinessAssetRelationVO.Relation relation = iBusinessAssetRelation.toBusinessAssetRelation();
        if (businessAssetRelationService.getByUniqueKey(relation) == null) {
            BusinessAssetRelation businessAssetRelation = BeanCopierUtil.copyProperties(relation, BusinessAssetRelation.class);
            businessAssetRelation.setAssetType(asset.getAssetType());
            businessAssetRelationService.add(businessAssetRelation);
        }
    }

}