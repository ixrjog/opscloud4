package com.baiyi.opscloud.asset.factory;

import com.baiyi.opscloud.asset.IAssetConvert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资产转换工厂
 *
 * @Author baiyi
 * @Date 2021/7/30 1:21 下午
 * @Version 1.0
 */
public class AssetConvertFactory {

    private AssetConvertFactory() {
    }

    private static Map<String, IAssetConvert> context = new ConcurrentHashMap<>();

    public static IAssetConvert getIAssetConvertByAssetType(String assetType) {
        return context.get(assetType);
    }

    public static void register(IAssetConvert bean) {
        context.put(bean.getAssetType(), bean);
    }


}
