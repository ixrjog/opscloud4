package com.baiyi.opscloud.common;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpResult<T> {

    public static final HttpResult<Boolean> SUCCESS = new HttpResult<>(true);

    private T body;

    @Schema(description = "是否成功")
    private boolean success;

    private String msg;

    private int code;

    public HttpResult(T body) {
        this.body = body;
        this.msg = "success";
        this.success = true;
    }

    public HttpResult(BaseException ex) {
        this.msg = ex.getMessage();
        this.code = ex.getCode();
        this.success = false;
    }

    public HttpResult(BusinessWrapper<T> wrapper) {
        this.success = wrapper.isSuccess();
        if (wrapper.isSuccess()) {
            this.body = wrapper.getBody();
            this.msg = "success";
        } else {
            this.code = wrapper.getCode();
            this.msg = wrapper.getDesc();
        }
    }

    public HttpResult(ErrorEnum errorEnum) {
        this.success = false;
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMessage();
    }

    public HttpResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.success = false;
    }

    @Override
    public String toString() {
        return JSONUtil.writeValueAsString(this);
    }
}
