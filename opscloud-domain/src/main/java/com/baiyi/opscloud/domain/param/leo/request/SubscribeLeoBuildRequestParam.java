package com.baiyi.opscloud.domain.param.leo.request;

import com.baiyi.opscloud.domain.param.SuperPageParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/24 17:23
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubscribeLeoBuildRequestParam extends SuperPageParam implements ILeoRequestParam {

    private Integer applicationId;

    private Integer envType;

    private List<Integer> jobIds;

    @Override
    public String getMessageType() {
        return LeoRequestType.SUBSCRIBE_LEO_BUILD.name();
    }

}