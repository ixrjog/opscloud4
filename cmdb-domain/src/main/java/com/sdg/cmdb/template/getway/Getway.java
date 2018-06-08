package com.sdg.cmdb.template.getway;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.config.ConfigPropertyDO;
import com.sdg.cmdb.domain.config.ServerGroupPropertiesDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.template.format.BashLine;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.*;

/**
 * Created by liangjian on 2016/11/15.
 */

/**
 * 生成文件的路径为 /data/www/conf/getway/user/getway.conf
 */
public class Getway {

    final String ver = "1.0.0";

    //shell颜色 color:(31|91 红  32|92绿  33|93黄 34|94蓝 35|95紫 36|96天蓝)
    private int[] colors = {31, 32, 33, 34, 35, 36, 91, 92, 93, 94, 95, 96};

    /**
     * 随机获取一个颜色
     *
     * @return
     */
    private int getColor() {
        int index = (int) (Math.random() * colors.length);
        return colors[index];
    }

    //文件头信息
    private String headInfo = "";

    private String getHeadInfo() {
        if (StringUtils.isEmpty(headInfo)) {
            FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
            this.headInfo = "# Created by cmdb on " + fastDateFormat.format(new Date()) + "\n\n";
        }
        return this.headInfo;
    }

    private List<ServerGroupDO> serverGroups;

    private Map<String, List<ServerDO>> servers;

    public Getway() {
    }

    /**
     * 创建用户配置内容 getway.conf
     *
     * @param userDO
     * @param serverGroups
     */
    public Getway(UserDO userDO, List<ServerGroupDO> serverGroups) {
        addUser(userDO);
        addServerGroup(serverGroups);
        build();
    }

    /**
     * 创建服务器配置列表内容 getway_host.conf
     *
     * @param servers
     */
    public Getway(List<ServerGroupDO> serverGroups, Map<String, List<ServerDO>> servers) {
        addServerGroup(serverGroups);
        addServers(servers);
        build();
    }


    private String path;

    /**
     * 获取文件路径
     * 参考  /data/www/conf/getway/xqadmin/getway.conf
     *
     * @return
     */
    public String getPath() {
        return this.path;
    }

    private void setPath(UserDO userDO) {
        this.path = "/data/www/conf/getway/" + userDO.getUsername() + "/getway.conf";
    }

    private UserDO userDO;


    public void addUser(UserDO userDO) {
        if (userDO == null) return;
        this.userDO = userDO;
        BashLine bashlineCn = new BashLine("CN", userDO.getUsername());
        setPath(userDO);
        this.user = bashlineCn.toLine();
        if (userDO.getDisplayName() == null) {
            userDO.setDisplayName(userDO.getUsername());
        }
        BashLine bashlineUser = new BashLine("NAME", userDO.getDisplayName());
        this.user = this.user + bashlineUser.toLine();
        if (userDO.getMail() == null) {
            userDO.setMail(userDO.getUsername() + "@51xianqu.net");
        }
        BashLine bashlineEmail = new BashLine("EMAIL", userDO.getMail());
        this.user = this.user + bashlineEmail.toLine();
    }


    public void addServerGroup(List<ServerGroupDO> serverGroups) {
        if (serverGroups == null) return;
        this.serverGroups = serverGroups;
    }

    public void addServers(Map<String, List<ServerDO>> servers) {
        if (servers == null) return;
        this.servers = servers;
    }

    private void build() {
        headInfo = getHeadInfo();
        if (this.userDO != null && this.serverGroups != null) {
            buildHostgroup();
        }
        if (this.serverGroups != null && this.servers != null) {
            buildHosts();
        }
    }

    /**
     * build HOSTGROUP_OPT
     */
    private void buildHostgroup() {

        Collections.sort(serverGroups,new Comparator<ServerGroupDO>(){
            @Override
            public int compare(ServerGroupDO arg0, ServerGroupDO arg1) {
                return arg0.getName().compareTo(arg1.getName());
            }
        });

        for (ServerGroupDO group : serverGroups) {
            String content = group.getContent();
            if (content == null || content.isEmpty()) {
                content = group.getName();
            }
            hostgroupOpt = hostgroupOpt + "'" + convertGroupname(group.getName()) + "' '" + content + "' '" + getColor() + "'\n";
            //buildHostgroup(group);
        }
        hostgroupOpt = "HOSTGROUP_OPT=( \n" + hostgroupOpt + ")";
    }

    /**
     * 转换服务器组名称
     * 小写转大写
     * 转换"-" "_"
     * 去除"group_"
     *
     * @param name
     * @return
     */
    private String convertGroupname(String name) {
        String n = name;
        n = n.replace("-", "_");
        n = n.replace("group_", "");
        n = n.toUpperCase();
        return n;
    }

    private void buildHosts() {
        for (ServerGroupDO group : serverGroups) {
            buildHosts(group);
        }
    }

    /**
     * build HOSTGROUP_x
     */
    private void buildHosts(ServerGroupDO group) {
        if (group == null) return;
        List<ServerDO> hosts = this.servers.get(group.getName());
        if (hosts == null) return;
        //排序
        Collections.sort(hosts, new Comparator<ServerDO>() {
            public int compare(ServerDO arg0, ServerDO arg1) {
                // return arg0.getEnvType().compareTo(arg1.getEnvType());
               // return ServerDO.EnvTypeEnum.getEnvTypeName(arg0.getEnvType()).compareTo(ServerDO.EnvTypeEnum.getEnvTypeName(arg1.getEnvType()));
                return new Double(String.valueOf(arg0.getEnvType())).compareTo(new Double(String.valueOf(arg1.getEnvType())));
            }
        });

        String hostgroup = "";
        for (ServerDO host : hosts) {
            hostgroup = hostgroup + "'" + host.getInsideIp() + "' '" + host.getServerName() + "-" + host.getSerialNumber() + "' '" + group.getName() + "' '" + ServerDO.EnvTypeEnum.getEnvTypeName(host.getEnvType()) + "' 'KEY'\n";
        }
        hostgroup = "HOSTGROUP_" + convertGroupname(group.getName()) + "=(\n" + hostgroup + ")\n\n";
        this.hostgroups = this.hostgroups + hostgroup;
    }

    private String user = "";

    private String hostgroupOpt = "";

    private String hostgroups = "";

    @Override
    public String toString() {
        return this.headInfo + this.user + "\n" + hostgroupOpt + "\n\n" + hostgroups;
    }

}
