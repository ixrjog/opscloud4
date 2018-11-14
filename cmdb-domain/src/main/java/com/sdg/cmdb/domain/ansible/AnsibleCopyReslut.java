package com.sdg.cmdb.domain.ansible;

import java.io.Serializable;

public class AnsibleCopyReslut implements Serializable {

    private static final long serialVersionUID = -1368018183047521615L;
    /**
     *
     "changed": true,
     "checksum": "e989084b3f4610a41811c5ea280b14f7c5e855f5",
     "dest": "/root/liuhao/test.sh",
     "gid": 0,
     "group": "root",
     "md5sum": "7c211ce4c7941a5bb064e77d69e3d9ff",
     "mode": "0644",
     "owner": "root",
     "secontext": "system_u:object_r:admin_home_t:s0",
     "size": 23,
     "src": "/root/.ansible/tmp/ansible-tmp-1515736119.26-238832413210409/source",
     "state": "file",
     "uid": 0
     */

    private boolean changed;

    private String  checksum;
    private String dest;
    private String group;
    private String md5sum;
    private String owner;
    private String src;
    private String state;

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
