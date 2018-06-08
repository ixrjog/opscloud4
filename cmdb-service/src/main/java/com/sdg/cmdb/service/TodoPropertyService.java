package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.todo.TodoDetailVO;


public interface TodoPropertyService {

  void invokeTodoNewProject(TodoDetailVO todoDetailVO);


  void invokeTodoCmdbRole(TodoDetailVO todoDetailVO);

  /**
   * SCM权限申请工单
   * @param todoDetailVO
   */
  void invokeTodoScm(TodoDetailVO todoDetailVO);


  void invokeTodoTomcatVersion(TodoDetailVO todoDetailVO);

}
