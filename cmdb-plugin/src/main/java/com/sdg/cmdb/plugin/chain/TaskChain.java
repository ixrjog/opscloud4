package com.sdg.cmdb.plugin.chain;

import com.sdg.cmdb.domain.BusinessWrapper;
import lombok.extern.slf4j.Slf4j;



import java.util.ArrayList;
import java.util.List;

/**
 * 任务执行链路
 */
@Slf4j
public class TaskChain {

    /**
     * 执行链路名称
     */
    private String chainName;

    /**
     * 执行过程回调通知
     */
    private TaskCallback callback;

    /**
     * 执行任务顺序链表
     */
    private List<TaskItem> taskItemList = new ArrayList<>();

    public TaskChain(String chainName, TaskCallback callback, List<TaskItem> taskItemList) {
        this.chainName = chainName;
        this.callback = callback;
        this.taskItemList = taskItemList;
    }

    /**
     * 执行链路
     */
    public void doInvoke() {
        if (taskItemList.isEmpty()) {
            return;
        }
        for(TaskItem item : taskItemList) {
            BusinessWrapper<String> result = item.runTask();
            callback.doNotify(result);
            if (!result.isSuccess()) {
                log.warn("run task={} failure, result={}", item.getTaskName(), result.getMsg());
                break;
            } else {
                log.info("run task={} success, result={}", item.getTaskName(), result.getBody());
            }
        }
    }
}
