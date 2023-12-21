package com.baiyi.opscloud.common.feign.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author 修远
 * @Date 2023/7/5 3:13 PM
 * @Since 1.0
 */

@Data
public class MgwCoreResponse<T> {

    private static final String SUCCESS_CODE = "00000000";

    @Schema(description = "返回码,只有返回00000000才表示受理成功，其余都是失败")
    private String respCode;

    @Schema(description = "返回码说明")
    private String respMsg;

    @Schema(description = "业务对象")
    private T data;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(this.respCode);
    }

}