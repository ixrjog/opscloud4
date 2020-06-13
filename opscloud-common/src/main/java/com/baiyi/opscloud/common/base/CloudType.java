package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/3/18 8:43 上午
 * @Version 1.0
 */
public enum  CloudType {

    ALIYUN(2, "Aliyun"),
    AWS(3, "AWS");

    private int type;
    private String name;

    CloudType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static String getName(int type) {
        for (CloudType cloudType : CloudType.values())
            if (cloudType.getType() == type)
                return cloudType.getName();
        return "Null";
    }

}
