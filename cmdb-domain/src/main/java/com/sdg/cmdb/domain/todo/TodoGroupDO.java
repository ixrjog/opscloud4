package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/9/5.
 */
public class TodoGroupDO implements Serializable {
    private static final long serialVersionUID = 7615730748146209842L;

    private long id;

    private String title;

    private String content;

    private int groupType;

    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return "TodoGroupDO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", groupType=" + groupType +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }
}
