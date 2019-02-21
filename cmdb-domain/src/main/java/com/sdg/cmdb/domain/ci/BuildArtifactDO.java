package com.sdg.cmdb.domain.ci;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class BuildArtifactDO implements Serializable {
    private static final long serialVersionUID = -4126522133020526438L;

    public BuildArtifactDO() {

    }

    public BuildArtifactDO(long buildId, String artifactName, String archiveUrl) {
        this.buildId = buildId;
        this.artifactName = artifactName;
        this.archiveUrl = archiveUrl;
    }

    private long id;
    private long buildId;
    private String artifactName;
    private String archiveUrl;
    private String ossPath;
    // 容量，单位Byte
    private int artifactSize;
    private String gmtCreate;
    private String gmtModify;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
