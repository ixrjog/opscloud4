package com.baiyi.opscloud.core.comparer;

import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2023/12/25 17:01
 * @Version 1.0
 */
@Data
@Builder
public class AssetComparer {

    public static final AssetComparer NOT_COMPARED = AssetComparer.builder()
            .equal(true)
            .build();

    public static final AssetComparer COMPARE_NAME = AssetComparer.builder()
            .compareName(true)
            .build();

    public static final AssetComparer COMPARE_DESCRIPTION = AssetComparer.builder()
            .compareDescription(true)
            .build();

    @Builder.Default
    private boolean compareName = false;

    @Builder.Default
    private boolean compareAssetId = false;

    @Builder.Default
    private boolean compareKind = false;

    @Builder.Default
    private boolean compareKey = false;

    @Builder.Default
    private boolean compareKey2 = false;

    @Builder.Default
    private boolean compareExpiredTime = false;

    @Builder.Default
    private boolean compareDescription = false;

    @Builder.Default
    private boolean compareActive = false;

    @Builder.Default
    private boolean compareCreatedTime = false;

    @Builder.Default
    private boolean equal = false;

    public boolean compare(DatasourceInstanceAsset a1, DatasourceInstanceAsset a2) {
        // 相同
        if (this.isEqual()) {
            return true;
        }
        if (this.isCompareName()) {
            if (!AssetUtil.equals(a2.getName(), a1.getName())) {
                return false;
            }
        }
        if (this.isCompareAssetId()) {
            if (!AssetUtil.equals(a2.getAssetId(), a1.getAssetId())) {
                return false;
            }
        }
        if (this.isCompareKey()) {
            if (!AssetUtil.equals(a2.getAssetKey(), a1.getAssetKey())) {
                return false;
            }
        }
        if (this.isCompareKey2()) {
            if (!AssetUtil.equals(a2.getAssetKey2(), a1.getAssetKey2())) {
                return false;
            }
        }
        if (this.isCompareDescription()) {
            if (!AssetUtil.equals(a2.getDescription(), a1.getDescription())) {
                return false;
            }
        }

        if (this.isCompareKind()) {
            if (!AssetUtil.equals(a2.getKind(), a1.getKind())) {
                return false;
            }
        }

        if (this.isCompareExpiredTime()) {
            if (!AssetUtil.equals(a2.getExpiredTime(), a1.getExpiredTime())) {
                return false;
            }
        }

        if (this.isCompareCreatedTime()) {
            if (a2.getCreatedTime() != a1.getCreatedTime()) {
                return false;
            }
        }

        if (this.isCompareActive()) {
            return a2.getIsActive().equals(a1.getIsActive());
        }
        return true;
    }

}