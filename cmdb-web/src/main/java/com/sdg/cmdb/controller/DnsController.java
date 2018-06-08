package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.dns.DnsmasqDO;
import com.sdg.cmdb.domain.server.ServerVO;
import com.sdg.cmdb.service.ConfigService;
import com.sdg.cmdb.service.DnsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/7/11.
 */
@Controller
@RequestMapping("/dns")
public class DnsController {

    private static final Logger logger = LoggerFactory.getLogger(DnsController.class);

    @Resource
    private DnsService dnsService;

    @RequestMapping(value = "/dnsmasq/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getDnsmasqPage(@RequestParam long dnsGroupId, @RequestParam int itemType, @RequestParam String queryItemValue,
                                     @RequestParam int page, @RequestParam int length) {
        return new HttpResult(dnsService.getDnsmasqPage(dnsGroupId, itemType, queryItemValue, page, length));
    }

    /**
     * 保存指定的Dnsmasq
     *
     * @param dnsmasqDO
     * @return
     */
    @RequestMapping(value = "/dnsmasq/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveServer(@RequestBody DnsmasqDO dnsmasqDO) {
        if (dnsmasqDO.getItemType() != DnsmasqDO.ItemTypeEnum.system.getCode())
            dnsmasqDO.setItem(DnsmasqDO.ItemTypeEnum.getItemTypeName(dnsmasqDO.getItemType()));

        BusinessWrapper<Boolean> wrapper = dnsService.saveDnsmasq(dnsmasqDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RequestMapping(value = "/dnsmasq/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult getDnsmasqPage(@RequestParam long id) {
        return new HttpResult(dnsService.delDnsmasq(id));
    }


}
