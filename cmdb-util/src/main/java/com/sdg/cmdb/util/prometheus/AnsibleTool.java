package com.sdg.cmdb.util.prometheus;

/**
 * Created by liangjian on 16/10/14.
 */
public class AnsibleTool extends AbsTools {

    /**
     * ansible执行命令完整路径
     */
    private String ansible_bin = "/usr/bin/ansible";


    protected  String getBinPath() {
        return null;
    };


    @Override
    protected String getCmdPath() {
        return this.ansible_bin;
    }


    public void setAnsible_bin(String ansible_bin) {
        if (ansible_bin == null) return;
        this.ansible_bin = ansible_bin;
    }

    /**
     * 批量执行的主机组名称
     */
    private String host_group;

    public void setHost_group(String host_group) {
        if (host_group == null) return;
        this.host_group = host_group;
    }

    /**
     * ansible并发线程数字,默认1
     */
    private int ansible_forks = 1;

    public void setAnsible_forks(int forks) {
        this.ansible_forks = forks;
    }

    private boolean isSudo = true;

    public void setIsSudo(boolean isSudo) {
        this.isSudo = isSudo;
    }

    private String cmdType = "-a";

    public void setCmdType(String cmdType) {
        if (cmdType == null) return;
        this.cmdType = cmdType;
    }

    private String cmd;

    public void setCmd(String cmd) {
        if (cmd == null) return;
        this.cmd = "\"" + cmd + "\"";
    }

    @Override
    protected void makeParams() {
        putParam(this.host_group);
        if (this.isSudo = true) {
            putParam("-sudo");
        }
        putParam("-f");
        putParam(String.valueOf(this.ansible_forks));
        putParam(this.cmdType);
        putParam(this.cmd);
    }

    /**
     * @param host_group
     * @param isSudo
     * @param ansible_forks
     * @param cmdType
     * @param cmd
     */
    public AnsibleTool(String host_group, boolean isSudo, int ansible_forks, String cmdType, String cmd) {
        super();
        setHost_group(host_group);
        setIsSudo(isSudo);
        setAnsible_forks(ansible_forks);
        setCmdType(cmdType);
        setCmd(cmd);
    }

    public AnsibleTool(String host_group, int ansible_forks, String cmd) {
        super();
        setHost_group(host_group);
        setAnsible_forks(ansible_forks);
        setCmd(cmd);
    }

}
