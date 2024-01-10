package com.baiyi.opscloud.core.util;

import org.apache.commons.lang3.StringUtils;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/6/18 5:07 下午
 * @Version 1.0
 */
public class AssetUtil {

    private AssetUtil(){}

    public static boolean equals(String var1, String var2) {
        if (StringUtils.isEmpty(var1)) {
            return StringUtils.isEmpty(var2);
        }
        if (StringUtils.isEmpty(var2)) {
            return false;
        }
        return var1.equals(var2);
    }

    public static boolean equals(Date date1, Date data2) {
        if (date1 == null) {
            return data2 == null;
        }
        return date1.equals(data2);
    }

}