package com.baiyi.opscloud.service.tag;

/**
 * @Author baiyi
 * @Date 2021/6/23 10:37 上午
 * @Version 1.0
 */
public interface SimpleTagService {

    /**
     * 检查业务是否有标签
     * @param tagKey
     * @param businessType
     * @param businessId
     * @param isConstraint 标签和业务对象类型必须一致
     * @return
     */
    boolean hasBusinessTag(String tagKey, Integer businessType, Integer businessId, boolean isConstraint);

    default boolean hasBusinessTag(String tagKey, Integer businessType, Integer businessId) {
        return hasBusinessTag(tagKey, businessType, businessId, true);
    }

    /**
     * 打标签
     * @param tagKey
     * @param businessType
     * @param businessId
     */
    void labeling(String tagKey, Integer businessType, Integer businessId);

}