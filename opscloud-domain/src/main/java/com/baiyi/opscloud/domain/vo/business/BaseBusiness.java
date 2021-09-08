package com.baiyi.opscloud.domain.vo.business;

/**
 * @Author baiyi
 * @Date 2021/8/2 11:39 上午
 * @Version 1.0
 */
public class BaseBusiness {

    public interface IBusiness extends IBusinessType {
        Integer getBusinessId();
    }

    public interface IBusinessType {
        Integer getBusinessType();
    }

}
