package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailQuery;
import com.sdg.cmdb.service.IPService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 16/9/11.
 */
@Controller
@RequestMapping("/ip")
public class IPController {

    @Resource
    private IPService ipService;

    /**
     * 查询指定条件的ip集合信息
     * @param detailQuery
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult queryIPPage(
            @RequestBody IPDetailQuery detailQuery, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ipService.getIPDetailPage(detailQuery, page, length));
    }

    /**
     * 保存 or 更新ip信息
     * @param ipDetailDO
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveIP(@RequestBody IPDetailDO ipDetailDO) {
        BusinessWrapper<Boolean> wrapper = ipService.saveGroupIP(ipDetailDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除指定id的ip信息
     * @param ipId
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult delIP(@RequestParam long ipId) {
        BusinessWrapper<Boolean> wrapper = ipService.delGroupIP(ipId);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 检查指定段的ip是否已经被使用
     * @param groupId
     * @param ip
     * @param serverId
     * @return
     */
    @RequestMapping(value = "/use/check", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult checkIPUse(@RequestParam long groupId, @RequestParam String ip, @RequestParam long serverId) {
        BusinessWrapper<Boolean> wrapper = ipService.checkIPHasUse(groupId, ip, serverId);

        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }
}
