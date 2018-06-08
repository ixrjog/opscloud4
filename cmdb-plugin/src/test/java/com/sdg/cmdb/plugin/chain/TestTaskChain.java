package com.sdg.cmdb.plugin.chain;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.BusinessWrapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestTaskChain {

    @Test
    public void test() {
        TaskCallback callback = new TaskCallback() {
            @Override
            public void doNotify(Object notify) {
                //获取到执行结果，并响应前端
                System.err.println(JSON.toJSONString(notify));
            }
        };

        List list = new ArrayList();

        TaskItem taskItem = new TaskItem("asas") {
            @Override
            public BusinessWrapper<String> runTask() {
                return null;
            }
        };

        list.add(taskItem);

        TaskChain taskChain = new TaskChain("initSystem", callback, list);
        taskChain.doInvoke();
    }
}
