package com.baiyi.opscloud.util.time;

import com.baiyi.opscloud.domain.vo.base.ShowTime;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @Author baiyi
 * @Date 2021/9/24 3:42 下午
 * @Version 1.0
 */
public class DurationUtil {

    private DurationUtil() {
    }

    /**
     * 计算并设置时长
     * @param iDuration
     */
    public static void wrap(ShowTime.IDuration iDuration) {
        if (iDuration.getStartTime() == null || iDuration.getEndTime() == null) return;
        long diffTime = iDuration.getEndTime().getTime() - iDuration.getStartTime().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        iDuration.setDuration(formatter.format(diffTime));
    }

}
