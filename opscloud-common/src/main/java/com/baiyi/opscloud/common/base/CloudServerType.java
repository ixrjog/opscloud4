package com.baiyi.opscloud.common.base;


/**
 * CREATE 新建（未录入）
 * REGISTER 已录入
 * OFFLINE 存在但未销毁（手动标记）
 * DELETE( 服务器表未删除但云服务器已销毁（脏数据）
 */
public enum CloudServerType {

    PS(0),
    VM(1),
    ECS(2),
    EC2(3),
    CVM(4),
    ESXI(5),
    ZH(6);

    private int type;

    CloudServerType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
