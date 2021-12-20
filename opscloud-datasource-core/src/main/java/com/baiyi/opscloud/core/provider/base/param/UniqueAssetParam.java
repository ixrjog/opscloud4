package com.baiyi.opscloud.core.provider.base.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2021/6/30 4:06 下午
 * @Since 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniqueAssetParam {

    private String assetId;
}
