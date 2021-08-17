package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.datasource.util.AssetUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/8/17 11:26 上午
 * @Version 1.0
 */
public class AssetUtilTest extends BaseUnit {

    @Test
    void dd() {
        System.out.println(AssetUtil.equals(null, "A"));
        System.out.println(AssetUtil.equals("A", null));

        System.out.println(AssetUtil.equals("B", "B"));
    }
}
