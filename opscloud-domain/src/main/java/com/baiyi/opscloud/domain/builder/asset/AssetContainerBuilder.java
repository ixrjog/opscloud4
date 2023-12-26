package com.baiyi.opscloud.domain.builder.asset;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/19 9:54 上午
 * @Version 1.0
 */
public class AssetContainerBuilder {

    private final AssetContainer assetContainer = AssetContainer.builder().build();

    private AssetContainerBuilder() {
    }

    static public AssetContainerBuilder newBuilder() {
        return new AssetContainerBuilder();
    }

    public AssetContainerBuilder paramProperty(String name, Object value) {
        if (!ObjectUtils.isEmpty(value)) {
            assetContainer.getProperties().put(name, String.valueOf(value));
        }
        return this;
    }

    public AssetContainerBuilder paramAsset(DatasourceInstanceAsset asset) {
        assetContainer.setAsset(asset);
        return this;
    }

    public AssetContainerBuilder paramChildren(List<AssetContainer> children) {
        assetContainer.setChildren(children);
        return this;
    }

    public AssetContainerBuilder paramChild(AssetContainer child) {
        assetContainer.getChildren().add(child);
        return this;
    }

    public AssetContainer build() {
        return assetContainer;
    }

}