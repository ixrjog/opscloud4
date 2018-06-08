package com.sdg.cmdb.domain.todo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liangjian on 2017/9/5.
 */
public class TodoGroupVO implements Serializable {
    private static final long serialVersionUID = -524917942763069915L;

    private TodoGroupDO todoGroupDO;

    private List<TodoDO> todoList;

    public TodoGroupVO() {
    }

    public TodoGroupVO(TodoGroupDO todoGroupDO, List<TodoDO> todoList) {
        this.todoGroupDO = todoGroupDO;
        this.todoList = todoList;
    }

    public TodoGroupDO getTodoGroupDO() {
        return todoGroupDO;
    }

    public void setTodoGroupDO(TodoGroupDO todoGroupDO) {
        this.todoGroupDO = todoGroupDO;
    }

    public List<TodoDO> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<TodoDO> todoList) {
        this.todoList = todoList;
    }
}
