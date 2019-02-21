package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.dns.DnsmasqDO;
import com.sdg.cmdb.service.DnsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/7/11.
 */
@Controller
@RequestMapping("/dns")
public class DnsController {

    @Resource
    private DnsService dnsService;

    @RequestMapping(value = "/dnsmasq/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getDnsmasqPage(@RequestParam String dnsItem,
                                     @RequestParam int page, @RequestParam int length) {
        return new HttpResult(dnsService.getDnsmasqPage(dnsItem, page, length));
    }

    @RequestMapping(value = "/dnsmasq/getConf", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getDnsmasqConf() {
        return new HttpResult(dnsService.getDnsmasqConf());
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
        BusinessWrapper<Boolean> wrapper = dnsService.saveDnsmasq(dnsmasqDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RequestMapping(value = "/dnsmasq/build", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult buildDnsmasqConf() {
        return new HttpResult(dnsService.buildDnsmasqConf());
    }

    @RequestMapping(value = "/dnsmasq/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delDnsmasq(@RequestParam long id) {
        return new HttpResult(dnsService.delDnsmasq(id));
    }


}
