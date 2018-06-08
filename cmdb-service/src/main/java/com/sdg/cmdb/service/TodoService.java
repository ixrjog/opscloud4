package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.todo.*;
import com.sdg.cmdb.domain.todo.todoProperty.StashProjectDO;
import com.sdg.cmdb.domain.todo.todoProperty.StashRepositoryDO;

import java.util.List;

/**
 * Created by zxxiao on 16/10/9.
 */
public interface TodoService {

    /**
     * 新增 or 更新配置项
     *
     * @param todoConfigDO
     * @return
     */
    BusinessWrapper<Boolean> saveTodoConfig(TodoConfigDO todoConfigDO);

    /**
     * 删除指定id的配置项
     *
     * @param itemId
     * @return
     */
    BusinessWrapper<Boolean> delTodoConfig(long itemId);

    /**
     * 查询合适条件下的工单配置项分页数据
     *
     * @param queryName
     * @param parent
     * @param valid
     * @param page
     * @param length
     * @return
     */
    TableVO<List<TodoConfigVO>> queryConfigPage(String queryName, long parent, int valid, int page, int length);

    /**
     * 查询指定配置项的子项目
     *
     * @param parentId
     * @return
     */
    List<TodoConfigDO> queryChildrenById(long parentId);

    /**
     * 查询工单分页数据
     *
     * @param queryDO
     * @param page
     * @param length
     * @return
     */
    TableVO<List<TodoDailyVO>> queryTodoPage(TodoDailyQueryDO queryDO, int page, int length);

    /**
     * 保存 or 更新日常工单
     *
     * @param dailyDO
     * @return
     */
    BusinessWrapper<Boolean> saveTodoItem(TodoDailyDO dailyDO);

    /**
     * 查询工单待处理分页数据
     *
     * @param queryDO
     * @param page
     * @param length
     * @return
     */
    TableVO<List<TodoDailyVO>> queryTodoProcessPage(TodoDailyQueryDO queryDO, int page, int length);

    /**
     * 查询指定工单的数据
     *
     * @param id
     * @return
     */
    TodoDailyVO queryTodoById(long id);

    List<TodoGroupVO> queryTodoGroup();

    TodoDetailVO queryTodoDetail(long id);

    /**
     * 创建工单
     *
     * @param todoId
     * @return
     */
    TodoDetailVO establishTodo(long todoId);

    /**
     * 提交工单（配置完成）
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> submitTodoDetail(long id);

    BusinessWrapper<Boolean> saveKeyboxDetail(TodoKeyboxDetailDO todoKeyboxDetailDO);

    BusinessWrapper<Boolean> delKeyboxDetail(long todoKeyboxDetailId);

    BusinessWrapper<Boolean> saveCiUserGroupDetail(TodoCiUserGroupDetailDO ciUserGroupDetailDO);

    BusinessWrapper<Boolean> delCiUserGroupDetail(long todoCiUserGroupDetailId);

    BusinessWrapper<Boolean> setTodoSystemAuth(long todoSystemAuthDetailId);

    BusinessWrapper<Boolean> setTodoVpn(long todoVpnDetailId);

    /**
     * 查询我的待办工单
     *
     * @return
     */
    List<TodoDetailVO> queryMyTodoJob();

    /**
     * 撤销工单
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> revokeTodoDetail(long id);

    /**
     * 处理工单
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> processingTodoDetail(long id);

    /**
     * 查询我完成的工单
     *
     * @return
     */
    List<TodoDetailVO> queryCompleteTodoJob();


    /**
     * 保存工单
     * @param todoDetailVO
     * @return
     */
    TodoDetailVO saveTodoDetail(TodoDetailVO todoDetailVO);

    TodoBuilderPlanDetailDO saveBuilderPlan(TodoBuilderPlanDetailDO todoBuildrPlanDetailDO);

    //TodoBuilderPlanDetailDO updateBuilderPlan(TodoBuilderPlanDetailDO todoBuildrPlanDetailDO);

    BusinessWrapper<Boolean> delBuilderPlan(long id);

    TableVO<List<TodoBuilderPlanDetailDO>> queryBuilderPlan(long todoDetailId);

    TableVO<List<TodoNewServerDetailDO>> queryNewServer(long todoDetailId);

    BusinessWrapper<Boolean> delNewServer(long id);

    TodoNewServerDetailDO saveNewServer(TodoNewServerDetailDO todoNewServerDetailDO);

    BusinessWrapper<Boolean>  checkProjectName(String projectName);

    /**
     * 处理新项目申请工单
     * @param todoDetailId
     * @param type
     * @return
     */
    BusinessWrapper<Boolean> procNewProject(long todoDetailId,int type);


    TodoTomcatVersionVO getTomcatVersion(long serverGroupId);


    /** 自动更新服务器Tomcat版本任务
     *
     * @param todoDetailId
     * @param serverId
     * @return
     */
  //  BusinessWrapper<Boolean>  taskUpdateTomcat(long todoDetailId,long serverId);

    /**
     * 回滚版本
     * @param todoDetailId
     * @param serverId
     * @return
     */
  //  BusinessWrapper<Boolean>  taskRollbackTomcat(long todoDetailId,long serverId);

}
