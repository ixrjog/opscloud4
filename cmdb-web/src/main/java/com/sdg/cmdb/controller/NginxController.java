package com.sdg.cmdb.controller;


import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.nginx.EnvFileDO;
import com.sdg.cmdb.domain.nginx.NginxFile;
import com.sdg.cmdb.domain.nginx.VhostEnvDO;
import com.sdg.cmdb.domain.nginx.VhostVO;
import com.sdg.cmdb.service.NginxService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/nginx")
public class NginxController {


    @Resource
    private NginxService nginxService;

    @RequestMapping(value = "/vhost/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryVhostPage(@RequestParam String serverName,
                                     @RequestParam int page, @RequestParam int length) {
        return new HttpResult(
                nginxService.getVhostPage(serverName, page, length)
        );
    }

    @RequestMapping(value = "/vhost/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult saveVhost(@RequestParam long id) {
        return new HttpResult(
                nginxService.delVhost(id)
        );
    }

    @RequestMapping(value = "/vhost/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getVhost(@RequestParam long id) {
        return new HttpResult(
                nginxService.getVhost(id)
        );
    }

    @RequestMapping(value = "/vhost/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveVhost(@RequestBody VhostVO vhostVO) {
        return new HttpResult(
                nginxService.saveVhost(vhostVO)
        );
    }

    @RequestMapping(value = "/vhost/env/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveEnv(@RequestBody VhostEnvDO vhostEnvDO) {
        return new HttpResult(
                nginxService.saveVhostEnv(vhostEnvDO)
        );
    }

    @RequestMapping(value = "/vhost/env/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult saveEnv(@RequestParam long id) {
        return new HttpResult(
                nginxService.delVhostEnv(id)
        );
    }


    @RequestMapping(value = "/vhost/env/file/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveEnv(@RequestBody EnvFileDO envFileDO) {
        return new HttpResult(
                nginxService.saveEnvFile(envFileDO)
        );
    }


    /**
     * 预览文件
     *
     * @param id
     * @param type 0 动态生成  1 本地文件
     * @return
     */
    @RequestMapping(value = "/vhost/env/file/launch", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult launchEnvFile(@RequestParam long id, @RequestParam int type) {
        BusinessWrapper<NginxFile> wrapper = nginxService.launchEnvFile(id, type);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }


    /**
     * 生成配置文件
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/vhost/env/file/build", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult buildEnvFile(@RequestParam long id) {
        return new HttpResult(
                nginxService.buildEnvFile(id)
        );
    }


    /**
     * 增加服务器组
     *
     * @param vhostId
     * @param serverGroupId
     * @return
     */
    @RequestMapping(value = "/vhost/serverGroup/add", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addServerGroup(@RequestParam long vhostId, @RequestParam long serverGroupId) {
        return new HttpResult(
                nginxService.addServerGroup(vhostId, serverGroupId)
        );
    }


    /**
     * 删除服务器组
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/vhost/serverGroup/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delServerGroup(@RequestParam long id) {
        return new HttpResult(
                nginxService.delServerGroup(id)
        );
    }

    /**
     * 查询vhost下的服务器组
     *
     * @param vhostId
     * @return
     */
    @RequestMapping(value = "/vhost/serverGroup/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryServerGroup(@RequestParam long vhostId) {
        return new HttpResult(
                nginxService.queryServerGroup(vhostId)
        );
    }


}
