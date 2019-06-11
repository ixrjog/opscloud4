package com.sdg.cmdb.controller;


import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @RequestMapping(value = "/server/ecs/get", method = RequestMethod.GET)
    @ResponseBody
    public Object getEcs(@RequestParam String authToke) {
        return apiService.getEcsStatus(authToke);
    }

    @RequestMapping(value = "/user/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getUser(@RequestParam String ak,@RequestParam String username) {
        return apiService.getUser(ak,username);
    }


    @RequestMapping(value = "/user/query", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult queryUser(@RequestParam String ak,@RequestBody UserDO userDO) {
        return apiService.getUser(ak,userDO);
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult updateUser(@RequestParam String ak,@RequestBody UserVO userVO) {
        return apiService.updateUser(ak,userVO);
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult createUser(@RequestParam String ak,@RequestBody UserVO userVO) {
        return apiService.createUser(ak,userVO);
    }

    @RequestMapping(value = "/user/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delUser(@RequestParam String ak,@RequestParam String username) {
        return apiService.delUser(ak,username);
    }


}
