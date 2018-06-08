package com.sdg.cmdb.plugin.chain;

import com.sdg.cmdb.domain.BusinessWrapper;

public abstract class TaskItem {
    /**
     * 任务名称
     */
    private String taskName;

    public TaskItem(String taskName) {
        this.taskName = taskName;
    }

    /**
     * 运行任务
     */
    public abstract BusinessWrapper<String> runTask();

    public String getTaskName() {
        return taskName;
    }
}
