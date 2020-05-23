package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/5/23 11:27 下午
 * @Version 1.0
 */
public enum CloudServerKey {

    AliyunECSCloudServer(2, "AliyunECSCloudServer"),
    AwsEC2CloudServer(3, "AwsEC2CloudServer"),
    VcsaESXiCloudServer(5, "VcsaESXiCloudServer"),
    VcsaVMCloudServer(1, "VcsaVMCloudServer"),
    ZabbixHostCloudServer(6, "ZabbixHostCloudServer");

    private int type;
    private String key;

    CloudServerKey(int type, String key) {
        this.type = type;
        this.key = key;
    }

    public int getType() {
        return this.type;
    }

    public String getKey() {
        return this.key;
    }

    public static String getKey(int type) {
        for (CloudServerKey  cloudServerKey : CloudServerKey .values())
            if (cloudServerKey.getType() == type)
                return cloudServerKey.getKey();
        return "Null";
    }
}
