package com.baiyi.opscloud.common.exception;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2020/4/19 12:20 下午
 * @Version 1.0
 */
@Getter
public enum BusinessErrorCodeEnum implements ErrorCode {

    /**
     *
     */
    UNSPECIFIED("500", "网络异常，请稍后再试");

    /**
     * 错误码
     */
    private final String code;

    /**
     * 描述
     */
    private final String desc;

    BusinessErrorCodeEnum(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据编码查询枚举。
     *
     * @param code 编码。
     * @return 枚举。
     */
    public static BusinessErrorCodeEnum getByCode(String code) {
        return Arrays.stream(BusinessErrorCodeEnum.values()).filter(value -> StringUtils.equals(code, value.getCode())).findFirst().orElse(UNSPECIFIED);
    }

    /**
     * 枚举是否包含此code
     *
     * @param code 枚举code
     * @return 结果
     */
    public static Boolean contains(String code) {
        return Arrays.stream(BusinessErrorCodeEnum.values()).anyMatch(value -> StringUtils.equals(code, value.getCode()));
    }

}