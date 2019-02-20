package com.sdg.cmdb.factory.workflow;


import java.util.HashMap;


public class WorkflowTodoFactory {



    static HashMap<String, TodoAbs> workflowMap = new HashMap<>();

    public static TodoAbs getByKey(String key) {

        return workflowMap.get(key);
    }




    public static void register(TodoAbs bean){
        workflowMap.put(bean.getKey(),bean);
    }
}
