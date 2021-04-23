package com.baiyi.opscloud.caesar.util;

import com.baiyi.opscloud.domain.param.PageParam;

/**
 * @Author baiyi
 * @Date 2021/1/11 1:56 下午
 * @Version 1.0
 */
public class PageUtils {

    public static final int EXTEND = 1;

    public static final int NOT_EXTEND = 0;

    public static void assemblePageParam(PageParam pageParam){
        pageParam.setLength(10);
        pageParam.setPage(1);
    }
}
