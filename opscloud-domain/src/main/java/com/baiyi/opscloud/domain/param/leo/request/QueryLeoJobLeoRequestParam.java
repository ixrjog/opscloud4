package com.baiyi.opscloud.domain.param.leo.request;

import com.baiyi.opscloud.domain.param.SuperPageParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2022/11/23 16:48
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryLeoJobLeoRequestParam extends SuperPageParam implements ILeoRequestParam {

    private Integer applicationId;

    private Integer envType;

    private final Boolean extend = true;

    @Override
    public String getMessageType() {
        return LeoRequestType.QUERY_LEO_JOB.name();
    }

}
