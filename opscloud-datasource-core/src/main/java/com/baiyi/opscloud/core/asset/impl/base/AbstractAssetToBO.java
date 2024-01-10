package com.baiyi.opscloud.core.asset.impl.base;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.core.asset.IAssetConverter;
import com.baiyi.opscloud.core.asset.factory.AssetConverterFactory;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/30 1:25 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractAssetToBO implements IAssetConverter, InitializingBean {

    @Resource
    protected BusinessAssetRelationService businessAssetRelationService;

    @Resource
    private EnvService envService;

    protected abstract BusinessAssetRelationVO.IBusinessAssetRelation toBO(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum);

    private BusinessAssetRelationVO.IBusinessAssetRelation wrap(BusinessAssetRelationVO.IBusinessAssetRelation bo, DsAssetVO.Asset asset) {
        bo.setAssetId(asset.getId());
        return bo;
    }

    @Override
    public Map<BusinessTypeEnum, BusinessAssetRelationVO.IBusinessAssetRelation> toBusinessTypes(DsAssetVO.Asset asset) {
        Map<BusinessTypeEnum, BusinessAssetRelationVO.IBusinessAssetRelation> businessTypeMap = Maps.newHashMap();
        for (BusinessTypeEnum businessType : getBusinessTypes()) {
            List<BusinessAssetRelation> businessAssetRelations = queryBusinessAssetRelations(asset, businessType);
            if (CollectionUtils.isEmpty(businessAssetRelations)) {
                businessTypeMap.put(businessType, wrap(toBO(asset, businessType), asset));
            }
        }
        return businessTypeMap;
    }

    protected int getDefaultEnvType() {
        Env env = envService.getByEnvName(Global.DEF_ENV);
        return env != null ? env.getEnvType() : 1;
    }

    protected List<BusinessAssetRelation> queryBusinessAssetRelations(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum) {
        return businessAssetRelationService.queryAssetRelations(businessTypeEnum.getType(), asset.getId());
    }

    /**
     * 将字符串的首字母转大写
     * 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
     *
     * @param str 需要转换的字符串
     * @return
     */
    protected String captureName(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    @Override
    public void afterPropertiesSet() {
        AssetConverterFactory.register(this);
    }

}