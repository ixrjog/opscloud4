package com.baiyi.opscloud.common.base;


/**
 * CREATE 新建（未录入）
 * REGISTER 已录入
 * OFFLINE 存在但未销毁（手动标记）
 * DELETE( 服务器表未删除但云服务器已销毁（脏数据）
 */
public enum CloudServerStatus {

    CREATE(0),
    REGISTER(1),
    OFFLINE(2),
    DELETE(3);

    private int status;

    CloudServerStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
