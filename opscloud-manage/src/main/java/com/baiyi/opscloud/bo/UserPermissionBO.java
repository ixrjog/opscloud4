package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/3/6 11:32 上午
 * @Version 1.0
 */
@Data
@Builder
public class UserPermissionBO {

    private Integer id;
    private Integer userId;
    private Integer businessId;
    private Integer businessType;

}
