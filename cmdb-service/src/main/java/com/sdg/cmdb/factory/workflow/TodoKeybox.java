package com.sdg.cmdb.factory.workflow;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailAbs;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailKeybox;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailDO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailVO;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Service
public class TodoKeybox extends TodoAbs implements Serializable {

    private static final long serialVersionUID = 6101632469561002018L;
    private final String TODO_KEY = "KEYBOX";

    /**
     * 工作流名称
     *
     * @return
     */
    @Override
    public String getKey() {
        return TODO_KEY;
    }


    /**
     * 暂存
     *
     * @param workflowTodoVO
     * @return
     */
    @Override
    public WorkflowTodoVO saveTodo(WorkflowTodoVO workflowTodoVO) {
        List<WorkflowTodoDetailVO> todoDetails = workflowTodoVO.getTodoDetails();
        // 去重
        HashMap<String, WorkflowTodoDetailDO> map = new HashMap<>();
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            WorkflowTodoDetailDO workflowTodoDetailDO = getTodoDetailDO(workflowTodoDetailVO);
            TodoDetailKeybox keyboxDetail = (TodoDetailKeybox) getTodoDetailVO(workflowTodoDetailDO).getDetail();
            workflowTodoDetailDO.setName(keyboxDetail.getServerGroupDO().getName());
            if (map.containsKey(keyboxDetail.getServerGroupDO().getName())) {
                continue;
            } else {
                if (!saveTodoDetail(workflowTodoDetailDO)) return workflowTodoVO;
                map.put(keyboxDetail.getServerGroupDO().getName(), workflowTodoDetailDO);
            }
        }
        return getTodo(workflowTodoVO.getId());
    }

    protected WorkflowTodoDetailVO getTodoDetailVO(WorkflowTodoDetailDO todoDetailDO) {
        // 对象转json
        Gson gson = new GsonBuilder().create();
        TodoDetailKeybox keyboxDetail = gson.fromJson(todoDetailDO.getDetailValue(), TodoDetailKeybox.class);
        return new WorkflowTodoDetailVO(keyboxDetail, todoDetailDO);
    }


    private WorkflowTodoDetailDO getTodoDetailDO(WorkflowTodoDetailVO workflowTodoDetailVO) {
        Gson gson = new GsonBuilder().create();
        TodoDetailKeybox keyboxDetail = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), TodoDetailKeybox.class);

        // TodoDetailKeybox keyboxDetail = (TodoDetailKeybox) workflowTodoDetailVO.getDetail();
        WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(workflowTodoDetailVO, keyboxDetail.toString());
        return workflowTodoDetailDO;
    }

    @Override
    public boolean applyTodo(long todoId) {

        //WorkflowTodoVO workflowTodoVO
//        try {
//            HashMap<String, TodoKeybox> detailMap = (HashMap<String, TodoKeybox>) workflowTodoVO.getDetails();
//            // Map<String, String> map = new HashMap<String, String>();
//            for (String key : detailMap.keySet()) {
//                TodoKeybox todoKeybox = detailMap.get(key);
//            }
//
//
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
        return false;
    }


}
