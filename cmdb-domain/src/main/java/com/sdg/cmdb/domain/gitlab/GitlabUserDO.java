package com.sdg.cmdb.domain.gitlab;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.gitlab.api.models.GitlabUser;

import java.io.Serializable;

@Data
public class GitlabUserDO implements Serializable {
    private static final long serialVersionUID = 516660096059105214L;
    private long id;
    private int gitlabUserId;
    private long userId;
    private String username;
    private String setKey;     // 推送的公钥
    private String gmtCreate;
    private String gmtModify;

    public GitlabUserDO() {
    }

    public GitlabUserDO(GitlabUser gitlabUser, long userId) {
        this.gitlabUserId = gitlabUser.getId();
        this.username = gitlabUser.getUsername();
        this.userId = userId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
