package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.todo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/10/9.
 */
@Component
public interface TodoDao {

    /**
     * 新增类目
     *
     * @param configDO
     * @return
     */
    int addTodoConfigItem(TodoConfigDO configDO);

    /**
     * 更新类目
     *
     * @param configDO
     * @return
     */
    int updateTodoConfigById(TodoConfigDO configDO);

    /**
     * 删除类目
     *
     * @param id
     * @return
     */
    int delTodoConfigById(@Param("id") long id);

    /**
     * 查询合适条件下的类目数目
     *
     * @param valid
     * @param queryName
     * @param parent
     * @return
     */
    long queryConfigItemSize(@Param("valid") int valid, @Param("queryName") String queryName, @Param("parent") long parent);

    /**
     * 查询合适条件下的类目分页数据
     *
     * @param valid
     * @param queryName
     * @param parent
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<TodoConfigDO> queryConfigItemPage(
            @Param("valid") int valid, @Param("queryName") String queryName, @Param("parent") long parent,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 查询指定id的类目信息
     *
     * @param id
     * @return
     */
    TodoConfigDO queryConfigItemById(@Param("id") long id);

    /**
     * 获取指定parentId的数目
     *
     * @param id
     * @return
     */
    long queryChildrenSizeByParentId(@Param("id") long id);

    /**
     * 查询指定id的子类目
     *
     * @param id
     * @return
     */
    List<TodoConfigDO> queryChildrenListById(@Param("id") long id);

    /**
     * 新建工单
     *
     * @param dailyDO
     * @return
     */
    int addTodoItem(TodoDailyDO dailyDO);

    /**
     * 更新工单
     *
     * @param dailyDO
     * @return
     */
    int updateTodoItem(TodoDailyDO dailyDO);

    /**
     * 查询工单数目
     *
     * @param queryDO
     * @return
     */
    long queryTodoItemSize(TodoDailyQueryDO queryDO);

    /**
     * 查询工单分页数据
     *
     * @param queryDO
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<TodoDailyDO> queryTodoItemPage(
            @Param("queryDO") TodoDailyQueryDO queryDO,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 新增日常工单日志
     *
     * @param dailyLogDO
     * @return
     */
    int addTodoDailyLog(TodoDailyLogDO dailyLogDO);

    /**
     * 查询日常订单日志
     *
     * @param dailyId
     * @return
     */
    List<TodoDailyLogDO> queryTodoDailyLogByDailyId(@Param("dailyId") long dailyId);

    /**
     * 查询指定id的工单信息
     *
     * @param dailyId
     * @return
     */
    TodoDailyDO queryTodoById(@Param("dailyId") long dailyId);

    /**
     * 查询满足合适规则的待处理工单集合数目
     *
     * @param queryDO
     * @param levelOneList
     * @return
     */
    long queryProcessTodoDailySize(
            @Param("queryDO") TodoDailyQueryDO queryDO, @Param("list") List<Long> levelOneList);

    /**
     * 查询满足合适规则的待处理工单集合分页数据
     *
     * @param queryDO
     * @param levelOneList
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<TodoDailyDO> queryProcessTodoDailyPage(
            @Param("queryDO") TodoDailyQueryDO queryDO, @Param("list") List<Long> levelOneList,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 查询满足指定角色集合的配置项id集合
     *
     * @param roleIdList
     * @return
     */
    List<Long> queryTodoConfigListByRoles(@Param("list") List<Long> roleIdList);

    /////////////////

    /**
     * 查询所有的工单组
     *
     * @return
     */
    List<TodoGroupDO> queryTodoGroup();

    /**
     * 查询所有的工单
     *
     * @return
     */
    List<TodoDO> queryTodo();

    /**
     * 查询工单
     *
     * @return
     */
    TodoDO queryTodoInfoById(@Param("id") long id);

    /**
     * 查询所有的工单
     *
     * @return
     */
    List<TodoDO> queryTodoByGroupId(@Param("todoGroupId") long todoGroupId);

    List<TodoDetailDO> queryTodoDetail(@Param("initiatorUserId") long initiatorUserId, @Param("todoId") long todoId, @Param("todoStatus") long todoStatus);

    TodoDetailDO queryTodoDetailById(@Param("id") long id);

    int updateTodoDetail(TodoDetailDO todoDetailDO);

    int addTodoDetail(TodoDetailDO todoDetailDO);

    int delTodoDetail(TodoDetailDO todoDetailDO);

    TodoKeyboxDetailDO queryTodoKeyboxDetail(@Param("todoDetailId") long todoDetailId, @Param("serverGroupId") long serverGroupId);

    List<TodoKeyboxDetailDO> queryTodoKeyboxDetailByTodoDetailId(@Param("todoDetailId") long todoDetailId);

    int addTodoKeyboxDetail(TodoKeyboxDetailDO todoKeyboxDetailDO);

    int delTodoKeyboxDetail(@Param("id") long id);

    List<TodoCiUserGroupDetailDO> queryTodoCiUserGroupDetailByTodoDetailId(@Param("todoDetailId") long todoDetailId);

    int addTodoCiUserGroupDetail(TodoCiUserGroupDetailDO todoCiUserGroupDetailDO);

    int delTodoCiUserGroupDetail(@Param("id") long id);

    List<TodoSystemAuthDetailDO> queryTodoSystemAuthDetailByTodoDetailId(@Param("todoDetailId") long todoDetailId);

    int addTodoSystemAuthDetail(TodoSystemAuthDetailDO todoSystemAuthDetailDO);

    int updateTodoSystemAuthDetail(TodoSystemAuthDetailDO todoSystemAuthDetailDO);

    TodoSystemAuthDetailDO queryTodoSystemAuthDetailById(@Param("id") long id);

    /**
     * 查询本人发起的工单
     *
     * @param initiatorUsername
     * @return
     */
    List<TodoDetailDO> queryMyTodoJob(@Param("initiatorUsername") String initiatorUsername);

    /**
     * 查询本人完结的工单
     *
     * @param initiatorUsername
     * @return
     */
    List<TodoDetailDO> queryCompleteTodoJob(@Param("initiatorUsername") String initiatorUsername);

    /**
     * 查询所有未完成的工单
     *
     * @return
     */
    List<TodoDetailDO> queryAllTodoJob();

    List<TodoDetailDO> queryAllCompleteTodoJob();

    /**
     * 更新 处理状态processStatus
     *
     * @param todoKeyboxDetailDO
     * @return
     */
    int updateTodoKeyboxDetail(TodoKeyboxDetailDO todoKeyboxDetailDO);

    /**
     * 更新 处理状态processStatus
     *
     * @param todoCiUserGroupDetailDO
     * @return
     */
    int updateTodoCiUserGroupDetail(TodoCiUserGroupDetailDO todoCiUserGroupDetailDO);


    int addTodoVpnDetail(TodoVpnDetailDO todoVpnDetailDO);

    int updateTodoVpnDetail(TodoVpnDetailDO todoVpnDetailDO);

    List<TodoVpnDetailDO> queryTodoVpnDetailByTodoDetailId(@Param("todoDetailId") long todoDetailId);

    TodoVpnDetailDO queryTodoVpnDetailById(@Param("id") long id);

    List<TodoPropertyDO> queryTodoPropertyByTodoDetailId(@Param("todoDetailId") long todoDetailId);

    List<TodoPropertyDO> queryTodoPropertyByTodoDetailIdAndTodoKey(@Param("todoDetailId") long todoDetailId,@Param("todoKey") String todoKey);

    int addTodoProperty(TodoPropertyDO todoPropertyDO);

    int delTodoPropertyById(@Param("id") long id);

    int addTodoBuilderPlanDetail(TodoBuilderPlanDetailDO todoBuilderPlanDetailDO);

    int delTodoBuilderPlanDetail(@Param("id") long id);

    int updateTodoBuilderPlanDetail(TodoBuilderPlanDetailDO todoBuilderPlanDetailDO);

    List<TodoBuilderPlanDetailDO> queryTodoBuilderPlanDetailByTodoDetailId(@Param("todoDetailId") long todoDetailId);

    TodoBuilderPlanDetailDO queryTodoBuilderPlanDetailByTodoDetailIdAndEnvType(@Param("todoDetailId") long todoDetailId,@Param("envType") long envType);

    int addTodoNewServerDetail(TodoNewServerDetailDO todoNewServerDetailDO);

    int delTodoNewServerDetail(@Param("id") long id);

    int updateTodoNewServerDetail(TodoNewServerDetailDO todoNewServerDetailDO);

    List<TodoNewServerDetailDO> queryTodoNewServerDetailByTodoDetailId(@Param("todoDetailId") long todoDetailId);

    TodoNewServerDetailDO queryTodoNewServerDetailByTodoDetailIdAndEnvType(@Param("todoDetailId") long todoDetailId,@Param("envType") long envType);
}
