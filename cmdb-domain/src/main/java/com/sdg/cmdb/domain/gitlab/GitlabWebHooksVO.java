package com.sdg.cmdb.domain.gitlab;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


@Data
public class GitlabWebHooksVO implements Serializable {
    private static final long serialVersionUID = -2419670181467361008L;

    public static final String PROJECT_NAME = "name";
    public static final String PROJECT_DESCRIPTION = "description";
    public static final String REPOSITORY_NAME = "name";
    public static final String REPOSITORY_URL = "url";
    public static final String REPOSITORY_DESCRIPTION = "description";
    public static final String REPOSITORY_HOMEPAGE = "homepage";

    private String before;

    // 当前提交的sha值
    private String after;

    private String ref;

    // gitlab userId
    private int userId;

    private String userName;

    // gitlab projectId
    private int projectId;

    private int totalCommitsCount;

    private HashMap<String, String> project;

    private HashMap<String, String> repository;

    private List<CommitsVO> commits;


}
