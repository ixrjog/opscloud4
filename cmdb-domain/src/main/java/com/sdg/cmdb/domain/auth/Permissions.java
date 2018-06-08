package com.sdg.cmdb.domain.auth;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/29.
 */
public class Permissions implements Serializable {
    private static final long serialVersionUID = 8458887497641578664L;

    /**
     * 查看所有服务器组数据
     */
    public static String lookAllServerGroup = "lookAllServerGroup";

    /**
     * 修改用户数据
     */
    public static String canEditUserInfo = "canEdit";
}
