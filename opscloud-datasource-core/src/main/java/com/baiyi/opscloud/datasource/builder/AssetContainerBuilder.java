package com.baiyi.opscloud.datasource.builder;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2021/6/19 9:54 上午
 * @Version 1.0
 */
public class AssetContainerBuilder {

    private AssetContainer assetContainer = AssetContainer.builder().build();

    private AssetContainerBuilder() {
    }

    static public AssetContainerBuilder newBuilder() {
        return new AssetContainerBuilder();
    }

    public AssetContainerBuilder paramProperty(String name, Object value) {
        if (!StringUtils.isEmpty(value))
            assetContainer.getProperties().put(name, String.valueOf(value));
        return this;
    }

    public AssetContainerBuilder paramAsset(DatasourceInstanceAsset asset) {
        assetContainer.setAsset(asset);
        return this;
    }

    public AssetContainer build() {
        return assetContainer;
    }
}
