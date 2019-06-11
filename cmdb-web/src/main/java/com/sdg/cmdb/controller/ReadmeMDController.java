package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.readmeMD.ReadmeMDDO;
import com.sdg.cmdb.service.ReadmeMDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/readmeMD")
public class ReadmeMDController {

    @Autowired
    private ReadmeMDService mdService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getMD(@RequestParam String mdKey) {
        return new HttpResult(
                mdService.getMD(mdKey)
        );
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult getMD(@RequestBody ReadmeMDDO readmeMDDO) {
        return new HttpResult(
                mdService.saveMD(readmeMDDO)
        );
    }

}
