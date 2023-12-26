package com.baiyi.opscloud.domain.vo.base;

import java.util.Date;

/**
 * 人类可阅读时间格式
 *
 * @Author baiyi
 * @Date 2021/8/5 10:08 上午
 * @Version 1.0
 */
public class ReadableTime {

    public interface IAgo {

        Date getAgoTime();

        void setAgo(String ago);
    }

    /**
     * 运行时间
     */
    public interface IRuntime {

        void setRuntime(String runtime);

        String getRuntime();

        Date getStartTime();

        Date getEndTime();

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