package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

/**
 * @Author baiyi
 * @Date 2022/12/29 17:14
 * @Version 1.0
 */
public class CalendarTest extends BaseUnit {

    @Test
    void calendarTest() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("今天星期几: " + calendar.get(Calendar.DAY_OF_WEEK));
    }

}
