package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.AuthDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.TodoDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.dao.stash.StashDao;
import com.sdg.cmdb.domain.auth.CiUserGroupDO;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.todo.TodoDetailVO;
import com.sdg.cmdb.domain.todo.TodoPropertyDO;
import com.sdg.cmdb.domain.todo.todoProperty.StashProjectDO;
import com.sdg.cmdb.service.ConfigServerGroupService;
import com.sdg.cmdb.service.TodoPropertyService;
import com.sdg.cmdb.util.SessionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TodoPropertyServiceImpl implements TodoPropertyService {

    @Resource
    private TodoDao todoDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ConfigServerGroupService configServerGroupService;

    @Resource
    private AuthDao authDao;


    @Resource
    private StashDao stashDao;


    @Override
    public void invokeTodoNewProject(TodoDetailVO todoDetailVO) {
        HashMap<String, Object> map = acqTodoNewProject(todoDetailVO);
        todoDetailVO.setNewProjectMap(map);
        String projectName = (String) map.get("projectName");
        if (!StringUtils.isEmpty(projectName)) {
            todoDetailVO.setContent("项目名称:" + projectName);
        }
    }

    @Override
    public void invokeTodoCmdbRole(TodoDetailVO todoDetailVO) {
        HashMap<String, Object> map = acqTodoCmdbRole(todoDetailVO);
        todoDetailVO.setCmdbRoleMap(map);
        String content = (String) map.get("content");
        if (!StringUtils.isEmpty(content)) {
            todoDetailVO.setContent(content);
        }
    }

    @Override
    public void invokeTodoScm(TodoDetailVO todoDetailVO) {
        HashMap<String, Object> map = acqTodoScm(todoDetailVO);
        todoDetailVO.setScmMap(map);
        String content = (String) map.get("content");
        if (!StringUtils.isEmpty(content)) {
            todoDetailVO.setContent(content);
        }
    }

    @Override
    public void invokeTodoTomcatVersion(TodoDetailVO todoDetailVO) {
        HashMap<String, Object> map = acqTodoTomcatVersion(todoDetailVO);
        todoDetailVO.setTomcatVersionMap(map);
        String content = (String) map.get("content");
        if (!StringUtils.isEmpty(content)) {
            todoDetailVO.setContent(content);
        }
    }

    // 获取工单
    public HashMap<String, Object> acqTodoNewProject(TodoDetailVO todoDetailVO) {
        List<TodoPropertyDO> list = todoDao.queryTodoPropertyByTodoDetailId(todoDetailVO.getId());
        if (list.size() == 0)
            return acqNewTodoNewProject(todoDetailVO);
        HashMap<String, Object> map = new HashMap<>();
        List<UserDO> users = new ArrayList<>();
        String projectName;
        for (TodoPropertyDO property : list) {
            if (property.getTodoKey().equalsIgnoreCase("projectName"))
                map.put("projectName", property.getTodoValue());
            if (property.getTodoKey().equalsIgnoreCase("projectContent"))
                map.put("projectContent", property.getTodoValue());
            if (property.getTodoKey().equalsIgnoreCase("buildTool"))
                map.put("buildTool", Integer.valueOf(property.getTodoValue()));
            if (property.getTodoKey().equalsIgnoreCase("jdkVersion"))
                map.put("jdkVersion", Integer.valueOf(property.getTodoValue()));
            if (property.getTodoKey().equalsIgnoreCase("buildParams"))
                map.put("buildParams", property.getTodoValue());
            if (property.getTodoKey().equalsIgnoreCase("deployType"))
                map.put("deployType", Integer.valueOf(property.getTodoValue()));
            if (property.getTodoKey().equalsIgnoreCase("tomcatVersion"))
                map.put("tomcatVersion", Integer.valueOf(property.getTodoValue()));
            if (property.getTodoKey().equalsIgnoreCase("stashProject"))
                map.put("stashProject", property.getTodoValue());
            if (property.getTodoKey().equalsIgnoreCase("stashRepository"))
                map.put("stashRepository", property.getTodoValue());
            if (property.getTodoKey().equalsIgnoreCase("environmentVariables"))
                map.put("environmentVariables", property.getTodoValue());
            if (property.getTodoKey().equalsIgnoreCase("workingSubDirectory"))
                map.put("workingSubDirectory", property.getTodoValue());
            if (property.getTodoKey().equalsIgnoreCase("leaderUserId")) {
                UserDO leaderUserDO = userDao.getUserById(Long.valueOf(property.getTodoValue()));
                map.put("leaderUser", leaderUserDO);
                map.put("leaderUserId", leaderUserDO.getId());
            }
            if (property.getTodoKey().equalsIgnoreCase("todoContent"))
                map.put("todoContent", property.getTodoValue());
            if (property.getTodoKey().equalsIgnoreCase("user")) {
                UserDO user = userDao.getUserById(Long.valueOf(property.getTodoValue()));
                if (user != null) users.add(user);
            }
            //TOMCAT_APP_NAME_OPT
            if (property.getTodoKey().equalsIgnoreCase("tomcatAppName"))
                map.put("tomcatAppName", property.getTodoValue());
            //TOMCAT_SERVERXML_WEBAPPSPATH_OPT
            if (property.getTodoKey().equalsIgnoreCase("tomcatWebAppsPath"))
                map.put("tomcatWebAppsPath", property.getTodoValue());
            //HTTP_STATUS_OPT
            if (property.getTodoKey().equalsIgnoreCase("httpStatus"))
                map.put("httpStatus", property.getTodoValue());

            if (property.getTodoKey().equalsIgnoreCase("procServerGroup"))
                map.put("procServerGroup", property.getTodoValue().equalsIgnoreCase("true"));

            if (property.getTodoKey().equalsIgnoreCase("webWww"))
                map.put("webWww", property.getTodoValue().equalsIgnoreCase("true"));
            if (property.getTodoKey().equalsIgnoreCase("webManage"))
                map.put("webManage", property.getTodoValue().equalsIgnoreCase("true"));
            if (property.getTodoKey().equalsIgnoreCase("webKa"))
                map.put("webKa", property.getTodoValue().equalsIgnoreCase("true"));

        }
        map.put("users", users);
        map.put("plans", new ArrayList<>());
        return map;
    }

    // 获取工单
    public HashMap<String, Object> acqTodoCmdbRole(TodoDetailVO todoDetailVO) {
        List<TodoPropertyDO> list = todoDao.queryTodoPropertyByTodoDetailId(todoDetailVO.getId());
        if (list.size() == 0)
            return acqNewTodoCmdbRole(todoDetailVO);
        HashMap<String, Object> map = new HashMap<>();
        String content = "";
        for (TodoPropertyDO property : list) {

            if (property.getTodoKey().equalsIgnoreCase(RoleDO.roleDevelopFt)) {
                boolean needDevFt = property.getTodoValue().equalsIgnoreCase("true");
                map.put(RoleDO.roleDevelopFt, needDevFt);
                if (needDevFt) content += "前端持续集成权限；";
            }

            if (property.getTodoKey().equalsIgnoreCase(RoleDO.roleDevelopAndroid)) {
                boolean needDevAndroid = property.getTodoValue().equalsIgnoreCase("true");
                map.put(RoleDO.roleDevelopAndroid, needDevAndroid);
                if (needDevAndroid) content += "Android持续集成权限；";
            }

            if (property.getTodoKey().equalsIgnoreCase(RoleDO.roleDevelopIos)) {
                boolean needDevIos = property.getTodoValue().equalsIgnoreCase("true");
                map.put(RoleDO.roleDevelopIos, needDevIos);
                if (needDevIos) content += "iOS持续集成权限；";
            }

            if (property.getTodoKey().equalsIgnoreCase(RoleDO.roleQualityAssurance)) {
                boolean needQa = property.getTodoValue().equalsIgnoreCase("true");
                map.put(RoleDO.roleQualityAssurance, needQa);
                if (needQa) content += "QA自动化测试管理权限；";
            }

            if (property.getTodoKey().equalsIgnoreCase("isDevFt")) ;
            map.put("isDevFt", property.getTodoValue().equalsIgnoreCase("true"));

            if (property.getTodoKey().equalsIgnoreCase("isDevAndroid"))
                map.put("isDevAndroid", property.getTodoValue().equalsIgnoreCase("true"));

            if (property.getTodoKey().equalsIgnoreCase("isDevIos"))
                map.put("isDevIos", property.getTodoValue().equalsIgnoreCase("true"));

            if (property.getTodoKey().equalsIgnoreCase("isQa"))
                map.put("isQa", property.getTodoValue().equalsIgnoreCase("true"));
        }
        map.put("content", content);
        return map;
    }

    // 获取新工单
    private HashMap<String, Object> acqNewTodoNewProject(TodoDetailVO todoDetailVO) {
        String usernmae = SessionUtils.getUsername();
        UserDO leaderUserDO = userDao.getUserByName(usernmae);
        HashMap<String, Object> map = new HashMap<>();
        map.put("projectName", "");
        map.put("projectContent", "");
        map.put("buildTool", 0);
        map.put("jdkVersion", 0);
        map.put("buildParams", "");
        // 0:war  1:jar
        map.put("deployType", 0);
        map.put("tomcatVersion", 0);
        map.put("stashProject", "");
        map.put("stashRepository", "");
        // Extra environment variables. e.g. MAVEN_OPTS="-Xmx256m -Xms128m". You can add multiple parameters separated by a space.
        map.put("environmentVariables", "");
        // Specify an alternative sub-directory as working directory for the task.
        map.put("workingSubDirectory", "");
        // 项目负责人
        map.put("leaderUser", leaderUserDO);
        map.put("leaderUserId", leaderUserDO.getId());
        map.put("todoContent", "");
        map.put("users", new ArrayList<>());
        // 构建计划（构建环境）
        map.put("plans", new ArrayList<>());
        map.put("tomcatAppName", "");
        map.put("tomcatWebAppsPath", "");
        map.put("httpStatus", "");
        map.put("procServerGroup", false);
        map.put("webWww", false);
        map.put("webManage", false);
        map.put("webKa", false);
        return map;

    }

    // 获取新工单
    private HashMap<String, Object> acqNewTodoCmdbRole(TodoDetailVO todoDetailVO) {
        //String usernmae = SessionUtils.getUsername();
        //UserDO leaderUserDO = userDao.getUserByName(usernmae);
        HashMap<String, Object> map = new HashMap<>();
        map.put(RoleDO.roleDevelopFt, false);
        map.put(RoleDO.roleDevelopAndroid, false);
        map.put(RoleDO.roleDevelopIos, false);
        map.put(RoleDO.roleQualityAssurance, false);
        map.put("isDevFt", false);
        map.put("isDevAndroid", false);
        map.put("isDevIos", false);
        map.put("isQa", false);
        String username = SessionUtils.getUsername();
        List<RoleDO> roles = authDao.getUserRoles(username);
        for (RoleDO roleDO : roles) {
            if (roleDO.getRoleName().equalsIgnoreCase(RoleDO.roleDevelopFt))
                map.put("isDevFt", true);
            if (roleDO.getRoleName().equalsIgnoreCase(RoleDO.roleDevelopAndroid))
                map.put("isDevAndroid", true);
            if (roleDO.getRoleName().equalsIgnoreCase(RoleDO.roleDevelopIos))
                map.put("isDevIos", true);
            if (roleDO.getRoleName().equalsIgnoreCase(RoleDO.roleQualityAssurance))
                map.put("isQa", true);
        }
        return map;
    }


    // 获取工单
    public HashMap<String, Object> acqTodoScm(TodoDetailVO todoDetailVO) {
        List<TodoPropertyDO> list = todoDao.queryTodoPropertyByTodoDetailId(todoDetailVO.getId());
        if (list.size() == 0) {
            return acqNewTodoScm(todoDetailVO);
        }
        HashMap<String, Object> map = new HashMap<>();
        String content = "";
        for (TodoPropertyDO property : list) {
            // 插入持续集成权限组
            if (property.getTodoKey().equalsIgnoreCase("ciUserGroupId")) {
                map.put("ciUserGroupId", property.getTodoValue());
                CiUserGroupDO ciUserGroupDO = userDao.getCiUserGroupById(Long.valueOf(property.getTodoValue()));
                map.put("ciUserGroup", ciUserGroupDO);
            }

            if (property.getTodoKey().equalsIgnoreCase("scmProject")) {
                String scmProject = property.getTodoValue();
                map.put("scmProject", scmProject);
                content = "SCM项目名称:" + scmProject;
                // 插入 stashProjectDO
                StashProjectDO stashProjectDO = stashDao.getStashProjectByName(scmProject);
                map.put("scmProjectDO", stashProjectDO);
            }

            if (property.getTodoKey().equalsIgnoreCase("groupName")) {
                String groupName = property.getTodoValue();
                map.put("groupName", property.getTodoValue());
                content += ", 持续集成组:" + groupName;
            }

            if (property.getTodoKey().equalsIgnoreCase("scmDesc"))
                map.put("scmDesc", property.getTodoValue());
        }
        content += ";";
        map.put("content", content);
        return map;
    }

    public HashMap<String, Object> acqTodoTomcatVersion(TodoDetailVO todoDetailVO) {
        List<TodoPropertyDO> list = todoDao.queryTodoPropertyByTodoDetailId(todoDetailVO.getId());
        if (list.size() == 0) {
            return acqNewTodoTomcatVersion(todoDetailVO);
        }
        HashMap<String, Object> map = new HashMap<>();
        String content;
        String tomcatInstallVersion = "";
        String jdkInstallVersion = "";
        String tomcatNewVersion = "";
        String jdkNewVersion = "";
        for (TodoPropertyDO property : list) {
            // 插入服务器组
            if (property.getTodoKey().equalsIgnoreCase("serverGroupId")) {
                map.put("serverGroupId", property.getTodoValue());

                ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(Long.valueOf(property.getTodoValue()));
                map.put("serverGroupDO", serverGroupDO);
                map.put("groupName", serverGroupDO.getName());
            }

            // 原Tomcat版本
            if (property.getTodoKey().equalsIgnoreCase("tomcatInstallVersion")) {
                tomcatInstallVersion = property.getTodoValue();
                map.put("tomcatInstallVersion", tomcatInstallVersion);
            }

            // 原JDK版本
            if (property.getTodoKey().equalsIgnoreCase("jdkInstallVersion")) {
                jdkInstallVersion = property.getTodoValue();
                map.put("jdkInstallVersion", jdkInstallVersion);
            }

            // 新Tomcat版本
            if (property.getTodoKey().equalsIgnoreCase("tomcatNewVersion")) {
                tomcatNewVersion = property.getTodoValue();
                map.put("tomcatNewVersion", tomcatNewVersion);
            }

            // 新JDK版本
            if (property.getTodoKey().equalsIgnoreCase("jdkNewVersion")) {
                jdkNewVersion = property.getTodoValue();
                map.put("jdkNewVersion", jdkNewVersion);
            }
        }
        content = "新Tomcat版本" + tomcatNewVersion + "（原版本" + tomcatInstallVersion + "），";
        content += "新JDK版本" + jdkNewVersion + "（原版本" + jdkInstallVersion + "）;";
        map.put("content", content);
        return map;
    }


    // 获取新工单
    private HashMap<String, Object> acqNewTodoScm(TodoDetailVO todoDetailVO) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ciUserGroupId", 0);
        map.put("groupName", "");
        map.put("scmProject", "");
        map.put("scmDesc", "");
        map.put("content", "null");
        return map;

    }


    // 获取新工单
    private HashMap<String, Object> acqNewTodoTomcatVersion(TodoDetailVO todoDetailVO) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("serverGroupId", 0);
        map.put("groupName", "");
        map.put("tomcatInstallVersion", "");
        map.put("jdkInstallVersion", "");
        map.put("tomcatNewVersion", "");
        map.put("jdkNewVersion", "");
        return map;

    }


}
