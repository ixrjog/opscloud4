package com.baiyi.opscloud.core.comparer;

/**
 * @Author baiyi
 * @Date 2023/12/25 17:57
 * @Version 1.0
 */
public class AssetComparerBuilder {

    private final AssetComparer assetComparer = AssetComparer.builder().build();

    private AssetComparerBuilder() {
    }

    static public AssetComparerBuilder newBuilder() {
        return new AssetComparerBuilder();
    }

    public AssetComparerBuilder compareOfAssetId() {
        assetComparer.setCompareAssetId(true);
        return this;
    }

    public AssetComparerBuilder compareOfName() {
        assetComparer.setCompareName(true);
        return this;
    }

    public AssetComparerBuilder compareOfKey() {
        assetComparer.setCompareKey(true);
        return this;
    }

    public AssetComparerBuilder compareOfKey2() {
        assetComparer.setCompareKey2(true);
        return this;
    }

    public AssetComparerBuilder compareOfKind() {
        assetComparer.setCompareKind(true);
        return this;
    }

    public AssetComparerBuilder compareOfDescription() {
        assetComparer.setCompareDescription(true);
        return this;
    }

    public AssetComparerBuilder compareOfExpiredTime() {
        assetComparer.setCompareExpiredTime(true);
        return this;
    }

    public AssetComparerBuilder compareOfActive() {
        assetComparer.setCompareActive(true);
        return this;
    }

    public AssetComparerBuilder compareOfCreatedTime() {
        assetComparer.setCompareCreatedTime(true);
        return this;
    }

    public AssetComparer build() {
        return assetComparer;
    }

}
