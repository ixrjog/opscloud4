package com.sdg.cmdb.domain.explain;

/**
 * 任务状态.-1：无效；0：待开始；1：执行中；2：执行异常；3：执行完成
 * Created by zxxiao on 2017/4/11.
 */
public enum JobStatusEnum {
    statusInvalid(-1, "无效"),
    statusStart(0, "待开始"),
    statusExecution(1, "执行中"),
    statusExecutionError(2, "执行异常"),
    statusComplate(3, "执行完成"),
    statusClose(4, "主动关闭");

    private int code;
    private String desc;

    JobStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
