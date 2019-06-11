package com.sdg.cmdb.service.jms.service;

import com.sdg.cmdb.service.jms.base.ApiConstants;
import com.sdg.cmdb.service.jms.model.User;
import com.sdg.cmdb.service.jms.model.Usergroup;

import java.util.Map;

public class JmsUsersService extends JmsBaseService {

    public JmsUsersService(String url, String token) {
        this.URL = url;
        this.TOKEN = token;
    }

    public JmsUsersService(String url, String username, String password) {
        super(url, username, password);
    }

    public Map<String, String> addUser(User user) {
        return super.add(user, ApiConstants.USERS);
    }

    public Map<String, String> updateUser(User user) {
        return super.update(user, ApiConstants.USERS, user.getId());
    }

    public Map<String, String> deleteUser(User user) {
        return super.delete(user, ApiConstants.USERS, user.getId());
    }

    public Map<String, String> queryUser(String id) {
        return super.query(id, ApiConstants.USERS);
    }

    public Map<String, String> addUserGroup(Usergroup usergroup) {
        return super.add(usergroup, ApiConstants.USERGROUPS);
    }

    public Map<String, String> updateUserGroup(Usergroup usergroup) {
        return super.update(usergroup, ApiConstants.USERGROUPS, usergroup.getId());
    }

    public Map<String, String> deleteUserGroup(Usergroup usergroup) {
        return super.delete(usergroup, ApiConstants.USERGROUPS, usergroup.getId());
    }

    public Map<String, String> queryUserGroup(String id) {
        return super.query(id, ApiConstants.USERGROUPS);
    }

    public Map<String, String> userPasswordReset(User user) {
        return super.updateX(user, ApiConstants.USER_PASSWORD_RESET, user.getId());
    }

    public Map<String, String> userPubkeyReset(User user) {
        return super.updatePubkey(user, ApiConstants.USER_PUBLICKEY_UPDATE, user.getId());
    }

}
