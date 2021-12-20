package com.baiyi.opscloud.core.provider.base.param;

/**
 * @Author 修远
 * @Date 2021/6/30 2:32 下午
 * @Since 1.0
 */
public class AssetFilterParamBuilder {

    private final AssetFilterParam param = new AssetFilterParam();

    private AssetFilterParamBuilder() {
    }

    public static AssetFilterParamBuilder builder() {
        return new AssetFilterParamBuilder();
    }

    public AssetFilterParam build() {
        return param;
    }

    public AssetFilterParamBuilder filterEntiry(String key, Object value) {
        param.putFilter(key, value);
        return this;
    }
}
