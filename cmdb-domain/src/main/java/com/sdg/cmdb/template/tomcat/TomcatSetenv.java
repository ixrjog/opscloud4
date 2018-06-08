package com.sdg.cmdb.template.tomcat;

import com.sdg.cmdb.domain.config.ConfigPropertyDO;
import com.sdg.cmdb.template.format.BashLine;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 16/10/11.
 */
public class TomcatSetenv  {

    final String ver = "1.0.0";

    //文件头信息
    private String headInfo;

    //TOMCAT_APP_NAME_OPT
    final static String tomcat_app_name_opt_key = "TOMCAT_APP_NAME_OPT";
    private String tomcat_app_name_opt_value;

    public void set_tomcat_app_name_opt_value(String v) {
        if (v == null) return;
        this.tomcat_app_name_opt_value = v;
    }

    //TOMCAT_HTTP_PORT_OPT
    final static String tomcat_http_port_opt_key = "TOMCAT_HTTP_PORT_OPT";
    private String tomcat_http_port_opt_value = "8080";

    public void set_tomcat_http_port_opt_value(String v) {
        if (v == null) return;
        this.tomcat_http_port_opt_value = v;
    }

    //TOMCAT_SHUTDOWN_PORT_OPT
    final static String tomcat_shutdown_port_opt_key = "TOMCAT_SHUTDOWN_PORT_OPT";
    private String tomcat_shutdown_port_opt_value = "8000";

    public void set_tomcat_shutdown_port_opt_value(String v) {
        if (v == null) return;
        this.tomcat_shutdown_port_opt_value = v;
    }

    //TOMCAT_JMX_rmiRegistryPortPlatform_OPT
    final static String tomcat_jmx_rmi_registry_port_platform_key = "TOMCAT_JMX_rmiRegistryPortPlatform_OPT";
    private String tomcat_jmx_rmi_registry_port_platform_value = "10000";

    public void set_tomcat_jmx_rmi_registry_port_platform_value(String v) {
        if (v == null) return;
        this.tomcat_jmx_rmi_registry_port_platform_value = v;
    }

    //TOMCAT_JMX_rmiServerPortPlatform_OPT
    final static String tomcat_jmx_rmi_server_port_platform_key = "TOMCAT_JMX_rmiServerPortPlatform_OPT";
    private String tomcat_jmx_rmi_server_port_platform_value = "10100";

    public void set_tomcat_jmx_rmi_server_port_platform_value(String v) {
        if (v == null) return;
        this.tomcat_jmx_rmi_server_port_platform_value = v;
    }

    //TOMCAT_SERVERXML_WEBAPPSPATH_OPT
    final static String tomcat_serverxml_webappspath_key = "TOMCAT_SERVERXML_WEBAPPSPATH_OPT";
    private String tomcat_serverxml_webappspath_value;

    public void set_tomcat_serverxml_webappspath_value(String v) {
        if (v == null) return;
        this.tomcat_serverxml_webappspath_value = v;
    }


    //HTTP_STATUS_OPT
    final static String http_status_opt_key = "HTTP_STATUS_OPT";
    private String http_status_opt_value="webStatus";

    public void set_http_status_opt_value(String v) {
        if (v == null || v.isEmpty()) return;
        this.http_status_opt_value = v;
    }

    //APP_CONF_NAME_OPT
    final static String app_conf_name_key = "APP_CONF_NAME_OPT";
    private String app_conf_name_value ;

    public void set_app_conf_name_value(String v) {
        if (v == null) return;
        this.app_conf_name_value = v;
    }

    //DEL_LOGS
    final static String del_logs_key = "DEL_LOGS";
    private String del_logs_value = "false";

    public void set_del_logs_value(String v) {
        if (v == null) return;
        this.del_logs_value = v;
    }

    //BACKUP_WAR
    final static String backup_war_key = "BACKUP_WAR";
    private String backup_war_value = "false";

    public void set_backup_war_value(String v) {
        if (v == null) return;
        this.backup_war_value = v;
    }

    /**
     * 定义tomcat默认安装路径
     * 没有默认值必须设置，建议
     * TOMCAT_INSTALL_PATH='/usr/local'
     */
    final static String tomcat_install_path_key = "TOMCAT_INSTALL_PATH";
    private String tomcat_install_path_value = "/usr/local";

    public void set_tomcat_install_path_value(String v) {
        if (v == null) return;
        this.tomcat_install_path_value = v;
    }

    /**
     * 强制编码 留空则不强制编码
     * 默认值 TOMCAT_HTTP_URI_ENCODING=
     * TOMCAT_HTTP_URI_ENCODING=utf8
     */
    final static String tomcat_http_url_encoding_key = "TOMCAT_HTTP_URI_ENCODING";
    private String tomcat_http_url_encoding_value = "utf8";

    public void set_tomcat_http_url_encoding_value(String v) {
        if (v == null) return;
        this.tomcat_http_url_encoding_value = v;
    }

    /**
     * 启动参数(JAVA_OPTS)支持
     * true:生效TOMCAT_JAVA_OPTS
     * false:不生效TOMCAT_JAVA_OPTS
     * 默认值 OPEN_TOMCAT_JAVA_OPTS=false
     */
    final static String open_tomcat_java_opts_key = "OPEN_TOMCAT_JAVA_OPTS";
    private String open_tomcat_java_opts_value = "true";

    public void set_open_tomcat_java_opts_value(String v) {
        if (v == null) return;
        this.open_tomcat_java_opts_value = v;
    }

    /**
     * 对应tomcat启动变量JAVA_OPTS设置
     * 若要启用，需要设置OPEN_TOMCAT_JAVA_OPTS=true
     * 若启用，且当前tomcat不需要启动参数 则输入-
     */
    final static String tomcat_java_opts_key = "TOMCAT_JAVA_OPTS";
    private String tomcat_java_opts_value = "-";

    public void set_tomcat_java_opts_value(String v) {
        if (v == null) return;
        this.tomcat_java_opts_value = v;
    }

    /**
     * 健康检查等待时间；默认5秒
     * HTTP_STATUS_TIME=5
     */
    final static String http_status_time_key = "HTTP_STATUS_TIME";
    private String http_status_time_value;

    public void set_http_status_time_value(String v) {
        if (v == null) return;
        this.http_status_time_value = v;
    }

    /**
     * Xss 默认256k
     * SET_JVM_Xss=256k
     */
    final static String set_jvm_xss_key = "SET_JVM_Xss";
    private String set_jvm_xss_value;

    public void set_set_jvm_xss_value(String v) {
        if (v == null) return;
        this.set_jvm_xss_value = v;
    }

    //SET_JVM_Xms
    final static String set_jvm_xms_key = "SET_JVM_Xms";
    private String set_jvm_xms_value;

    public void set_set_jvm_xms_value(String v) {
        if (v == null) return;
        this.set_jvm_xms_value = v;
    }

    //SET_JVM_Xmx
    final static String set_jvm_xmx_key = "SET_JVM_Xmx";
    private String set_jvm_xmx_value;

    public void set_set_jvm_xmx_value(String v) {
        if (v == null) return;
        this.set_jvm_xmx_value = v;
    }

    //SET_JVM_Xmn
    final static String set_jvm_xmn_key = "SET_JVM_Xmn";
    private String set_jvm_xmn_value;

    public void set_set_jvm_xmn_value(String v) {
        if (v == null) return;
        this.set_jvm_xmn_value = v;
    }

    /**
     * 返回TomcatSetenv
     * @param h
     * @return
     */
    public static TomcatSetenv builder(HashMap<String, String> h) {
        if (h == null) return null;
        TomcatSetenv ts = new TomcatSetenv();
        HashMap<String, String> data = h;
        ts.set_tomcat_app_name_opt_value(data.get(tomcat_app_name_opt_key));
        ts.set_tomcat_http_port_opt_value(data.get(tomcat_http_port_opt_key));
        ts.set_tomcat_shutdown_port_opt_value(data.get(tomcat_shutdown_port_opt_key));
        ts.set_tomcat_jmx_rmi_registry_port_platform_value(data.get(tomcat_jmx_rmi_registry_port_platform_key));
        ts.set_tomcat_jmx_rmi_server_port_platform_value(data.get(tomcat_jmx_rmi_server_port_platform_key));
        ts.set_tomcat_serverxml_webappspath_value(data.get(tomcat_serverxml_webappspath_key));
        ts.set_http_status_opt_value(data.get(http_status_opt_key));
        ts.set_app_conf_name_value(data.get(app_conf_name_key));
        ts.set_del_logs_value(data.get(del_logs_key));
        ts.set_backup_war_value(data.get(backup_war_key));
        ts.set_tomcat_install_path_value(data.get(tomcat_install_path_key));
        ts.set_tomcat_http_url_encoding_value(data.get(tomcat_http_url_encoding_key));
        ts.set_open_tomcat_java_opts_value(data.get(open_tomcat_java_opts_key));
        ts.set_tomcat_java_opts_value(data.get(tomcat_java_opts_key));
        ts.set_http_status_time_value(data.get(http_status_time_key));
        ts.set_set_jvm_xss_value(data.get(set_jvm_xss_key));
        ts.set_set_jvm_xms_value(data.get(set_jvm_xms_key));
        ts.set_set_jvm_xmx_value(data.get(set_jvm_xmx_key));
        ts.set_set_jvm_xmn_value(data.get(set_jvm_xmn_key));
        return ts;
    }

    /**
     * 返回TomcatSetenv
     * @param propertyDOList
     * @return
     */
    public static TomcatSetenv builder(List<ConfigPropertyDO> propertyDOList){
        HashMap<String, String> data = new HashMap<>();
        for(ConfigPropertyDO propertyDO : propertyDOList){
            data.put(propertyDO.getProName(), propertyDO.getProValue());
        }
        return builder(data);
    }

    public String getPath(String bashPath, String fileName) {
        return bashPath + "/" + this.tomcat_app_name_opt_value + "/" + fileName;
    }

    /**
     * 生成tomcat_setenv.conf文件内容
     */
    public String toBody() {
        BashLine bl = new BashLine();
        this.invoke(bl);
        return this.getHeadInfo()+bl.getLines();
    }

    private void invoke(BashLine bashLines) {
        if (bashLines == null) return;
        //TOMCAT_APP_NAME_OPT
        bashLines.put(BashLine.builder(this.tomcat_app_name_opt_key, this.tomcat_app_name_opt_value, BashLine.bash_format_list));
        //TOMCAT_HTTP_PORT_OPT
        bashLines.put(BashLine.builder(this.tomcat_http_port_opt_key, this.tomcat_http_port_opt_value, BashLine.bash_format_list));
        //TOMCAT_SHUTDOWN_PORT_OPT
        bashLines.put(BashLine.builder(this.tomcat_shutdown_port_opt_key, this.tomcat_shutdown_port_opt_value, BashLine.bash_format_list));
        //TOMCAT_JMX_rmiRegistryPortPlatform_OPT
        bashLines.put(BashLine.builder(this.tomcat_jmx_rmi_registry_port_platform_key, this.tomcat_jmx_rmi_registry_port_platform_value, BashLine.bash_format_list));
        //TOMCAT_JMX_rmiServerPortPlatform_OPT
        bashLines.put(BashLine.builder(this.tomcat_jmx_rmi_server_port_platform_key, this.tomcat_jmx_rmi_server_port_platform_value, BashLine.bash_format_list));
        //TOMCAT_SERVERXML_WEBAPPSPATH_OPT
        bashLines.put(BashLine.builder(this.tomcat_serverxml_webappspath_key, this.tomcat_serverxml_webappspath_value, BashLine.bash_format_list));
        //HTTP_STATUS_OPT
        bashLines.put(BashLine.builder(this.http_status_opt_key, this.http_status_opt_value, BashLine.bash_format_list));
        //APP_CONF_NAME_OPT
        bashLines.put(BashLine.builder(this.app_conf_name_key, this.app_conf_name_value, BashLine.bash_format_list));
        //DEL_LOGS
        bashLines.put(BashLine.builder(this.del_logs_key, this.del_logs_value));
        //BACKUP_WAR
        bashLines.put(BashLine.builder(this.backup_war_key, this.backup_war_value));
        //TOMCAT_INSTALL_PATH
        bashLines.put(BashLine.builder(this.tomcat_install_path_key, this.tomcat_install_path_value));
        //TOMCAT_HTTP_URI_ENCODING
        bashLines.put(BashLine.builder(this.tomcat_http_url_encoding_key, this.tomcat_http_url_encoding_value));
        //OPEN_TOMCAT_JAVA_OPTS
        bashLines.put(BashLine.builder(this.open_tomcat_java_opts_key, this.open_tomcat_java_opts_value));
        //TOMCAT_JAVA_OPTS
        bashLines.put(BashLine.builder(this.tomcat_java_opts_key, this.tomcat_java_opts_value, BashLine.bash_format_list));
        //HTTP_STATUS_TIME
        bashLines.put(BashLine.builder(this.http_status_time_key, this.http_status_time_value));
        //SET_JVM_Xss
        bashLines.put(BashLine.builder(this.set_jvm_xss_key, this.set_jvm_xss_value));
        //SET_JVM_Xms
        bashLines.put(BashLine.builder(this.set_jvm_xms_key, this.set_jvm_xms_value));
        //SET_JVM_Xmx
        bashLines.put(BashLine.builder(this.set_jvm_xmx_key, this.set_jvm_xmx_value));
        //SET_JVM_Xmn
        bashLines.put(BashLine.builder(this.set_jvm_xmn_key, this.set_jvm_xmn_value));
    }

    public void setHeadInfo(String headInfo) {
        this.headInfo = headInfo;
    }

    private String getHeadInfo() {
        if (StringUtils.isEmpty(headInfo)) {
            FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
            this.headInfo = "# Created by cmdb on " + fastDateFormat.format(new Date()) + "\n\n";
        }
        return this.headInfo;
    }
}