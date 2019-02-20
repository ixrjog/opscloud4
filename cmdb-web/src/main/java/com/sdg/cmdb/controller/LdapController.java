package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.service.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ldap")
public class LdapController {

    @Autowired
    private LdapService ldapService;

    @RequestMapping(value = "/group/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getLdapGroupPage(
            @RequestParam String cn, @RequestParam int groupType,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ldapService.getLdapGroupPage(cn, groupType, page, length));
    }

    @RequestMapping(value = "/group/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getLdapGroup(
            @RequestParam String cn) {
        return new HttpResult(ldapService.getLdapGroup(cn));
    }

    @RequestMapping(value = "/group/create", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult createLdapGroup(
            @RequestParam String cn,
            @RequestParam String content,
            @RequestParam int groupType) {
        return new HttpResult(ldapService.createLdapGroup(cn, content, groupType));
    }

    @RequestMapping(value = "/group/addUser", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addLdapGroupUser(
            @RequestParam String groupname,
            @RequestParam String username) {
        return new HttpResult(ldapService.addUserToGroup(groupname, username));
    }

    @RequestMapping(value = "/group/delUser", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delLdapGroupUser(
            @RequestParam String groupname,
            @RequestParam String username) {
        return new HttpResult(ldapService.delUserToGroup(groupname, username));
    }

    @RequestMapping(value = "/group/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delLdaapGroup(
            @RequestParam long id) {
        return new HttpResult(ldapService.delLdapGroup(id));
    }

}
