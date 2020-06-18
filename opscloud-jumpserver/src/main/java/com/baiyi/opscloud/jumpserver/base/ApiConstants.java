package com.baiyi.opscloud.jumpserver.base;

public class ApiConstants{

    public static final  String TOKEN = "/api/users/v1/auth/";

    public static final  String USERS = "/api/users/v1/users/";
    public static final  String USERGROUPS = "/api/users/v1/groups/";
    public static final  String USER_PASSWORD_RESET = "/api/users/v1/users/{id}/password/";
    public static final  String USER_PUBLICKEY_UPDATE = "/api/users/v1/users/{id}/pubkey/update/";

    public static final  String ASSETS = "/api/assets/v1/assets/";
    public static final  String NODES = "/api/assets/v1/nodes/";
    public static final  String NODES_CHILDREN = "/api/assets/v1/nodes/{id}/children/";
    public static final  String NODES_CHILDREN_ADD = "/api/assets/v1/nodes/{id}/children/add/";
    public static final  String NODES_ASSETS_ADD = "/api/assets/v1/nodes/{id}/assets/add/";
    public static final  String NODES_ASSETS_REMOVE = "/api/assets/v1/nodes/{id}/assets/remove/";
    public static final  String LABLES = "/api/assets/v1/labels/";
    public static final  String ADMIN_USERS = "/api/assets/v1/admin-user/";
    public static final  String ADMIN_USERS_CLUSTER = "/api/assets/v1/admin-user/{id}/clusters/";
    public static final  String ADMIN_USERS_AUTH = "/api/assets/v1/admin-user/{id}/auth/";
    public static final  String SYSTEM_USERS = "/api/assets/v1/system-user/";
    public static final  String SYSTEM_USERS_AUTHINFO = "/api/assets/v1/system-user/{id}/auth-info/";
    public static final  String ASSET_PERMISSIONS = "/api/perms/v1/asset-permissions/";

    public static final  String LUNA_TOKEN = "/api/users/v1/connection-token/";
    public static final  String LUNA_TOKEN_VALIDATE = "/api/users/v1/connection-token/?token=";
    public static final  String LUNA_LINUX_CONNECT = "/luna/connect?system=linux&token=";
    public static final  String LUNA_WINDOWS_CONNECT = "/luna/connect?system=windows&token=";

    public static final  String SYSTEM_USERS_PUSH = "/api/assets/v1/system-user/{id}/push/";


}
