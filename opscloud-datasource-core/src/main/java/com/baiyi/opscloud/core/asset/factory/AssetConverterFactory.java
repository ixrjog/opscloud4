package com.baiyi.opscloud.core.asset.factory;

import com.baiyi.opscloud.core.asset.IAssetConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资产转换业务对象工厂
 *
 * @Author baiyi
 * @Date 2021/7/30 1:21 下午
 * @Version 1.0
 */
@Slf4j
public class AssetConverterFactory {

    private AssetConverterFactory() {
    }

    private static final Map<String, IAssetConverter> context = new ConcurrentHashMap<>();

    public static IAssetConverter getIAssetConvertByAssetType(String assetType) {
        return context.get(assetType);
    }

    public static void register(IAssetConverter bean) {
        log.info("AssetConvertFactory注册: beanName = {}", bean.getClass().getSimpleName());
        context.put(bean.getAssetType(), bean);
    }

}
