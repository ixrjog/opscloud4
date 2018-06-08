package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.dao.cmdb.*;
import com.sdg.cmdb.dao.stash.StashDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.CiUserGroupDO;
import com.sdg.cmdb.domain.auth.CiUserGroupVO;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerDO;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerVO;
import com.sdg.cmdb.domain.logCleanup.LogCleanupPropertyDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.todo.*;
import com.sdg.cmdb.domain.todo.todoProperty.StashProjectDO;
import com.sdg.cmdb.domain.todo.todoProperty.StashRepositoryDO;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.TimeViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zxxiao on 16/10/9.
 */
@Service
public class TodoServiceImpl implements TodoService {

    private static final Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);

    public static final int NEWPROJECT_CREATE_SERVERGROUP = 0;


    @Resource
    private TodoDao todoDao;

    @Resource
    private UserDao userDao;

    @Resource
    private AuthDao authDao;

    @Resource
    private AuthService authService;

    @Resource
    private EmailService emailService;

    @Resource
    private KeyBoxService keyBoxService;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ConfigService configService;

    @Resource
    private ServerDao serverDao;

    @Resource
    private CiUserGroupService ciUserGroupService;

    @Resource
    private TodoPropertyService todoPropertyService;

    @Resource
    private ConfigServerGroupService configServerGroupService;

    @Resource
    private AnsibleTaskService ansibleTaskService;


    @Resource
    private CiDao ciDao;

    @Override
    public BusinessWrapper<Boolean> saveTodoConfig(TodoConfigDO todoConfigDO) {
        try {
            if (todoConfigDO.getId() == 0) {
                todoDao.addTodoConfigItem(todoConfigDO);
            } else {
                todoDao.updateTodoConfigById(todoConfigDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delTodoConfig(long itemId) {
        try {
            /**
             * 1.如果是父类目,则不能有子类目才允许逻辑删除
             * 2.父子类目必须无正在使用的工单才允许逻辑删除
             */
            TodoConfigDO todoConfigDO = todoDao.queryConfigItemById(itemId);
            if (todoConfigDO == null) {
                return new BusinessWrapper<>(true);
            }

            if (todoConfigDO.getParentId() == 0) {  //父类目,检查是否有子类目
                long children = todoDao.queryChildrenSizeByParentId(itemId);
                if (children == 0) {
                    todoDao.delTodoConfigById(itemId);
                } else {
                    return new BusinessWrapper<>(ErrorCode.configHasChildren);
                }
            }

            todoDao.delTodoConfigById(itemId);

            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public TableVO<List<TodoConfigVO>> queryConfigPage(String queryName, long parent, int valid, int page, int length) {
        long size = todoDao.queryConfigItemSize(valid, queryName, parent);
        List<TodoConfigDO> list = todoDao.queryConfigItemPage(valid, queryName, parent, page * length, length);

        List<TodoConfigVO> configVOList = new ArrayList<>();
        for (TodoConfigDO configDO : list) {
            TodoConfigDO parentConfigDO = todoDao.queryConfigItemById(configDO.getParentId());
            RoleDO roleDO = authService.getRoleById(configDO.getRoleId());
            TodoConfigVO configVO = new TodoConfigVO(configDO, parentConfigDO, roleDO);

            configVOList.add(configVO);
        }

        return new TableVO<>(size, configVOList);
    }

    @Override
    public List<TodoConfigDO> queryChildrenById(long parentId) {
        return todoDao.queryChildrenListById(parentId);
    }

    @Override
    public BusinessWrapper<Boolean> saveTodoItem(TodoDailyDO dailyDO) {
        try {
            UserDO sponsorUser = authService.getUserByName(dailyDO.getSponsor());

            TodoConfigDO levelOne = todoDao.queryConfigItemById(dailyDO.getLevelOne());
            List<UserDO> userDOList = authService.getUsersByRole(levelOne.getRoleId());
            if (dailyDO.getId() == 0) {
                todoDao.addTodoItem(dailyDO);
                TodoDailyVO dailyVO = queryTodoById(dailyDO.getId());
                for (UserDO userDO : userDOList) {
                    //不需要给自己发
                    if (userDO.getUsername().equals(SessionUtils.getUsername())) {
                        continue;
                    }
                    boolean sendResult = emailService.doSendNewTodo(userDO, dailyVO, sponsorUser);
                    if (!sendResult) {
                        logger.warn("send email notify to: " + userDO.getDisplayName() + " fail!");
                    }
                }
            } else {
                TodoDailyDO tmpDailyDO = todoDao.queryTodoById(dailyDO.getId());
                if (tmpDailyDO != null) {
                    userDOList.add(authService.getUserByName(tmpDailyDO.getSponsor()));    //通知人加上发起人
                }
                todoDao.updateTodoItem(dailyDO);
                TodoDailyVO dailyVO = queryTodoById(dailyDO.getId());

                for (UserDO userDO : userDOList) {
                    //不需要给自己发
                    if (userDO.getUsername().equals(SessionUtils.getUsername())) {
                        continue;
                    }
                    boolean sendResult = false;
                    // 完成工单通知邮件
                    if (dailyDO.getTodoStatus() == TodoDailyDO.TodoStatusEnum.finishStatus.getCode()) {
                        if (userDO.getUsername().equals(tmpDailyDO.getSponsor())) {
                            sendResult = emailService.doFinishTodo(userDO, dailyVO, sponsorUser);
                        }
                    } else if (dailyDO.getTodoStatus() == TodoDailyDO.TodoStatusEnum.processStatus.getCode()) {
                        if (tmpDailyDO.getTodoStatus() == TodoDailyDO.TodoStatusEnum.processStatus.getCode()) {
                            sendResult = emailService.doSendUpdateTodo(userDO, dailyVO, sponsorUser);
                        } else if (tmpDailyDO.getTodoStatus() == TodoDailyDO.TodoStatusEnum.feedbackStatus.getCode()) {
                            sendResult = emailService.doSendInitFeedbackTodo(userDO, dailyVO, sponsorUser);
                        }
                    } else if (dailyDO.getTodoStatus() == TodoDailyDO.TodoStatusEnum.feedbackStatus.getCode()) {
                        if (userDO.getUsername().equals(tmpDailyDO.getSponsor())) {
                            sendResult = emailService.doSendAcceptFeedbackTodo(userDO, dailyVO, sponsorUser);
                        }
                    }

                    if (!sendResult) {
                        logger.warn("send email notify to: " + userDO.getDisplayName() + " fail!");
                    }
                }
            }

            TodoDailyLogDO logDO = TodoDailyLogDO.buildDailyLogDO(dailyDO, SessionUtils.getUsername());

            //当工单状态为待处理时,日志待反馈内容重置
            if (dailyDO.getTodoStatus() == TodoDailyLogDO.statusProcess) {
                logDO.setDailyFeedbackContent("");
            }
            todoDao.addTodoDailyLog(logDO);

            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public TableVO<List<TodoDailyVO>> queryTodoProcessPage(TodoDailyQueryDO queryDO, int page, int length) {
        List<Long> roleIdList = authService.getUserRoleIds(SessionUtils.getUsername());
        if (roleIdList.isEmpty()) {
            return new TableVO<>(0, Collections.EMPTY_LIST);
        }

        List<Long> configIdList = todoDao.queryTodoConfigListByRoles(roleIdList);
        if (configIdList.isEmpty()) {
            return new TableVO<>(0, Collections.EMPTY_LIST);
        }

        long size = todoDao.queryProcessTodoDailySize(queryDO, configIdList);
        List<TodoDailyDO> dailyDOList = todoDao.queryProcessTodoDailyPage(queryDO, configIdList, page * length, length);
        return new TableVO<>(size, acqTodoDailyVO(dailyDOList));
    }

    private List<TodoDailyVO> acqTodoDailyVO(List<TodoDailyDO> dailyDOList) {
        List<TodoDailyVO> dailyVOList = new ArrayList<>();
        for (TodoDailyDO dailyDO : dailyDOList) {
            TodoConfigDO levelOne = todoDao.queryConfigItemById(dailyDO.getLevelOne());
            TodoConfigDO levelTwo = todoDao.queryConfigItemById(dailyDO.getLevelTwo());
            TodoDailyVO dailyVO = new TodoDailyVO(dailyDO, levelOne, levelTwo);
            dailyVO.setDailyLogDOList(todoDao.queryTodoDailyLogByDailyId(dailyDO.getId()));
            dailyVOList.add(dailyVO);
        }
        return dailyVOList;
    }

    @Override
    public TableVO<List<TodoDailyVO>> queryTodoPage(TodoDailyQueryDO queryDO, int page, int length) {
        long size = todoDao.queryTodoItemSize(queryDO);
        List<TodoDailyDO> dailyDOList = todoDao.queryTodoItemPage(queryDO, page * length, length);
        return new TableVO<>(size, acqTodoDailyVO(dailyDOList));
    }


    @Override
    public TodoDailyVO queryTodoById(long id) {
        TodoDailyDO dailyDO = todoDao.queryTodoById(id);

        TodoConfigDO levelOne = todoDao.queryConfigItemById(dailyDO.getLevelOne());
        TodoConfigDO levelTwo = todoDao.queryConfigItemById(dailyDO.getLevelTwo());

        TodoDailyVO dailyVO = new TodoDailyVO(dailyDO, levelOne, levelTwo);

        dailyVO.setDailyLogDOList(todoDao.queryTodoDailyLogByDailyId(dailyDO.getId()));

        return dailyVO;
    }

    @Override
    public List<TodoGroupVO> queryTodoGroup() {
        List<TodoGroupDO> todoGroupDOList = todoDao.queryTodoGroup();
        List<TodoGroupVO> todoGroupVOList = new ArrayList<>();
        for (TodoGroupDO todoGroupDO : todoGroupDOList) {
            List<TodoDO> todoDOList = todoDao.queryTodoByGroupId(todoGroupDO.getId());
            TodoGroupVO todoGroupVO = new TodoGroupVO(todoGroupDO, todoDOList);
            todoGroupVOList.add(todoGroupVO);
        }
        return todoGroupVOList;
    }

    @Override
    public TodoDetailVO queryTodoDetail(long id) {
        TodoDetailDO todoDetailDO = todoDao.queryTodoDetailById(id);
        TodoDetailVO todoDetailVO = new TodoDetailVO(todoDetailDO);
        invokeTodoDetailVO(todoDetailVO);
        return todoDetailVO;
    }

    /**
     * 创建工单
     *
     * @param todoId
     * @return
     */
    @Override
    public TodoDetailVO establishTodo(long todoId) {
        String username = SessionUtils.getUsername();
        if (StringUtils.isEmpty(username)) new BusinessWrapper<>(ErrorCode.userExist);
        UserDO userDO = userDao.getUserByName(username);
        if (userDO == null) new BusinessWrapper<>(ErrorCode.userExist);
        TodoDetailDO todoDetailDO = acqNewTodo(userDO, todoId);

        TodoDetailVO todoDetailVO = new TodoDetailVO(todoDetailDO);
        invokeTodoDetailVO(todoDetailVO);
        return todoDetailVO;
    }

    /**
     * 获取一个新的工单，如果有新建未填写的工单则复用
     *
     * @param userDO
     * @param todoId
     * @return
     */
    private TodoDetailDO acqNewTodo(UserDO userDO, long todoId) {
        // 查询是否有新建未填写的工单
        List<TodoDetailDO> todoDetailList = todoDao.queryTodoDetail(userDO.getId(), todoId, TodoDetailDO.TodoStatusEnum.establish.getCode());
        if (todoDetailList.size() > 1) {
            for (TodoDetailDO todoDetailDO : todoDetailList)
                todoDao.delTodoDetail(todoDetailDO);
        }
        if (todoDetailList.size() == 1) return todoDetailList.get(0);
        TodoDetailDO todoDetailDO = new TodoDetailDO(userDO, todoId);
        try {
            todoDao.addTodoDetail(todoDetailDO);
        } catch (Exception e) {
            new BusinessWrapper<>(ErrorCode.serverFailure);
        }
        return todoDetailDO;
    }

    @Override
    public BusinessWrapper<Boolean> saveKeyboxDetail(TodoKeyboxDetailDO todoKeyboxDetailDO) {
        String username = SessionUtils.getUsername();
        // 已授权
        KeyboxUserServerDO userServerDO = new KeyboxUserServerDO(username, todoKeyboxDetailDO.getServerGroupId());
        long size = keyBoxService.getUserServerSize(userServerDO);
        if (size > 0) return new BusinessWrapper<>(ErrorCode.serverGroupAuthed);
        // 重复
        TodoKeyboxDetailDO keyboxDO = todoDao.queryTodoKeyboxDetail(todoKeyboxDetailDO.getTodoDetailId(), todoKeyboxDetailDO.getServerGroupId());
        if (keyboxDO != null) return new BusinessWrapper<>(ErrorCode.serverGroupRepeat);
        //// TODO 前置检查完成，开始添加
        if (todoKeyboxDetailDO.isCiAuth()) {
            CiUserGroupDO ciUserGroupDO = userDao.getCiUserGroupByServerGroupIdAndEnvType(todoKeyboxDetailDO.getServerGroupId(), CiUserGroupVO.EnvTypeEnum.all.getCode());
            if (ciUserGroupDO != null) {
                todoKeyboxDetailDO.setCiUserGroupId(ciUserGroupDO.getId());
                todoKeyboxDetailDO.setCiUserGroupName(ciUserGroupDO.getGroupName());
            } else {
                // 标记为0则没找到数据
                todoKeyboxDetailDO.setCiUserGroupId(0l);
            }
        }
        try {
            todoDao.addTodoKeyboxDetail(todoKeyboxDetailDO);
        } catch (Exception e) {
            return new BusinessWrapper<>(false);
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> saveCiUserGroupDetail(TodoCiUserGroupDetailDO ciUserGroupDetailDO) {
        CiUserGroupDO ciUserGroupDO = userDao.getCiUserGroupById(ciUserGroupDetailDO.getCiUserGroupId());
        if (ciUserGroupDO == null) return new BusinessWrapper<Boolean>(false);

        ciUserGroupDetailDO.setEnvType(ciUserGroupDO.getEnvType());
        ciUserGroupDetailDO.setCiUserGroupName(ciUserGroupDO.getGroupName());
        ciUserGroupDetailDO.setServerGroupId(ciUserGroupDO.getServerGroupId());
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(ciUserGroupDO.getServerGroupId());
        // 只有持续集成组没有服务器组
        if (serverGroupDO != null)
            ciUserGroupDetailDO.setServerGroupName(serverGroupDO.getName());
        try {
            todoDao.addTodoCiUserGroupDetail(ciUserGroupDetailDO);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> submitTodoDetail(long id) {
        TodoDetailDO todoDetailDO = todoDao.queryTodoDetailById(id);
        if (todoDetailDO.getTodoStatus() != TodoDetailDO.TodoStatusEnum.establish.getCode())
            return new BusinessWrapper<>(ErrorCode.todoStatusIsNotEstablish);
        todoDetailDO.setTodoStatus(TodoDetailDO.TodoStatusEnum.submit.getCode());
        try {
            todoDao.updateTodoDetail(todoDetailDO);
            sendSubmitTodo(todoDetailDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }


    /**
     * //// TODO 插入相关数据（工单配置+工单详情)
     *
     * @param todoDetailDO
     */
    private void sendSubmitTodo(TodoDetailDO todoDetailDO) {
        TodoDetailVO todoDetailVO = new TodoDetailVO(todoDetailDO);
        invokeTodoDetailVO(todoDetailVO);
        emailService.doSendSubmitTodo(todoDetailVO);
    }


    @Override
    public BusinessWrapper<Boolean> delKeyboxDetail(long todoKeyboxDetailId) {
        try {
            todoDao.delTodoKeyboxDetail(todoKeyboxDetailId);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delCiUserGroupDetail(long todoCiUserGroupDetailId) {
        try {
            todoDao.delTodoCiUserGroupDetail(todoCiUserGroupDetailId);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }


    @Override
    public BusinessWrapper<Boolean> setTodoSystemAuth(long todoSystemAuthDetailId) {
        TodoSystemAuthDetailDO todoDO = todoDao.queryTodoSystemAuthDetailById(todoSystemAuthDetailId);
        todoDO.setNeed(!todoDO.isNeed());
        try {
            todoDao.updateTodoSystemAuthDetail(todoDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> setTodoVpn(long todoVpnDetailId) {
        TodoVpnDetailDO todoDO = todoDao.queryTodoVpnDetailById(todoVpnDetailId);
        todoDO.setNeed(!todoDO.isNeed());
        try {
            todoDao.updateTodoVpnDetail(todoDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }


    /**
     * 查询符合条件的（工单）负责人
     */
    private void invokeAssigneeUsers(TodoDetailVO todoDetailVO) {
        TodoDO todoDO = todoDao.queryTodoInfoById(todoDetailVO.getTodoId());
        // 0 = devops
        String roleName = TodoDO.TodoTypeEnum.getTodoTypeName(todoDO.getTodoType());
        List<UserDO> users = authService.queryUsersByRoleName(roleName);
        todoDetailVO.setAssigneeUsers(users);
    }


    public List<TodoDetailVO> queryMyTodoJob(String username) {
        //HashMap<Long, TodoDetailVO> map = new HashMap<>();
        //// TODO 判断是否为 devops
        List<UserDO> users = authService.queryUsersByRoleName(TodoDO.TodoTypeEnum.devops.getDesc());
        boolean isDevops = false;
        List<TodoDetailDO> todoDetailList = new ArrayList<>();
        for (UserDO userDO : users) {
            if (userDO.getUsername().equals(username)) {
                todoDetailList = todoDao.queryAllTodoJob();
                isDevops = true;
                break;
            }
        }
        //// TODO 查询自己的工单
        if (!isDevops) todoDetailList = todoDao.queryMyTodoJob(username);
        return acqTodoJob(todoDetailList);
    }

    /**
     * 查询我需要处理的工单
     *
     * @return
     */
    @Override
    public List<TodoDetailVO> queryMyTodoJob() {
        String username = SessionUtils.getUsername();
        return queryMyTodoJob(username);
    }


    /**
     * 撤销工单
     *
     * @param id
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> revokeTodoDetail(long id) {
        TodoDetailDO todoDetailDO = todoDao.queryTodoDetailById(id);
        if (todoDetailDO == null) return new BusinessWrapper<Boolean>(false);
        todoDetailDO.setTodoStatus(TodoDetailDO.TodoStatusEnum.revoke.getCode());
        try {
            todoDao.updateTodoDetail(todoDetailDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }


    @Override
    public BusinessWrapper<Boolean> processingTodoDetail(long id) {
        TodoDetailDO todoDetailDO = todoDao.queryTodoDetailById(id);
        // 按工单类型处理工单
        processingTodoDetailByTodoId(todoDetailDO.getTodoId(), todoDetailDO);
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        // 插入负责人
        todoDetailDO.setAssigneeUserId(userDO.getId());
        todoDetailDO.setAssigneeUsername(userDO.getUsername());
        todoDetailDO.setTodoStatus(TodoDetailDO.TodoStatusEnum.complete.getCode());
        //System.err.println("id" + id);
        //System.err.println("TodoStatus" + todoDetailDO.getTodoStatus());
        todoDao.updateTodoDetail(todoDetailDO);

        TodoDetailVO todoDetailVO = new TodoDetailVO(todoDetailDO);
        invokeTodoDetailVO(todoDetailVO);
        emailService.doSendCompleteTodo(todoDetailVO);
        return new BusinessWrapper<Boolean>(true);
    }

    /**
     * 处理工单
     *
     * @param todoId
     * @param todoDetailDO
     * @return
     */
    private boolean processingTodoDetailByTodoId(long todoId, TodoDetailDO todoDetailDO) {
        UserDO userDO = userDao.getUserById(todoDetailDO.getInitiatorUserId());
        int id = (int) todoId;
        switch (id) {
            // 堡垒机权限申请工单
            case 1:
                List<TodoKeyboxDetailDO> todoKeyboxDetailList = todoDao.queryTodoKeyboxDetailByTodoDetailId(todoDetailDO.getId());
                for (TodoKeyboxDetailDO todoItem : todoKeyboxDetailList) {
                    KeyboxUserServerVO keyboxVO = new KeyboxUserServerVO(todoDetailDO.getInitiatorUsername(), todoItem);
                    BusinessWrapper<Boolean> businessWrapper = keyBoxService.saveUserGroup(keyboxVO);
                    if (businessWrapper.isSuccess()) {
                        todoItem.setProcessStatus(TodoKeyboxDetailDO.ProcessStatusEnum.complete.getCode());
                    } else {
                        todoItem.setProcessStatus(TodoKeyboxDetailDO.ProcessStatusEnum.err.getCode());
                    }
                    todoDao.updateTodoKeyboxDetail(todoItem);
                }
                // 创建用户堡垒机配置文件
                keyBoxService.createUserGroupConfigFile(userDO.getUsername());
                return true;
            // 持续集成权限申请工单
            case 2:
                List<TodoCiUserGroupDetailDO> todoCiUserGroupDetailList = todoDao.queryTodoCiUserGroupDetailByTodoDetailId(todoDetailDO.getId());
                for (TodoCiUserGroupDetailDO todoItem : todoCiUserGroupDetailList) {
                    BusinessWrapper<Boolean> businessWrapper = ciUserGroupService.userAddGroup(todoDetailDO.getInitiatorUserId(), todoItem.getCiUserGroupId());
                    if (businessWrapper.isSuccess()) {
                        todoItem.setProcessStatus(TodoDetailDO.ProcessStatusEnum.complete.getCode());
                    } else {
                        todoItem.setProcessStatus(TodoDetailDO.ProcessStatusEnum.err.getCode());
                    }
                    todoDao.updateTodoCiUserGroupDetail(todoItem);
                }
                return true;
            // 平台权限申请工单
            case 3:
                List<TodoSystemAuthDetailDO> todoSystemAuthDetailList = todoDao.queryTodoSystemAuthDetailByTodoDetailId(todoDetailDO.getId());
                for (TodoSystemAuthDetailDO todoItem : todoSystemAuthDetailList) {
                    if (todoItem.isNeed()) {
                        if (todoItem.getName().equals("getway")) {
                            // 堡垒机权限
                            keyBoxService.authUserKeybox(userDO.getUsername());
                            todoItem.setProcessStatus(TodoDetailDO.ProcessStatusEnum.complete.getCode());
                        } else {
                            // LDAP权限
                            if (authService.addMemberToGroup(userDO, todoItem.getName())) {
                                todoItem.setProcessStatus(TodoDetailDO.ProcessStatusEnum.complete.getCode());
                            } else {
                                todoItem.setProcessStatus(TodoDetailDO.ProcessStatusEnum.err.getCode());
                            }
                            // jira普通用户关联开发者角色
                            if (todoItem.getName().equals("jira-users")) {
                                authService.addMemberToGroup(userDO, "jira-developers");
                            }

                        }
                        todoDao.updateTodoSystemAuthDetail(todoItem);
                    }
                }
                return true;
            // VPN申请工单
            case 4:
                List<TodoVpnDetailDO> details = todoDao.queryTodoVpnDetailByTodoDetailId(todoDetailDO.getId());
                for (TodoVpnDetailDO detail : details) {
                    if (detail.isNeed()) {
                        authService.addMemberToGroup(userDO, detail.getLdapGroup());
                        detail.setProcessStatus(TodoDetailDO.ProcessStatusEnum.complete.getCode());
                        // 添加用户至LDAP组
                        if (authService.addMemberToGroup(userDO, detail.getLdapGroup())) {
                            detail.setProcessStatus(TodoDetailDO.ProcessStatusEnum.complete.getCode());
                        } else {
                            detail.setProcessStatus(TodoDetailDO.ProcessStatusEnum.err.getCode());
                        }

                        todoDao.updateTodoVpnDetail(detail);
                    }
                }
                return true;
            // 新项目申请工单
            case 5:
                return true;
            // 前端持续集成权限申请
            case 6:
                TodoDetailVO todoDetailVO = new TodoDetailVO(todoDetailDO);
                invokeTodoDetailVO(todoDetailVO);
                //todoPropertyService.invokeTodoCmdbRole(todoDetailVO);
                HashMap<String, Object> cmdbRoleMap = todoDetailVO.getCmdbRoleMap();
                if (cmdbRoleMap.containsKey(RoleDO.roleDevelopFt)) {
                    if ((boolean) cmdbRoleMap.get(RoleDO.roleDevelopFt))
                        authService.userRoleBind(RoleDO.roleDevelopFt, userDO.getUsername());
                }
                if (cmdbRoleMap.containsKey(RoleDO.roleDevelopAndroid)) {
                    if ((boolean) cmdbRoleMap.get(RoleDO.roleDevelopAndroid))
                        authService.userRoleBind(RoleDO.roleDevelopAndroid, userDO.getUsername());
                }
                if (cmdbRoleMap.containsKey(RoleDO.roleDevelopIos)) {
                    if ((boolean) cmdbRoleMap.get(RoleDO.roleDevelopIos))
                        authService.userRoleBind(RoleDO.roleDevelopIos, userDO.getUsername());
                }

                if (cmdbRoleMap.containsKey(RoleDO.roleQualityAssurance)) {
                    if ((boolean) cmdbRoleMap.get(RoleDO.roleQualityAssurance))
                        authService.userRoleBind(RoleDO.roleQualityAssurance, userDO.getUsername());
                }

                // 插入研发权限
                authService.userRoleBind(RoleDO.roleDevelop, userDO.getUsername());
                // 重置登陆状态，更新用户界面
                authService.resetToken(userDO.getUsername());
                return true;
            case 7:
                TodoDetailVO scmTodoDetailVO = new TodoDetailVO(todoDetailDO);
                invokeTodoDetailVO(scmTodoDetailVO);
                HashMap<String, Object> scmMap = scmTodoDetailVO.getScmMap();
                if (scmMap.containsKey("ciUserGroupId")) {
                    long ciUserGroupId = Long.valueOf((String) scmMap.get("ciUserGroupId"));
                    BusinessWrapper<Boolean> businessWrapper = ciUserGroupService.userAddGroup(todoDetailDO.getInitiatorUserId(), ciUserGroupId);
                    if (businessWrapper.isSuccess())
                        return true;
                }
                return false;
            //// TODO Tomcat版本变更
            case 8:
                TodoDetailVO tomcatVersionTodoDetailVO = new TodoDetailVO(todoDetailDO);
                invokeTodoDetailVO(tomcatVersionTodoDetailVO);
                HashMap<String, Object> tomcatVersionMap = tomcatVersionTodoDetailVO.getTomcatVersionMap();

                // configServerGroupService.queryTomcatInstallVersion();

                if (tomcatVersionMap.containsKey("serverGroupId")) {
                    long serverGroupId = Long.valueOf((String) tomcatVersionMap.get("serverGroupId"));
                    ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverGroupId);
                    String tomcatVersion = (String) tomcatVersionMap.get("tomcatNewVersion");
                    configServerGroupService.saveTomcatInstallVersion(serverGroupDO, tomcatVersion);

                    String jdkVersion = (String) tomcatVersionMap.get("jdkNewVersion");
                    configServerGroupService.saveJavaInstallVersion(serverGroupDO, jdkVersion);
                    // 更新资源文件
                    configService.createConfigFileByName(ConfigServiceImpl.CONFIG_FILE_TOMCAT_RES);
                    return true;
                }
                return false;
            default:
                return false;
        }

    }


    /**
     * 查询我完成的工单
     *
     * @return
     */
    @Override
    public List<TodoDetailVO> queryCompleteTodoJob() {
        String username = SessionUtils.getUsername();
        return queryCompleteTodoJob(username);
    }


    public List<TodoDetailVO> queryCompleteTodoJob(String username) {
        //HashMap<Long, TodoDetailVO> map = new HashMap<>();
        //// TODO 判断是否为 devops
        List<UserDO> users = authService.queryUsersByRoleName(TodoDO.TodoTypeEnum.devops.getDesc());
        boolean isDevops = false;
        List<TodoDetailDO> todoDetailList = new ArrayList<>();
        for (UserDO userDO : users) {
            if (userDO.getUsername().equals(username)) {
                todoDetailList = todoDao.queryAllCompleteTodoJob();
                isDevops = true;
                break;
            }
        }
        //// TODO 查询自己的工单
        if (!isDevops) todoDetailList = todoDao.queryCompleteTodoJob(username);
        return acqTodoJob(todoDetailList);
    }

    /**
     * 组装工单
     *
     * @param todoDetailList
     * @return
     */
    private List<TodoDetailVO> acqTodoJob(List<TodoDetailDO> todoDetailList) {
        List<TodoDetailVO> todoDetailVOList = new ArrayList<TodoDetailVO>();
        for (TodoDetailDO todoDetailDO : todoDetailList) {
            todoDetailVOList.add(acqTodoJob(todoDetailDO));
        }
        return todoDetailVOList;
    }

    private TodoDetailVO acqTodoJob(TodoDetailDO todoDetailDO) {
        TodoDetailVO todoDetailVO = new TodoDetailVO(todoDetailDO);
        //// TODO 插入相关数据（负责人+工单配置+工单详情)
        invokeTodoDetailVO(todoDetailVO);
        return todoDetailVO;
    }

    /**
     * //// TODO 插入工单详情(包括新建的工单)
     *
     * @param todoDetailVO
     */
    public void invokeTodoDetailVO(TodoDetailVO todoDetailVO) {
        int id = (int) todoDetailVO.getTodoId();
        long todoDetailId = todoDetailVO.getId();
        switch (id) {
            // 堡垒机权限申请工单
            case 1:
                List<TodoKeyboxDetailDO> todoKeyboxDetailList = todoDao.queryTodoKeyboxDetailByTodoDetailId(todoDetailVO.getId());
                List<TodoKeyboxDetailVO> todoKeyboxDetailVOList = new ArrayList<>();
                for (TodoKeyboxDetailDO todoItem : todoKeyboxDetailList) {
                    ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(todoItem.getServerGroupId());
                    TodoKeyboxDetailVO todoKeyboxDetailVO = new TodoKeyboxDetailVO(todoItem, serverGroupDO);
                    todoKeyboxDetailVOList.add(todoKeyboxDetailVO);
                    todoDetailVO.setContent(todoDetailVO.getContent() + todoKeyboxDetailVO.getContent());
                }
                todoDetailVO.setTodoKeyboxDetailList(todoKeyboxDetailVOList);
                break;
            // 持续集成权限申请工单
            case 2:
                List<TodoCiUserGroupDetailDO> todoCiUserGroupDetailList = todoDao.queryTodoCiUserGroupDetailByTodoDetailId(todoDetailVO.getId());
                List<TodoCiUserGroupDetailVO> todoCiUserGroupDetailVOList = new ArrayList<>();
                for (TodoCiUserGroupDetailDO todoItem : todoCiUserGroupDetailList) {
                    TodoCiUserGroupDetailVO todoCiUserGroupDetailVO;
                    if (todoItem.getServerGroupId() != 0) {
                        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(todoItem.getServerGroupId());
                        todoCiUserGroupDetailVO = new TodoCiUserGroupDetailVO(todoItem, serverGroupDO);
                    } else {
                        todoCiUserGroupDetailVO = new TodoCiUserGroupDetailVO(todoItem);
                    }
                    todoCiUserGroupDetailVOList.add(todoCiUserGroupDetailVO);
                    todoDetailVO.setContent(todoDetailVO.getContent() + todoCiUserGroupDetailVO.getContent());
                }
                todoDetailVO.setTodoCiUserGroupDetailList(todoCiUserGroupDetailVOList);
                break;
            // 平台权限申请工单
            case 3:
                List<TodoSystemAuthDetailDO> todoSystemAuthDetailList = todoDao.queryTodoSystemAuthDetailByTodoDetailId(todoDetailVO.getId());
                List<TodoSystemAuthDetailDO> todoDetailList = new ArrayList<>();
                if (todoSystemAuthDetailList.size() == 0) {
                    // 新工单需要初始化数据
                    initTodoSystemAuthDetail(todoDetailVO);
                } else {
                    for (TodoSystemAuthDetailDO todoItem : todoSystemAuthDetailList) {
                        if (todoItem.getName().equals("getway")) {
                            todoDetailVO.setTodoSystemAuthGetway(todoItem);
                        } else {
                            todoDetailList.add(todoItem);
                        }
                        if (todoItem.isNeed()) {
                            todoDetailVO.setContent(todoDetailVO.getContent() + todoItem.getContent());
                        }
                    }
                    todoDetailVO.setTodoSystemAuthDetailList(todoDetailList);
                }
                break;
            //// todo VPN申请工单
            case 4:
                List<TodoVpnDetailDO> details = todoDao.queryTodoVpnDetailByTodoDetailId(todoDetailVO.getId());
                //List<TodoVpnDetailDO> todoDetailList = new ArrayList<>();
                if (details.size() == 0) {
                    // 新工单需要初始化数据
                    initTodoVpnDetail(todoDetailVO);
                } else {
                    // 其实只有一条数据
                    for (TodoVpnDetailDO deatil : details) {
                        todoDetailVO.setContent(todoDetailVO.getContent());
                        UserDO userDO = userDao.getUserById(todoDetailVO.getInitiatorUserId());
                        boolean isAuthed = authService.isGroupMember(userDO.getUsername(), TodoVpnDetailDO.VPN_LDAP_GRPOUP_NAME);
                        TodoVpnDetailVO todoVpnDetailVO = new TodoVpnDetailVO(deatil, isAuthed);
                        todoDetailVO.setTodoVpn(todoVpnDetailVO);
                    }
                }
                break;
            //// todo 新项目申请工单
            case 5:
                // 插入工单主属性
                todoPropertyService.invokeTodoNewProject(todoDetailVO);
                todoDetailVO.setTodoNewServerDetailList(todoDao.queryTodoNewServerDetailByTodoDetailId(todoDetailId));
                todoDetailVO.setTodoBuilderPlanDetailList(todoDao.queryTodoBuilderPlanDetailByTodoDetailId(todoDetailId));
                break;
            //// todo 持续集成前端权限
            case 6:
                // 插入工单主属性
                todoPropertyService.invokeTodoCmdbRole(todoDetailVO);
                break;
            //// todo SCM组权限
            case 7:
                // 插入工单主属性
                todoPropertyService.invokeTodoScm(todoDetailVO);
                break;
            //// todo Tomcat版本变更
            case 8:
                todoPropertyService.invokeTodoTomcatVersion(todoDetailVO);
            default:
        }
        //// TODO 插入工单
        TodoDO todoDO = todoDao.queryTodoInfoById(todoDetailVO.getTodoId());
        todoDetailVO.setTodoDO(todoDO);
        //// TODO 插入负责人
        invokeAssigneeUsers(todoDetailVO);
        //// TODO 插入所有负责人
        if (todoDetailVO.getAssigneeUserId() != 0)
            todoDetailVO.setAssigneeUserDO(acqTodoUserDO(todoDetailVO.getAssigneeUserId()));
        //// TODO 插入发起人
        todoDetailVO.setInitiatorUserDO(acqTodoUserDO(todoDetailVO.getInitiatorUserId()));
        //// TODO 插入工单历时
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
            Date modifyDate = format.parse(todoDetailVO.getGmtModify());
            todoDetailVO.setTimeView(TimeViewUtils.format(modifyDate));
        } catch (Exception e) {
        }
    }

    private void initTodoVpnDetail(TodoDetailVO todoDetailVO) {
        UserDO userDO = userDao.getUserById(todoDetailVO.getInitiatorUserId());
        TodoVpnDetailDO todoVpnDetailDO = new TodoVpnDetailDO(todoDetailVO.getId());
        boolean isAuthed = authService.isGroupMember(userDO.getUsername(), TodoVpnDetailDO.VPN_LDAP_GRPOUP_NAME);
        try {
            todoDao.addTodoVpnDetail(todoVpnDetailDO);
            TodoVpnDetailVO todoVpnDetailVO = new TodoVpnDetailVO(todoVpnDetailDO, isAuthed);
            todoDetailVO.setTodoVpn(todoVpnDetailVO);
        } catch (Exception e) {
        }
    }

    // 初始化工单详情
    private void initTodoSystemAuthDetail(TodoDetailVO todoDetailVO) {
        UserDO userDO = userDao.getUserById(todoDetailVO.getInitiatorUserId());
        List<TodoSystemAuthDetailDO> list = new ArrayList<>();
        String username = userDO.getUsername();
        list.add(new TodoSystemAuthDetailDO(todoDetailVO.getId(), "confluence-users", "wiki普通用户权限;", authService.isGroupMember(username, "confluence-users")));
        list.add(new TodoSystemAuthDetailDO(todoDetailVO.getId(), "designwiki-users", "设计部wiki普通用户权限;", authService.isGroupMember(username, "designwiki-users")));
        list.add(new TodoSystemAuthDetailDO(todoDetailVO.getId(), "jira-users", "jira普通用户权限;", authService.isGroupMember(username, "jira-users")));
        list.add(new TodoSystemAuthDetailDO(todoDetailVO.getId(), "jira-administrators", "jira管理员权限(可以创建和管理项目);", authService.isGroupMember(username, "jira-administrators")));
        list.add(new TodoSystemAuthDetailDO(todoDetailVO.getId(), "nexus-developer", "nexus开发者权限(允许deploy)", authService.isGroupMember(username, "nexus-developer")));
        TodoSystemAuthDetailDO getway = new TodoSystemAuthDetailDO(todoDetailVO.getId(), "堡垒机授权(若更改密码或公钥需要重新授权);", (userDO.getAuthed() == 1) ? true : false);
        try {
            todoDao.addTodoSystemAuthDetail(getway);
        } catch (Exception e) {
        }
        for (TodoSystemAuthDetailDO todoSystemAuthDetailDO : list) {
            try {
                todoDao.addTodoSystemAuthDetail(todoSystemAuthDetailDO);
            } catch (Exception e) {
            }
        }
        todoDetailVO.setTodoSystemAuthDetailList(list);
        todoDetailVO.setTodoSystemAuthGetway(getway);
    }

    private UserDO acqTodoUserDO(long userId) {
        UserDO userDO = userDao.getUserById(userId);
        UserDO todoUserDO = new UserDO();
        if (userDO != null) {

            todoUserDO.setUsername(userDO.getUsername());
            if (!StringUtils.isEmpty(userDO.getDisplayName()))
                todoUserDO.setDisplayName(userDO.getDisplayName());
            if (!StringUtils.isEmpty(userDO.getMail()))
                todoUserDO.setMail(userDO.getMail());
            if (!StringUtils.isEmpty(userDO.getMobile()))
                todoUserDO.setMobile(userDO.getMobile());
        } else {
            todoUserDO.setDisplayName("用户不存在(离职)");
        }
        return todoUserDO;
    }


    /**
     * 保存工单
     *
     * @param todoDetailVO
     * @return
     */
    @Override
    public TodoDetailVO saveTodoDetail(TodoDetailVO todoDetailVO) {

        int id = (int) todoDetailVO.getTodoId();
        switch (id) {
            // 新项目申请工单
            case 5:
                return saveTodoNewProject(todoDetailVO);
            case 6:
                return saveTodoCmdbRole(todoDetailVO);
            case 7:
                return saveTodoScm(todoDetailVO);
            case 8:
                return saveTodoTomcatVersion(todoDetailVO);
            default:
                return todoDetailVO;
        }
    }

    // 保存新项目申请工单
    private TodoDetailVO saveTodoNewProject(TodoDetailVO todoDetailVO) {
        HashMap<String, Object> map = todoDetailVO.getNewProjectMap();
        // 删除老得配置
        delTodoPropertyByTodoDetailId(todoDetailVO.getId());
        // 增加配置
        addTodoProperty(todoDetailVO, map, "projectName");
        addTodoProperty(todoDetailVO, map, "projectContent");

        addTodoProperty(todoDetailVO, map, "buildTool");
        addTodoProperty(todoDetailVO, map, "jdkVersion");
        addTodoProperty(todoDetailVO, map, "buildParams");
        addTodoProperty(todoDetailVO, map, "deployType");
        addTodoProperty(todoDetailVO, map, "tomcatVersion");
        addTodoProperty(todoDetailVO, map, "stashProject");
        addTodoProperty(todoDetailVO, map, "stashRepository");
        addTodoProperty(todoDetailVO, map, "environmentVariables");
        addTodoProperty(todoDetailVO, map, "workingSubDirectory");
        addTodoProperty(todoDetailVO, map, "leaderUserId");

        addTodoProperty(todoDetailVO, map, "httpStatus");
        addTodoProperty(todoDetailVO, map, "tomcatAppName");
        addTodoProperty(todoDetailVO, map, "tomcatWebAppsPath");

        try {
            // 执行了创建服务器组标准位
            addTodoProperty(todoDetailVO, "procServerGroup", (Boolean) map.get("procServerGroup"));
            addTodoProperty(todoDetailVO, "webWww", (Boolean) map.get("webWww"));
            addTodoProperty(todoDetailVO, "webManage", (Boolean) map.get("webManage"));
            addTodoProperty(todoDetailVO, "webKa", (Boolean) map.get("webKa"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONArray users = JSONArray.parseArray(map.get("users").toString());
            for (int i = 0; i < users.size(); i++) {
                JSONObject userJO = users.getJSONObject(i);
                addTodoProperty(todoDetailVO, "user", userJO.getString("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        todoPropertyService.invokeTodoNewProject(todoDetailVO);
        return todoDetailVO;
    }

    // 保存新项目申请工单
    private TodoDetailVO saveTodoCmdbRole(TodoDetailVO todoDetailVO) {
        HashMap<String, Object> map = todoDetailVO.getCmdbRoleMap();
        // 删除老得配置
        delTodoPropertyByTodoDetailId(todoDetailVO.getId());
        // 增加配置
        addTodoProperty(todoDetailVO, map, "devFt");
        addTodoProperty(todoDetailVO, map, "devAndroid");
        addTodoProperty(todoDetailVO, map, "devIos");
        addTodoProperty(todoDetailVO, map, "qa");

        addTodoProperty(todoDetailVO, map, "isDevFt");
        addTodoProperty(todoDetailVO, map, "isDevAndroid");
        addTodoProperty(todoDetailVO, map, "isDevIos");
        addTodoProperty(todoDetailVO, map, "isQa");
        todoPropertyService.invokeTodoCmdbRole(todoDetailVO);
        return todoDetailVO;
    }

    // 保存SCM权限申请工单
    private TodoDetailVO saveTodoScm(TodoDetailVO todoDetailVO) {
        HashMap<String, Object> map = todoDetailVO.getScmMap();
        // 删除老得配置
        delTodoPropertyByTodoDetailId(todoDetailVO.getId());
        // 增加配置
        addTodoProperty(todoDetailVO, map, "ciUserGroupId");
        addTodoProperty(todoDetailVO, map, "groupName");
        addTodoProperty(todoDetailVO, map, "scmProject");
        addTodoProperty(todoDetailVO, map, "scmDesc");
        todoPropertyService.invokeTodoScm(todoDetailVO);
        return todoDetailVO;
    }

    private TodoDetailVO saveTodoTomcatVersion(TodoDetailVO todoDetailVO) {
        HashMap<String, Object> map = todoDetailVO.getTomcatVersionMap();
        // 删除老得配置
        delTodoPropertyByTodoDetailId(todoDetailVO.getId());
        // 增加配置
        addTodoProperty(todoDetailVO, map, "serverGroupId");
        addTodoProperty(todoDetailVO, map, "groupName");
        addTodoProperty(todoDetailVO, map, "tomcatInstallVersion");
        addTodoProperty(todoDetailVO, map, "jdkInstallVersion");
        addTodoProperty(todoDetailVO, map, "tomcatNewVersion");
        addTodoProperty(todoDetailVO, map, "jdkNewVersion");
        todoPropertyService.invokeTodoTomcatVersion(todoDetailVO);
        return todoDetailVO;
    }


    private boolean addTodoProperty(TodoDetailVO todoDetailVO, HashMap<String, Object> map, String key) {
        //System.err.println(key);
        TodoPropertyDO todoPropertyDO = new TodoPropertyDO(todoDetailVO, key, map.get(key).toString());
        return addTodoProperty(todoPropertyDO);
    }

    private boolean addTodoProperty(TodoDetailVO todoDetailVO, String key, String value) {
        TodoPropertyDO todoPropertyDO = new TodoPropertyDO(todoDetailVO, key, value);
        return addTodoProperty(todoPropertyDO);
    }

    private boolean addTodoProperty(TodoDetailVO todoDetailVO, String key, boolean value) {
        TodoPropertyDO todoPropertyDO = new TodoPropertyDO(todoDetailVO, key, value);
        return addTodoProperty(todoPropertyDO);
    }

    private boolean addTodoProperty(TodoPropertyDO todoPropertyDO) {
        try {
            todoDao.addTodoProperty(todoPropertyDO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除老得配置
    private boolean delTodoPropertyByTodoDetailId(long todoDetailId) {
        List<TodoPropertyDO> list = todoDao.queryTodoPropertyByTodoDetailId(todoDetailId);
        try {
            for (TodoPropertyDO todoPropertyDO : list) {
                todoDao.delTodoPropertyById(todoPropertyDO.getId());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public TodoBuilderPlanDetailDO saveBuilderPlan(TodoBuilderPlanDetailDO todoBuilderPlanDetailDO) {

        if (todoBuilderPlanDetailDO.isNeedEnvParams()) {
            if (todoBuilderPlanDetailDO.getBuildTool() == TodoBuilderPlanDetailDO.BuildToolTypeEnum.gradle.getCode() && StringUtils.isEmpty(todoBuilderPlanDetailDO.getEnvParams())) {
                if (todoBuilderPlanDetailDO.getEnvType() == ServerDO.EnvTypeEnum.daily.getCode())
                    todoBuilderPlanDetailDO.setEnvParams("-Denv=daily");
                if (todoBuilderPlanDetailDO.getEnvType() == ServerDO.EnvTypeEnum.gray.getCode())
                    todoBuilderPlanDetailDO.setEnvParams("-Denv=gray");
                if (todoBuilderPlanDetailDO.getEnvType() == ServerDO.EnvTypeEnum.prod.getCode())
                    todoBuilderPlanDetailDO.setEnvParams("-Denv=online");
            }
            if (todoBuilderPlanDetailDO.getBuildTool() == TodoBuilderPlanDetailDO.BuildToolTypeEnum.maven.getCode() && StringUtils.isEmpty(todoBuilderPlanDetailDO.getEnvParams())) {
                if (todoBuilderPlanDetailDO.getEnvType() == ServerDO.EnvTypeEnum.daily.getCode())
                    todoBuilderPlanDetailDO.setEnvParams("-Pdaily");
                if (todoBuilderPlanDetailDO.getEnvType() == ServerDO.EnvTypeEnum.gray.getCode())
                    todoBuilderPlanDetailDO.setEnvParams("-Pgray");
                if (todoBuilderPlanDetailDO.getEnvType() == ServerDO.EnvTypeEnum.prod.getCode())
                    todoBuilderPlanDetailDO.setEnvParams("-Ponline");
            }
        } else {
            todoBuilderPlanDetailDO.setEnvParams("");
        }


        try {
            TodoBuilderPlanDetailDO todoDO = todoDao.queryTodoBuilderPlanDetailByTodoDetailIdAndEnvType(todoBuilderPlanDetailDO.getTodoDetailId(), todoBuilderPlanDetailDO.getEnvType());
            if (todoDO == null) {
                todoDao.addTodoBuilderPlanDetail(todoBuilderPlanDetailDO);
            } else {
                todoDao.updateTodoBuilderPlanDetail(todoBuilderPlanDetailDO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todoBuilderPlanDetailDO;
    }

    @Override
    public BusinessWrapper<Boolean> delBuilderPlan(long id) {
        try {
            todoDao.delTodoBuilderPlanDetail(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public TableVO<List<TodoBuilderPlanDetailDO>> queryBuilderPlan(long todoDetailId) {
        List<TodoBuilderPlanDetailDO> list = todoDao.queryTodoBuilderPlanDetailByTodoDetailId(todoDetailId);
        return new TableVO<>(list.size(), list);
    }

    @Override
    public TableVO<List<TodoNewServerDetailDO>> queryNewServer(long todoDetailId) {
        List<TodoNewServerDetailDO> list = todoDao.queryTodoNewServerDetailByTodoDetailId(todoDetailId);
        return new TableVO<>(list.size(), list);
    }


    @Override
    public BusinessWrapper<Boolean> delNewServer(long id) {
        try {
            todoDao.delTodoNewServerDetail(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }

    }

    @Override
    public TodoNewServerDetailDO saveNewServer(TodoNewServerDetailDO todoNewServerDetailDO) {
        TodoNewServerDetailDO todoNewServer = todoDao.queryTodoNewServerDetailByTodoDetailIdAndEnvType(todoNewServerDetailDO.getTodoDetailId(), todoNewServerDetailDO.getEnvType());
        try {
            if (todoNewServer == null) {
                todoDao.addTodoNewServerDetail(todoNewServerDetailDO);
            } else {
                todoDao.updateTodoNewServerDetail(todoNewServerDetailDO);
            }
            return todoNewServerDetailDO;
        } catch (
                Exception e
                ) {
            e.printStackTrace();
        }
        return new TodoNewServerDetailDO();
    }

    @Override
    public BusinessWrapper<Boolean> checkProjectName(String projectName) {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_" + projectName);
        if (serverGroupDO == null) {
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.serverGroupHasUse);
        }
    }


    /**
     * 管理员按类型处理新建项目工单
     *
     * @param todoDetailId
     * @param type
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> procNewProject(long todoDetailId, int type) {
        TodoDetailDO todoDetailDO = todoDao.queryTodoDetailById(todoDetailId);
        TodoDetailVO todoDetailVO = new TodoDetailVO(todoDetailDO);
        invokeTodoDetailVO(todoDetailVO);
        if (type == this.NEWPROJECT_CREATE_SERVERGROUP)
            return procNewProjectCreateServerGroup(todoDetailVO);

        return new BusinessWrapper<Boolean>(false);
    }

    /**
     * 添加服务器组
     *
     * @param todoDetailVO
     * @return
     */
    private BusinessWrapper<Boolean> procNewProjectCreateServerGroup(TodoDetailVO todoDetailVO) {
        HashMap<String, Object> map = todoDetailVO.getNewProjectMap();
        String groupName = "group_" + (String) map.get("projectName");
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName(groupName);
        if (serverGroupDO != null) return new BusinessWrapper<Boolean>(ErrorCode.serverGroupHasUse);
        String content = (String) map.get("projectContent");
        serverGroupDO = new ServerGroupDO(groupName, content, ServerGroupDO.UseTypeEnum.webservice.getCode());
        try {
            serverGroupDao.addServerGroupInfo(serverGroupDO);
            updateTodoPropertyValue(todoDetailVO.getId(), "procServerGroup", "true");
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(ErrorCode.serverFailure);
        }
    }

    private boolean updateTodoPropertyValue(long todoDetailId, String todoKey, String todoValue) {
        try {
            List<TodoPropertyDO> list = todoDao.queryTodoPropertyByTodoDetailIdAndTodoKey(todoDetailId, todoKey);
            for (TodoPropertyDO todoPropertyDO : list) {
                todoDao.delTodoPropertyById(todoPropertyDO.getId());
                todoPropertyDO.setTodoValue(todoValue);
                todoDao.addTodoProperty(todoPropertyDO);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public TodoTomcatVersionVO getTomcatVersion(long serverGroupId) {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverGroupId);
        String tomcatInstallVersion = configServerGroupService.queryTomcatInstallVersion(serverGroupDO);
        String jdkInstallVersion = configServerGroupService.queryJavaInstallVersion(serverGroupDO);
        TodoTomcatVersionVO tomcatVersionVO = new TodoTomcatVersionVO(tomcatInstallVersion, jdkInstallVersion);
        return tomcatVersionVO;
    }








    // 判断日志清理是否成功
    private boolean checkTaskUpdateTomcat(String invokeStr) {
        // 自动安装Tomcat成功
        // 启动Tomcat服务成功
        boolean installSuccess = checkSuccess(invokeStr, "自动安装Tomcat成功");
        boolean startSuccess = checkSuccess(invokeStr, "启动Tomcat服务成功");
        return installSuccess && startSuccess;
    }


    // 检查日志中的关键字
    private boolean checkSuccess(String invokeStr, String checkStr) {
        if (StringUtils.isEmpty(invokeStr)) return false;
        if (invokeStr.indexOf(checkStr) != -1) {
            return true;
        } else {
            return false;
        }
    }

}
