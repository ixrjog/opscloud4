package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/2/29 3:07 下午
 * @Version 1.0
 */
public enum CloudDBType {

    ALIYUN_RDS_MYSQL(2, "AliyunRDSMysqlCloudDB"),
    AWS_RDS_MYSQL(3, "AWSRDSMysqlCloudDB");

    private int type;
    private String name;

    CloudDBType(int type, String name) {
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
        for (CloudDBType cloudDBType : CloudDBType.values())
            if (cloudDBType.getType() == type)
                return cloudDBType.getName();
        return "Null";
    }


}
