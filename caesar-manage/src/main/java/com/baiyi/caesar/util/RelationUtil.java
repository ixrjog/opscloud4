package com.baiyi.caesar.util;

import com.baiyi.caesar.domain.param.IRelation;

/**
 * @Author baiyi
 * @Date 2021/6/21 3:11 下午
 * @Version 1.0
 */
public class RelationUtil {

    private RelationUtil(){}

    public static boolean isRelation(IRelation iRelation){
        if(iRelation.getRelation() == null) return false;
        return iRelation.getRelation();
    }
}
