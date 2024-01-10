package com.baiyi.opscloud.common.base;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Author baiyi
 * @Date 2020/3/3 2:18 下午
 * @Version 1.0
 */
public class Global {

    public static final String SUPER_ADMIN = "super_admin";

    public static final String CREATED_BY = "Created by opscloud4";

    public static final int EXTEND = 1;
    public static final int NOT_EXTEND = 0;

    // 高权限账户
    // public static final String HIGH_AUTHORITY_ACCOUNT = "admin";

    public static final String ENV_PROD = "prod";

    public static final String ENV_PRE = "pre";

    public static final String ENV_DAILY = "daily";

    @Schema(description = "资产转换的默认环境")
    public static final String DEF_ENV = ENV_PROD;


    public static final String ADMIN = "ADMIN";

    @Schema(description = "自动部署")
    public static final String AUTO_DEPLOY = "AutoDeploy";

    @Schema(description = "自动构建")
    public static final String AUTO_BUILD = "AutoBuild";


    @Schema(description = "默认分组数")
    public static final int DEF_NUM_OF_GROUPS = 1;

}