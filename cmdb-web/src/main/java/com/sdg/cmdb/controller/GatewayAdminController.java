package com.sdg.cmdb.controller;


import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.service.GatewayAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/gatewayadmin")
public class GatewayAdminController {

    @Autowired
    private GatewayAdminService gatewayAdminService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryGatewayAdminPage(@RequestParam String serverGroupName,
                                            @RequestParam String appName,
                                            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(gatewayAdminService.queryGatewayAdminPage(serverGroupName, appName, page, length));
    }

    @RequestMapping(value = "/sync", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult syncGatewayAdmin() {
        return new HttpResult(gatewayAdminService.syncGatewayAdmin());
    }

    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult preview(@RequestParam long id, @RequestParam int envType) {
        return new HttpResult(gatewayAdminService.preview(id,envType));
    }

    @RequestMapping(value = "/appSet", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult setApp(@RequestParam long id, @RequestParam int envType) {
        return new HttpResult(gatewayAdminService.appSet(id,envType));
    }

    @RequestMapping(value = "/appServerSet", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult setAppServer(@RequestParam long id, @RequestParam int envType) {
        return new HttpResult(gatewayAdminService.appServerSet(id,envType));
    }

    @RequestMapping(value = "/batchSet", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult batchSet() {
        return new HttpResult(gatewayAdminService.batchSet());
    }

    @RequestMapping(value = "/appServerList", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult listAppServer(@RequestParam long id, @RequestParam int envType) {
        return new HttpResult(gatewayAdminService.appServerList(id,envType));
    }

    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delGatewayadmin(@RequestParam long id) {
        return new HttpResult(gatewayAdminService.delGatewayadmin(id));
    }
}
