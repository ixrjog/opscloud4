package com.sdg.cmdb.domain.todo.todoProperty;

import java.io.Serializable;

public class StashProjectDO implements Serializable{
    private static final long serialVersionUID = -4400212741416207105L;

    private long id;


    private String name;

    private String project_key;

    private String description;

    // 0 标准项目
    private int project_type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProject_key() {
        return project_key;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProject_type() {
        return project_type;
    }

    public void setProject_type(int project_type) {
        this.project_type = project_type;
    }
}
