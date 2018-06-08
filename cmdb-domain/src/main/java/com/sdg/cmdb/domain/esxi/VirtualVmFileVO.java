package com.sdg.cmdb.domain.esxi;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by liangjian on 2017/8/2.
 */
public class VirtualVmFileVO implements Serializable {
    private static final long serialVersionUID = -7374118523711106629L;

    private String path;
    private Long fileSize;
    private Calendar modification;
    private String owner;

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public Long getFileSize() {
        return fileSize;
    }
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    public Calendar getModification() {
        return modification;
    }
    public void setModification(Calendar modification) {
        this.modification = modification;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
