package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/9/5.
 */
public class TodoDO implements Serializable {
    private static final long serialVersionUID = 5427411491545874232L;

    private long id;

    private long todoGroupId;

    private boolean approval;

    private String name;

    private String title;

    // 工单状态 0:正常  1:暂时关闭   2:开发中
    private int todoStatus;

    // 帮助连接
    private String helpLink;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    private int todoType;

    public enum TodoTypeEnum {
        // 类型代表处理者角色
        //运维工程师
        devops(0, "devops"),
        dba(1, "dba");
        private int code;
        private String desc;

        TodoTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getTodoTypeName(int code) {
            for (TodoTypeEnum todoTypeEnum : TodoTypeEnum.values()) {
                if (todoTypeEnum.getCode() == code) {
                    return todoTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    @Override
    public String toString() {
        return "TodoGroupDO{" +
                "id=" + id +
                ", todoGroupId=" + todoGroupId +
                ", approval=" + approval +
                ", todoType=" + todoType +
                ", todoStatus=" + todoStatus +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", helpLink='" + helpLink + '\'' +
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

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public int getTodoType() {
        return todoType;
    }

    public void setTodoType(int todoType) {
        this.todoType = todoType;
    }

    public long getTodoGroupId() {
        return todoGroupId;
    }

    public void setTodoGroupId(long todoGroupId) {
        this.todoGroupId = todoGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(int todoStatus) {
        this.todoStatus = todoStatus;
    }

    public String getHelpLink() {
        return helpLink;
    }

    public void setHelpLink(String helpLink) {
        this.helpLink = helpLink;
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
