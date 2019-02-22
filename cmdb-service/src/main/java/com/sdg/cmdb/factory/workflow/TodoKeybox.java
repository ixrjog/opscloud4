package com.sdg.cmdb.factory.workflow;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerVO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailKeybox;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailDO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailVO;
import com.sdg.cmdb.service.KeyBoxService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

@Service
public class TodoKeybox extends TodoAbs implements Serializable {

    private static final long serialVersionUID = 6101632469561002018L;
    private final String TODO_KEY = "KEYBOX";

    @Resource
    private KeyBoxService keyBoxService;

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
        String notice = "服务器组：";
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            WorkflowTodoDetailDO workflowTodoDetailDO = getTodoDetailDO(workflowTodoDetailVO);
            TodoDetailKeybox keyboxDetail = (TodoDetailKeybox) getTodoDetailVO(workflowTodoDetailDO).getDetail();
            workflowTodoDetailDO.setName(keyboxDetail.getServerGroupDO().getName());
            if (map.containsKey(keyboxDetail.getServerGroupDO().getName())) {
                continue;
            } else {
                if (!saveTodoDetail(workflowTodoDetailDO)) return workflowTodoVO;
                map.put(keyboxDetail.getServerGroupDO().getName(), workflowTodoDetailDO);
                notice += workflowTodoDetailVO.getName() + ";";
            }
        }
        workflowTodoVO.setNotice(notice);
        saveTodoUser(workflowTodoVO);
        updateTodo(workflowTodoVO);
        return getTodo(workflowTodoVO.getId());
    }

    @Override
    protected Type getType() {
        return TodoDetailKeybox.class;
    }

    protected WorkflowTodoDetailVO getTodoDetailVO(WorkflowTodoDetailDO todoDetailDO) {
        // 对象转json
        Gson gson = new GsonBuilder().create();
        TodoDetailKeybox keyboxDetail = gson.fromJson(todoDetailDO.getDetailValue(), getType());
        return new WorkflowTodoDetailVO(keyboxDetail, todoDetailDO);
    }


    private WorkflowTodoDetailDO getTodoDetailDO(WorkflowTodoDetailVO workflowTodoDetailVO) {
        Gson gson = new GsonBuilder().create();
        TodoDetailKeybox keyboxDetail = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), getType());

        // TodoDetailKeybox keyboxDetail = (TodoDetailKeybox) workflowTodoDetailVO.getDetail();
        WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(workflowTodoDetailVO, keyboxDetail.toString());
        return workflowTodoDetailDO;
    }


    @Override
    protected boolean invokeTodoDetails(WorkflowTodoVO todoVO) {
        List<WorkflowTodoDetailVO> todoDetails = todoVO.getTodoDetails();
        UserDO userDO = userDao.getUserById(todoVO.getApplyUserId());
        boolean result = true;
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            TodoDetailKeybox keybox = (TodoDetailKeybox) workflowTodoDetailVO.getDetail();
            KeyboxUserServerVO keyboxVO = new KeyboxUserServerVO(userDO.getUsername(), keybox);
            BusinessWrapper<Boolean> businessWrapper = keyBoxService.saveUserGroup(keyboxVO);
            if (businessWrapper.isSuccess()) {
                workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_COMPLETE);
            } else {
                workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_ERR);
                result = false;
            }
            saveTodoDetail(workflowTodoDetailVO);
        }
        return result;
    }

    @Override
    protected void createTodoDetails(long todoId, List<WorkflowTodoDetailVO> detailList) {
    }


}
