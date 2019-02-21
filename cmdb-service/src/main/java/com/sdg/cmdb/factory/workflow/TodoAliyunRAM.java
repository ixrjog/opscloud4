package com.sdg.cmdb.factory.workflow;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailRAMGroup;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailDO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailVO;
import com.sdg.cmdb.service.AliyunRAMService;
import com.sdg.cmdb.util.SessionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class TodoAliyunRAM extends TodoAbs implements Serializable {
    private static final long serialVersionUID = 6918204012961846263L;

    private final String TODO_KEY = "RAM";

    @Resource
    private AliyunRAMService aliyunRAMService;

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
        //HashMap<String, WorkflowTodoDetailDO> map = new HashMap<>();
        String notice = "RAM权限组：";
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            WorkflowTodoDetailDO workflowTodoDetailDO = getTodoDetailDO(workflowTodoDetailVO);
            //TodoDetailRAMGroup ramGroupDetail = (TodoDetailRAMGroup) getTodoDetailVO(workflowTodoDetailDO).getDetail();
            //workflowTodoDetailDO.setName(ramGroupDetail .getServerGroupDO().getName());
            if (!saveTodoDetail(workflowTodoDetailDO)) return workflowTodoVO;
            // TODO 插入工作流详情
            Gson gson = new GsonBuilder().create();
            TodoDetailRAMGroup ramGroupDetail = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), TodoDetailRAMGroup.class);
            if (ramGroupDetail.isApply())
                notice += workflowTodoDetailVO.getName() + ";";
        }
        workflowTodoVO.setNotice(notice);
        saveTodoUser(workflowTodoVO);
        updateTodo(workflowTodoVO);
        return getTodo(workflowTodoVO.getId());
    }

    /**
     * 封装对象
     *
     * @param todoDetailDO
     * @return
     */
    protected WorkflowTodoDetailVO getTodoDetailVO(WorkflowTodoDetailDO todoDetailDO) {
        // TODO 对象转json
        Gson gson = new GsonBuilder().create();
        TodoDetailRAMGroup ramGroupDetail = gson.fromJson(todoDetailDO.getDetailValue(), TodoDetailRAMGroup.class);
        return new WorkflowTodoDetailVO(ramGroupDetail, todoDetailDO);
    }


    @Override
    protected Type getType() {
        return TodoDetailRAMGroup.class;
    }


    private WorkflowTodoDetailDO getTodoDetailDO(WorkflowTodoDetailVO workflowTodoDetailVO) {
        Gson gson = new GsonBuilder().create();
        TodoDetailRAMGroup ramGroupDetail = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), getType());
        // TodoDetailKeybox keyboxDetail = (TodoDetailKeybox) workflowTodoDetailVO.getDetail();
        WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(workflowTodoDetailVO, ramGroupDetail.toString());
        return workflowTodoDetailDO;
    }

    @Override
    protected void createTodoDetails(long todoId, List<WorkflowTodoDetailVO> detailList) {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        List<TodoDetailRAMGroup> ramGroups = aliyunRAMService.getUserGroupInfo(userDO);
        for (TodoDetailRAMGroup ramGroup : ramGroups) {
            WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(todoId, ramGroup.getGroupName(), getKey());
            WorkflowTodoDetailVO workflowTodoDetailVO = new WorkflowTodoDetailVO(ramGroup, workflowTodoDetailDO);
            detailList.add(workflowTodoDetailVO);
        }
    }

    @Override
    protected boolean invokeTodoDetails(WorkflowTodoVO todoVO) {
        List<WorkflowTodoDetailVO> todoDetails = todoVO.getTodoDetails();
        UserDO userDO = userDao.getUserById(todoVO.getApplyUserId());
        boolean result = true;
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            TodoDetailRAMGroup ramGroup = (TodoDetailRAMGroup) workflowTodoDetailVO.getDetail();
            // TODO 判断是否申请
            if (!ramGroup.isApply())
                continue;
            if (aliyunRAMService.addUserToGroup(userDO, ramGroup.getGroupName())) {
                workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_COMPLETE);
            } else {
                workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_ERR);
                result = false;
            }
            saveTodoDetail(workflowTodoDetailVO);
        }
        return result;
    }

}
