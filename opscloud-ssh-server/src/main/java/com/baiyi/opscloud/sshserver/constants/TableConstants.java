package com.baiyi.opscloud.sshserver.constants;

/**
 * @Author baiyi
 * @Date 2022/4/14 13:23
 * @Version 1.0
 */
public class TableConstants {

    public final static String[] TABLE_KUBERNETES_DEPLOYMENT_FIELD_NAMES = {"ID", "Kubernetes Instance", "Namespace", "Deployment"};

    public final static String[] TABLE_KUBERNETES_POD_FIELD_NAMES
            = {"ID", "Kubernetes Instance", "Namespace", "Pod", "Pod IP", "Start Time", "Status", "Restart", "Container"};

    public final static String[] TABLE_SERVER_FIELD_NAMES
            = {"ID", "Server", "Server Group", "Env", "IP", "Tag", "Account", "Comment"};

}