package com.sdg.cmdb.domain.gitlab.v1;

import lombok.Data;

import java.io.Serializable;

@Data
public class Repository implements Serializable {
    private static final long serialVersionUID = -5063790932768714354L;

    private String name;
    private String url;
    private String description;
    private String homepage;
    private String git_http_url;
    private String git_ssh_url;
    private int visibility_level;

}
