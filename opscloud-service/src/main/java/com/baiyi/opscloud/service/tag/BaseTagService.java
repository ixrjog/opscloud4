package com.baiyi.opscloud.service.tag;

/**
 * @Author baiyi
 * @Date 2021/6/23 10:37 上午
 * @Version 1.0
 */
public interface BaseTagService {

    boolean hasBusinessTag(String tagKey, Integer businessType, Integer businessId, boolean isConstraint);
}
