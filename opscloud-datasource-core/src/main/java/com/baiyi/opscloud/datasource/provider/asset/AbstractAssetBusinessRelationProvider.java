package com.baiyi.opscloud.datasource.provider.asset;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.asset.IAssetConvert;
import com.baiyi.opscloud.datasource.asset.factory.AssetConvertFactory;
import com.baiyi.opscloud.datasource.provider.base.asset.IAssetBusinessRelation;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.server.ServerService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资产与业务对象绑定
 *
 * @Author baiyi
 * @Date 2021/8/2 2:00 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractAssetBusinessRelationProvider<T> extends BaseAssetProvider<T> implements IAssetBusinessRelation {

    @Resource
    private ServerService serverService;

    @Resource
    private BusinessAssetRelationService businessAssetRelationService;

    @Override
    @SingleTask(name = "ScanAssetBusiness", lockTime = "5m")
    public void scan(int dsInstanceId) {
        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                .instanceId(dsInstanceId)
                .assetType(getAssetType())
                .build();
        pageQuery.setPage(1);
        pageQuery.setLength(10000);
        DataTable<DatasourceInstanceAsset> dataTable = dsInstanceAssetService.queryPageByParam(pageQuery);
        dataTable.getData().forEach(a -> scan(BeanCopierUtil.copyProperties(a, DsAssetVO.Asset.class)));
    }

    @Override
    public void scan(DsAssetVO.Asset asset) {
        IAssetConvert iAssetConvert = AssetConvertFactory.getIAssetConvertByAssetType(getAssetType());
        if (iAssetConvert == null) return;
        // 获取可转换的业务对象
        List<BusinessTypeEnum> businessTypeEnums = iAssetConvert.getBusinessTypes();
        businessTypeEnums.forEach(t -> bind(t, asset));
    }

    private void bind(BusinessTypeEnum businessTypeEnum, DsAssetVO.Asset asset) {
        if (businessTypeEnum.equals(BusinessTypeEnum.SERVER)) {
            Server server = serverService.getByPrivateIp(asset.getAssetKey());
            if (server != null) {
                ServerVO.Server business = BeanCopierUtil.copyProperties(server, ServerVO.Server.class);
                business.setAssetId(asset.getId());
                bind(asset, business);
            }
        }
    }

    private void bind(DsAssetVO.Asset asset, BusinessAssetRelationVO.IBusinessAssetRelation iBusinessAssetRelation) {
        BusinessAssetRelationVO.Relation relation = iBusinessAssetRelation.toBusinessAssetRelation();
        if (businessAssetRelationService.getByUniqueKey(relation) == null) {
            BusinessAssetRelation businessAssetRelation = BeanCopierUtil.copyProperties(relation, BusinessAssetRelation.class);
            businessAssetRelation.setAssetType(asset.getAssetType());
            businessAssetRelationService.add(businessAssetRelation);
        }
    }
}