package com.baiyi.opscloud.common.leo.response;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.param.leo.request.ILeoRequestParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2022/11/23 18:57
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
public class LeoContinuousDeliveryResponse<T> implements ILeoRequestParam {

    private T body;

    private String messageType;

    @Override
    public String toString() {
        return JSONUtil.writeValueAsString(this);
    }

}