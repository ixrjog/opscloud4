package com.sdg.cmdb.domain.gitlab.v1;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GitlabWebHooks implements Serializable {

    public String ver = "1.0.0";

    private static final long serialVersionUID = -4806578823588739972L;

    private String object_kind;
    private String event_name;
    private String before;
    private String after;
    private String ref;
    private String checkout_sha;
    private int user_id;
    private String user_name;
    private String user_username;
    private String user_email;
    private String user_avatar;
    private int project_id;
    private Project project;
    private Repository repository;
    private List<Commits> commits;
    private int total_commits_count;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
