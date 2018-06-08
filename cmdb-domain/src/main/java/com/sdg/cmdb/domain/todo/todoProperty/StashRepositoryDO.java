package com.sdg.cmdb.domain.todo.todoProperty;

import java.io.Serializable;

public class StashRepositoryDO implements Serializable {
    private static final long serialVersionUID = -1576014337007289288L;


    private String name;
    private long project_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }
}
