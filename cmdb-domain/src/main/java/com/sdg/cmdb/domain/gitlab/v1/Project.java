package com.sdg.cmdb.domain.gitlab.v1;

import lombok.Data;

import java.io.Serializable;

@Data
public class Project implements Serializable {
    private static final long serialVersionUID = -2573792319967242086L;


    private long id;
    private String name;
    private String description;
    private String web_url;
    private String avatar_url;
    private String git_ssh_url;
    private String git_http_url;
    private String namespace;
    private int visibility_level;
    private String path_with_namespace;
    private String default_branch;
    private String homepage;
    private String url;
    private String ssh_url;
    private String http_url;


}
