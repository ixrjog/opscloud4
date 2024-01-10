package com.baiyi.opscloud.domain.notice;

import com.baiyi.opscloud.domain.util.ObjectUtil;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/12/14 10:23 AM
 * @Version 1.0
 */
public interface INoticeMessage {

    default Map<String, Object> toContentMap() {
        return ObjectUtil.objectToMap(this);
    }

}