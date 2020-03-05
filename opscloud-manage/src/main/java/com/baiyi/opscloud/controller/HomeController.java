package com.baiyi.opscloud.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Api(tags = "首页")
public class HomeController {

    @RequestMapping("/home")
    public String index() {
        return "index.html";
    }
}
