package com.sdg.cmdb.domain.gitlab.v1;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Commits implements Serializable {
    private static final long serialVersionUID = 5881824486422012468L;

    private String id;
    private String message;
    private String timestamp;
    private String url;

    private List<String> added;
    private List<String> modified;
    private List<String> removed;

    private Author author;

}
