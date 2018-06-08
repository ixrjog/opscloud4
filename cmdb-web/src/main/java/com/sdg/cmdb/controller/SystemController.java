package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.systems.SystemDO;
import com.sdg.cmdb.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 16/10/8.
 */
@Controller
@RequestMapping("/system")
public class SystemController {

    @Resource
    private SystemService systemService;

    /**
     * 系统新增 or 更新
     * @param systemDO
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveSystem(@RequestBody SystemDO systemDO) {
        BusinessWrapper<Boolean> wrapper = systemService.saveSystem(systemDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除指定的系统
     * @param systemId
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delSystem(@RequestParam long systemId) {
        BusinessWrapper<Boolean> wrapper = systemService.delSystem(systemId);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 模糊查询匹配名称的系统列表
     * @param systemName
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult querySystem(@RequestParam String systemName, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(systemService.querySystems(systemName, page, length));
    }
}
