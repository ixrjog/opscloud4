package com.sdg.cmdb.domain.nginx;

import java.io.Serializable;

public class NginxFile implements Serializable {
    private static final long serialVersionUID = -3392937481481772643L;

    // 文件内容
    private String file;

    // 路径
    private String path;

    private VhostDO vhostDO;

    private VhostEnvDO vhostEnvDO;

    private EnvFileDO envFileDO;


    public NginxFile() {

    }

    public NginxFile(VhostDO vhostDO, VhostEnvDO vhostEnvDO, EnvFileDO envFileDO, String path) {
        this.vhostDO = vhostDO;
        this.vhostEnvDO = vhostEnvDO;
        this.envFileDO = envFileDO;
        this.path = path;
    }

    public NginxFile(VhostDO vhostDO, VhostEnvDO vhostEnvDO, EnvFileDO envFileDO, String file, String path) {
        this.vhostDO = vhostDO;
        this.vhostEnvDO = vhostEnvDO;
        this.envFileDO = envFileDO;
        this.file = file;
        this.path = path;
    }


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public VhostDO getVhostDO() {
        return vhostDO;
    }

    public void setVhostDO(VhostDO vhostDO) {
        this.vhostDO = vhostDO;
    }

    public VhostEnvDO getVhostEnvDO() {
        return vhostEnvDO;
    }

    public void setVhostEnvDO(VhostEnvDO vhostEnvDO) {
        this.vhostEnvDO = vhostEnvDO;
    }

    public EnvFileDO getEnvFileDO() {
        return envFileDO;
    }

    public void setEnvFileDO(EnvFileDO envFileDO) {
        this.envFileDO = envFileDO;
    }
}
