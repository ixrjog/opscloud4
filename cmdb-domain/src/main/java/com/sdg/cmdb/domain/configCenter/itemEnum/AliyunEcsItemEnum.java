package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum AliyunEcsItemEnum {
    ALIYUN_ECS_ACCESS_KEY("ALIYUN_ECS_ACCESS_KEY", "Aliyun ECS AccessKey"),
    ALIYUN_ECS_ACCESS_SECRET("ALIYUN_ECS_ACCESS_SECRET", "Aliyun ECS AccessSecret"),
    ALIYUN_ECS_REGION_ID("ALIYUN_ECS_REGION_ID", "查询的区域:华东1,香港,美国西部1 ... 按此格式添加  regionId1,regionId2,regionId3 ..."),
    ALIYUN_ECS_SEARCH_TIME("ALIYUN_ECS_SEARCH_TIME", "查询时间（分钟）"),
    ALIYUN_ECS_IMAGE_ID("ALIYUN_ECS_IMAGE_ID", "ECS模版机配置，新建的ECS将会用此模版机"),
    ALIYUN_ECS_SECURITY_GROUP_ID("ALIYUN_ECS_SECURITY_GROUP_ID", "安全组id，新建的ecs将会加入此安全组"),
    ALIYUN_ECS_PUBLIC_NETWORK_ID("ALIYUN_ECS_PUBLIC_NETWORK_ID", "ip_network表中的阿里云公网网段")
    ;

    private String itemKey;
    private String itemDesc;

    AliyunEcsItemEnum(String itemKey, String itemDesc) {
        this.itemKey = itemKey;
        this.itemDesc = itemDesc;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public String getItemDescByKey(String itemKey) {
        for (AliyunEcsItemEnum itemEnum : AliyunEcsItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }
}