package com.baiyi.opscloud.common.base;


/**
 * CREATE 新建（未录入）
 * REGISTER 已录入
 * OFFLINE 存在但未销毁（手动标记）
 * DELETE( 服务器表未删除但云服务器已销毁（脏数据）
 */
public enum CloudServerType {

    PS(0, "PS"),
    VM(1, "VM"),
    ECS(2, "ECS"),
    EC2(3, "EC2"),
    CVM(4, "CVM"),
    ESXI(5, "ESXI"),
    ZH(6, "ZH");

    private int type;
    private String name;

    CloudServerType(int type, String name) {
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
        for (CloudServerType cloudServerType : CloudServerType.values())
            if (cloudServerType.getType() == type)
                return cloudServerType.getName();
        return "Null";
    }
}


