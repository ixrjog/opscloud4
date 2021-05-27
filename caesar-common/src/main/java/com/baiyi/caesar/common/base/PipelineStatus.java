package com.baiyi.caesar.common.base;

/**
 * @Author baiyi
 * @Date 2021/3/31 2:32 下午
 * @Version 1.0
 */
public enum PipelineStatus {

    INITIAL("INITIAL", "会话初始建立"),
    QUERY_TASK("QUERY_TASK", "查询任务"),
    CLOSE("CLOSE", "关闭所有会话");

    private String code;
    private String desc;

    PipelineStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getDescByCode(String code) {
        for (PipelineStatus typeEnum : PipelineStatus.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getDesc();
            }
        }
        return "未知类型";
    }
}
