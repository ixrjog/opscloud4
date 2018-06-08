package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.server.CreateEcsVO;
import com.sdg.cmdb.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/6/6.
 */
@Controller
@RequestMapping("/server/template")
public class ServerTemplateController {

    @Resource
    private EcsService ecsService;

    @Resource
    private VmService vmService;

    @Resource
    private EcsCreateService ecsCreateService;

    /**
     * ecs扩容
     *
     * @param createEcsVO
     * @return
     */
    @RequestMapping(value = "/ecs/create", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult ecsCreate(@RequestBody CreateEcsVO createEcsVO) {
        return ecsCreateService.create(null, createEcsVO);
    }


    /**
     * 获取指定条件的ECS模版列表分页数据
     *
     * @param zoneId
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/ecs/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryEcsPage(@RequestParam String zoneId,
                                   @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ecsService.getEcsTemplatePage(zoneId, page, length));
    }

    /**
     * 获取指定条件的VM模版列表分页数据
     *
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/vm/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryVmPage(
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(vmService.getVmTemplatePage(page, length));
    }


}
