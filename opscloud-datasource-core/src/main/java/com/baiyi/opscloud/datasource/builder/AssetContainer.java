package com.baiyi.opscloud.datasource.builder;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/19 9:34 上午
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetContainer {

    private DatasourceInstanceAsset asset;

    @Builder.Default
    private Map<String, String> properties = Maps.newHashMap();

}
