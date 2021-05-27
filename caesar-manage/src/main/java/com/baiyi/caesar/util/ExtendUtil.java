package com.baiyi.caesar.util;

import com.baiyi.caesar.domain.param.IExtend;

/**
 * @Author baiyi
 * @Date 2021/5/13 3:57 下午
 * @Version 1.0
 */
public class ExtendUtil {

    private ExtendUtil(){}

    public static boolean isExtend(IExtend iExtend){
        if(iExtend.getExtend() == null) return false;
        return iExtend.getExtend();
    }
}
