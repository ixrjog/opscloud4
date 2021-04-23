package com.baiyi.opscloud.jumpserver.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/3/4 11:01 上午
 * @Version 1.0
 */
@Data
@Builder
public class AssetsSystemuserAssetsBO {

    private String systemuserId;

    private String assetId;
}
