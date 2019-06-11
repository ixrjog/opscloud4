package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.server.ServerStatisticsDO;

import java.util.List;

public interface ApiService {

    List<ServerStatisticsDO> getEcsStatus(String authToke);

    HttpResult getUser(String authToke, String username);

    HttpResult getUser(String authToke, UserDO userDO);

    HttpResult updateUser(String authToke, UserVO userVO);

    HttpResult createUser(String authToke, UserVO userVO);

    HttpResult delUser(String authToke, String username);
}
