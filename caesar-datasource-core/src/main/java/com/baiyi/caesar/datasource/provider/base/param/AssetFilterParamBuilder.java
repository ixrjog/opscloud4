package com.baiyi.caesar.datasource.provider.base.param;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/30 2:32 下午
 * @Since 1.0
 */
public class AssetFilterParamBuilder {

    private AssetFilterParam param = new AssetFilterParam();

    private AssetFilterParamBuilder() {
    }

    public static AssetFilterParamBuilder builder() {
        return new AssetFilterParamBuilder();
    }

    public AssetFilterParam build() {
        return param;
    }

    public AssetFilterParamBuilder filterEntry(String key, Object value) {
        param.putFilter(key, value);
        return this;
    }
}
