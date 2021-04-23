package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.common.base.Global;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2020/10/14 4:20 下午
 * @Version 1.0
 */
public class CreatedByUtils {

    public static String getHead() {
        return Joiner.on(" ").join("#", Global.CREATED_BY, "on", TimeUtils.nowDate(), "\n\n");
    }

}
