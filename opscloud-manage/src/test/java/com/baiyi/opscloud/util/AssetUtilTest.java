package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.datasource.util.AssetUtil;
import com.baiyi.opscloud.datasource.util.TimeUtil;
import com.baiyi.opscloud.datasource.util.enums.TimeZoneEnum;
import org.junit.jupiter.api.Test;

import java.util.Date;

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

    @Test
    void toDate() {
        // 2021-08-27T03:14:04Z

        String time = "2021-08-27T03:14:04Z";

        //    String UTC = "yyyy-MM-dd'T'HH:mm'Z'";

        Date date = TimeUtil.toGmtDate(time, TimeZoneEnum.UTC);

        String d = com.baiyi.opscloud.common.util.TimeUtil.dateToStr(date);
        System.err.println(d);
    }
}
