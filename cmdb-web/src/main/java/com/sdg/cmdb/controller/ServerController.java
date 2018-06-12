package com.sdg.cmdb.controller;

import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.esxi.HostDatastoreInfoVO;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by zxxiao on 16/9/6.
 */
@Controller
@RequestMapping("/server")
public class ServerController {

    @Resource
    private ServerService serverService;

    @Resource
    private VmService vmService;

    @Resource
    private EcsService ecsService;

    @Resource
    private ConfigService configService;

    @Resource
    private PhysicalServerService physicalServerService;

    @Resource
    private ServerDao serverDao;

    @Resource
    private EcsCreateService ecsCreateService;

    @Resource
    private ServerPerfService serverPerfService;

    /**
     * 获取指定条件的服务器列表分页数据
     *
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param queryIp
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryServerPage(@RequestParam long serverGroupId, @RequestParam String serverName,
                                      @RequestParam int useType, @RequestParam int envType, @RequestParam String queryIp,
                                      @RequestParam int page, @RequestParam int length) {
        return new HttpResult(serverService.getServerPage(serverGroupId, serverName, useType, envType, queryIp, page, length));
    }

    /**
     * 保存指定的serveritem
     *
     * @param serverVO
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveServer(@RequestBody ServerVO serverVO) {
        BusinessWrapper<Boolean> wrapper = serverService.saveServer(serverVO);
        // 变更配置文件
        configService.invokeServerConfig(serverVO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除指定的server
     *
     * @param serverId
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delServer(@RequestParam long serverId) {
        BusinessWrapper<Boolean> wrapper = serverService.delServerGroupServer(serverId);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 查询指定条件的服务器属性组数据
     *
     * @param groupId
     * @param serverId
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/propertygroup/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getPropertyGroup(@RequestParam long groupId, @RequestParam long serverId,
                                       @RequestParam int page, @RequestParam int length) {
        return new HttpResult(configService.getGroupPropertyPageByServerId(groupId, serverId, page, length));
    }

    /**
     * 获取指定条件的ECS服务器列表分页数据
     *
     * @param serverName
     * @param queryIp
     * @param status
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/vmPage", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryVmServerPage(@RequestParam String serverName, @RequestParam String queryIp, @RequestParam int status,
                                        @RequestParam int page, @RequestParam int length) {

        return new HttpResult(vmService.getVmServerPage(serverName, queryIp, status, page, length));
    }


    /**
     * 修改所有vm的名称(ip:name)
     *
     * @return
     */
    @RequestMapping(value = "/vmRename", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getVmRename() {
        return new HttpResult(vmService.rename());
    }


    /**
     * 获取指定条件的ECS服务器列表分页数据
     *
     * @param serverName
     * @param status
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/ecsPage", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryEcsServerPage(@RequestParam String serverName, @RequestParam String queryIp, @RequestParam int status,
                                         @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ecsService.getEcsServerPage(serverName, queryIp, status, page, length));
    }


    @RequestMapping(value = "/ecsRefresh", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getEcsRefresh() {
        BusinessWrapper<Boolean> wrapper = ecsService.ecsRefresh();
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RequestMapping(value = "/ecsSync", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getEcsSync(@RequestParam int type) {
        BusinessWrapper<Boolean> wrapper = ecsService.ecsSync(type);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 刷新vm_server表的原始数据(从vcsa获取最新信息)
     *
     * @return
     */
    @RequestMapping(value = "/vmRefresh", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getVmRefresh() {
        BusinessWrapper<Boolean> wrapper = vmService.vmRefresh();
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RequestMapping(value = "/ecsCheck", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getEcsCheck() {
        BusinessWrapper<Boolean> wrapper = ecsService.ecsCheck();
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }

    }

    @RequestMapping(value = "/vmCheck", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getVmCheck() {
        BusinessWrapper<Boolean> wrapper = vmService.vmCheck();
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 修改ECS状态
     */
    @RequestMapping(value = "/setStatus", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult setStatus(@RequestParam String insideIp) {
        BusinessWrapper<Boolean> wrapper = ecsService.setStatus(insideIp, EcsServerDO.statusDel);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RequestMapping(value = "/delEcs", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult delEcs(@RequestParam String insideIp) {
        BusinessWrapper<Boolean> wrapper = ecsService.delEcs(insideIp);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RequestMapping(value = "/delVm", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult delVm(@RequestParam String insideIp) {
        BusinessWrapper<Boolean> wrapper = vmService.delVm(insideIp);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RequestMapping(value = "/vmPowerOff", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult vmPowerOff(@RequestParam long id) {
        VmServerDO vmServerDO = serverDao.getVmServerById(id);
        BusinessWrapper<Boolean> wrapper = vmService.powerOff(vmServerDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RequestMapping(value = "/vmPowerOn", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult vmPowerOn(@RequestParam long id) {
        VmServerDO vmServerDO = serverDao.getVmServerById(id);
        BusinessWrapper<Boolean> wrapper = vmService.powerOn(vmServerDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RequestMapping(value = "/vmStatistics", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult vmStatistics() {
        return new HttpResult(vmService.statistics());

    }

    @RequestMapping(value = "/ecsStatistics", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult ecsStatistics() {
        return new HttpResult(ecsService.statistics());
    }

    /**
     * 获取指定条件的物理服务器列表分页数据
     *
     * @param serverName
     * @param useType
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/psPage", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryPhysicalServerPage(@RequestParam String serverName, @RequestParam int useType,
                                              @RequestParam int page, @RequestParam int length) {

        TableVO<List<PhysicalServerVO>> tableVO = physicalServerService.getPhysicalServerPage(serverName, useType, page, length);
        for (PhysicalServerVO physicalServerVO : tableVO.getData()) {
            if (physicalServerVO.getUseType() != PhysicalServerDO.UseTypeEnum.vm.getCode()) {
                ServerDO serverDO = new ServerDO();
                serverDO.setId(physicalServerVO.getServerId());
                ServerPerfVO serverPerfVO = serverPerfService.getCache(serverDO);
                physicalServerVO.setServerPerfVO(serverPerfVO);
            }
        }
        return new HttpResult(tableVO);
    }

    @RequestMapping(value = "/ps/esxiVms", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryEsxiVmsPage(@RequestParam String serverName) {
        TableVO<List<VmServerDO>> tableVO = vmService.getEsxiVmsPage(serverName);
        return new HttpResult(tableVO);
    }

    @RequestMapping(value = "/ps/esxiDatastores", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryEsxiDatastoresPage(@RequestParam String serverName) {
        TableVO<List<HostDatastoreInfoVO>> tableVO = vmService.getEsxiDatastoresPage(serverName);
        return new HttpResult(tableVO);
    }


    @RequestMapping(value = "/psStatistics", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult psStatistics() {
        return new HttpResult(physicalServerService.statistics());
    }


    /**
     * 设置upstream
     *
     * @param ip
     * @param action
     * @return
     */
    @RequestMapping(value = "/upstream", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult setUpstream(@RequestParam String ip,
                                  @RequestParam String action) {
        return new HttpResult(serverService.setUpstream(ip, action));
    }

    /**
     * 分配公网ip
     *
     * @param instanceId
     * @return
     */
    @RequestMapping(value = "/ecsAllocateIp", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult allocateIp(@RequestParam String instanceId) {
        return new HttpResult(ecsCreateService.allocateIpAddress(instanceId));
    }


    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult statusLogService() {
        return new HttpResult(serverService.status());
    }

}
