package com.baiyi.opscloud.packer;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;

/**
 * @Author baiyi
 * @Date 2022/2/17 3:42 PM
 * @Version 1.0
 */
public interface IWrapper<T> {

    void wrap(T vo, IExtend iExtend);

    default void wrap(T vo) {
        wrap(vo, SimpleExtend.EXTEND);
    }

}
