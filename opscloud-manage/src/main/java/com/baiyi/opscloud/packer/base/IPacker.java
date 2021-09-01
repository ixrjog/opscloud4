package com.baiyi.opscloud.packer.base;

/**
 * @Author baiyi
 * @Date 2021/9/1 11:28 上午
 * @Version 1.0
 */
public interface IPacker<V, D> {


    V toVO(D d);


}
