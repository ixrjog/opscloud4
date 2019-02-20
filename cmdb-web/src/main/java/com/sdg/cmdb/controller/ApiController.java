package com.sdg.cmdb.controller;


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
}
