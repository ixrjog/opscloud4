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

    /**
     * 星期日为一周的第一天	SUN	MON	TUE	WED	THU	FRI	SAT
     * DAY_OF_WEEK返回值	1	2	3	4	5	6	7
     * 星期一为一周的第一天	MON	TUE	WED	THU	FRI	SAT	SUN
     * DAY_OF_WEEK返回值	1	2	3	4	5	6	7
     */

    @Test
    void calendarTest1() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("今天星期几: " + calendar.get(Calendar.DAY_OF_WEEK));
    }

    @Test
    void calendarTest2() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("日期:" + calendar.get(Calendar.DAY_OF_MONTH));
    }

}
