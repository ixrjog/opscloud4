package com.baiyi.opscloud.asset.impl.base;

import com.baiyi.opscloud.asset.IAssetConvert;
import com.baiyi.opscloud.asset.factory.AssetConvertFactory;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/30 1:25 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseAssetConvert<T> implements IAssetConvert<T>, InitializingBean {

    @Resource
    protected BusinessAssetRelationService businessAssetRelationService;

    protected abstract List<BusinessTypeEnum> getBusinessTypes();

    protected abstract T toBusinessObject(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum);

    @Override
    public Map<BusinessTypeEnum, T> toBusinessTypes(DsAssetVO.Asset asset) {
        Map<BusinessTypeEnum, T> businessTypeMap = Maps.newHashMap();
        for (BusinessTypeEnum businessType : getBusinessTypes()) {
            List<BusinessAssetRelation> businessAssetRelations = queryBusinessAssetRelations(asset, businessType);
            if (CollectionUtils.isEmpty(businessAssetRelations)) {
                businessTypeMap.put(businessType, toBusinessObject(asset, businessType));
            }
        }
        return businessTypeMap;
    }

    protected int getDefaultEnvType() {
        return 0;
    }


    protected List<BusinessAssetRelation> queryBusinessAssetRelations(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum) {
        return businessAssetRelationService.queryAssetRelations(businessTypeEnum.getType(), asset.getId());
    }

    /**
     * 将字符串的首字母转大写
     *
     * @param str 需要转换的字符串
     * @return
     */
    protected String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    @Override
    public void afterPropertiesSet() {
        AssetConvertFactory.register(this);
    }
}
