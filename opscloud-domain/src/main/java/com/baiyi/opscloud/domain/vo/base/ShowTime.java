package com.baiyi.opscloud.domain.vo.base;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/8/5 10:08 上午
 * @Version 1.0
 */
public class ShowTime {

    public interface IAgo {

        Date getAgoTime();

        void setAgo(String ago);

    }

    public interface ILater {

        Date getExpiredTime();

        void setLater(String later);
    }

    /**
     * 持续时间
     */
    public interface IDuration {

        Date getStartTime();

        Date getEndTime();

        void setDuration(String duration);
    }

}
