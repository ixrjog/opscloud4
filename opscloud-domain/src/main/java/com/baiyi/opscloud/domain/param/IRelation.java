package com.baiyi.opscloud.domain.param;

/**
 * @Author baiyi
 * @Date 2021/6/21 11:31 上午
 * @Version 1.0
 */
public interface IRelation {

    Boolean getRelation();

    default boolean isRelation() {
        if (this.getRelation() == null) {
            return false;
        }
        return this.getRelation();
    }

}