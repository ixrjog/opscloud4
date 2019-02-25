package com.sdg.cmdb.controller;



import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.service.JumpserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/jumpserver")
public class JumpserverController {

    @Autowired
    private JumpserverService jumpserverService;


    @RequestMapping(value = "/authAdmin", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult authAdmin(@RequestParam long userId) {
        return new HttpResult(jumpserverService.authAdmin(userId));
    }

    /**
     * 查询系统账户
     * @param name
     * @return
     */
    @RequestMapping(value = "/assetsSystemuser/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryAssetsSystemuser(@RequestParam String name) {
        return new HttpResult(jumpserverService.queryAssetsSystemuser(name));
    }

    @RequestMapping(value = "/assetsAdminuser/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryAssetsAdminuser(@RequestParam String name) {
        return new HttpResult(jumpserverService.queryAssetsAdminuser(name));
    }

    /**
     * 同步资产
     * @return
     */
    @RequestMapping(value = "/assets/sync", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult syncAssets() {
        return new HttpResult(jumpserverService.syncAssets());
    }

    /**
     * 同步用户
     * @return
     */
    @RequestMapping(value = "/users/sync", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult syncUsers() {
        return new HttpResult(jumpserverService.syncUsers());
    }

    /**
     * 查询当前状态
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getJumpserver() {
        return new HttpResult(jumpserverService.getJumpserver());
    }


    @RequestMapping(value = "/assetsSystemuser/save", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult saveAssetsSystemuser(@RequestParam String id) {
        return new HttpResult(jumpserverService.saveAssetsSystemuser(id));
    }

    @RequestMapping(value = "/assetsAdminuser/save", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult saveAssetsAdminuser(@RequestParam String id) {
        return new HttpResult(jumpserverService.saveAssetsAdminuser(id));
    }


}
