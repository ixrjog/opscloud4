package com.sdg.cmdb.service.impl;


import com.sdg.cmdb.dao.cmdb.ApiDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.server.ServerStatisticsDO;
import com.sdg.cmdb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {


    @Autowired
    private ApiDao apiDao;

    @Autowired
    private UserService userService;

    @Autowired
    private LdapService ldapService;

    @Autowired
    private  AuthService authService;

    @Autowired
    private EcsService ecsService;

    @Override
    public List<ServerStatisticsDO> getEcsStatus(String authToke) {
        List<ServerStatisticsDO> list = new ArrayList<>();
        if (!checkToke(authToke))
            return list;
        list.add(ecsService.statistics());
        return list;
    }

    /**
     * 查询用户
     *
     * @param authToke
     * @param username
     * @return
     */
    @Override
    public HttpResult getUser(String authToke, String username) {
        if (StringUtils.isEmpty(authToke) || apiDao.checkApiToke(authToke) == 0)
            return new HttpResult("999", "认证失败，没有携带有效的令牌!");
        UserDO userDO = userService.getUserDOByName(username);
        if (userDO == null) return new HttpResult(null);
        UserVO user = new UserVO(userDO, true);
        return new HttpResult(user);
    }

    /**
     * 查询用户
     * @param authToke
     * @param userDO
     * @return
     */
    @Override
    public HttpResult getUser(String authToke, UserDO userDO) {
        if (StringUtils.isEmpty(authToke) || apiDao.checkApiToke(authToke) == 0)
            return new HttpResult("999", "认证失败，没有携带有效的令牌!");
        UserDO user = userService.getUserByDO(userDO);
        if (user == null || user.getId() == 0) return new HttpResult(null);
        UserVO userVO =new UserVO(user, true);

        return new HttpResult(userVO);
    }

    @Override
    public HttpResult updateUser(String authToke, UserVO userVO) {
        if (StringUtils.isEmpty(authToke) || apiDao.checkApiToke(authToke) == 0)
            return new HttpResult("999", "认证失败，没有携带有效的令牌!");
        BusinessWrapper<Boolean> wrapper = ldapService.updateUserBase(userVO, true);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @Override
    public HttpResult createUser(String authToke, UserVO userVO) {
        if (StringUtils.isEmpty(authToke) || apiDao.checkApiToke(authToke) == 0)
            return new HttpResult("999", "认证失败，没有携带有效的令牌!");
        BusinessWrapper<Boolean> wrapper = authService.addUser(userVO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @Override
    public HttpResult delUser(String authToke, String username) {
        if (StringUtils.isEmpty(authToke) || apiDao.checkApiToke(authToke) == 0)
            return new HttpResult("999", "认证失败，没有携带有效的令牌!");
        BusinessWrapper<Boolean> wrapper = authService.delUser(username);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }


    private boolean checkToke(String authToke) {
        if (apiDao.checkApiToke(authToke) == 0)
            return false;
        return true;
    }


}
