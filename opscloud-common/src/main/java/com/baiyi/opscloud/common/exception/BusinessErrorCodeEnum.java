package com.baiyi.opscloud.common.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/4/19 12:20 下午
 * @Version 1.0
 */
public enum BusinessErrorCodeEnum implements ErrorCode{

    UNSPECIFIED("500", "网络异常，请稍后再试");

    /** 错误码 */
    private final String code;

    /** 描述 */
    private final String desc;

    private BusinessErrorCodeEnum(final String code, final String desc) {
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
        for (BusinessErrorCodeEnum value : BusinessErrorCodeEnum.values()) {
            if (StringUtils.equals(code, value.getCode())) {
                return value;
            }
        }
        return UNSPECIFIED;
    }

    /**
     * 枚举是否包含此code
     * @param code 枚举code
     * @return 结果
     */
    public static Boolean contains(String code){
        for (BusinessErrorCodeEnum value : BusinessErrorCodeEnum.values()) {
            if (StringUtils.equals(code, value.getCode())) {
                return true;
            }
        }
        return  false;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }



}
