package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.ip.IPGroupSearchVO;
import com.sdg.cmdb.domain.ip.IPNetworkDO;
import com.sdg.cmdb.service.IPGroupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 16/9/9.
 */
@Controller
@RequestMapping("/ipgroup")
public class IPGroupController {

    @Resource
    private IPGroupService ipGroupService;

    /**
     * ip组按条件查询
     * @param searchVO
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult queryGroupPage(@RequestBody IPGroupSearchVO searchVO, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ipGroupService.getIPGroupPage(searchVO, page, length));
    }

    /**
     * 保存 or 更新IP组信息
     * @param ipNetworkDO
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveGroupInfo(@RequestBody IPNetworkDO ipNetworkDO) {
        return new HttpResult(ipGroupService.saveIPGroupInfo(ipNetworkDO));
    }

    /**
     * 删除指定IP组信息
     * @param ipGroupId
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult delGroupInfo(@RequestParam long ipGroupId) {
        return new HttpResult(ipGroupService.delIPGroupInfo(ipGroupId));
    }

    /**
     * 生成ip
     * @param ipGroupId
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult createGroupIPs(@RequestParam long ipGroupId) {
        BusinessWrapper<Integer> wrapper = ipGroupService.createIp(ipGroupId);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 查询指定服务器组的ip组集合
     * @param serverGroupId
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/serverGroup/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryByServerGroupId(@RequestParam long serverGroupId, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ipGroupService.queryIPGroupByServerGroupId(serverGroupId, page, length));
    }
}
