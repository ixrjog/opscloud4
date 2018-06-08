package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerGroupUseTypeDO;
import com.sdg.cmdb.service.ConfigService;
import com.sdg.cmdb.service.ServerGroupService;
import com.sdg.cmdb.service.ServerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 16/9/1.
 */
@Controller
@RequestMapping("/servergroup")
public class ServerGroupController {

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private ServerService serverService;

    @Resource
    private ConfigService configService;

    /**
     * 查询服务器组的分页数据
     *
     * @param page
     * @param length
     * @param name
     * @param useType
     * @return
     */
    @RequestMapping(value = "/query/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getServerGroupPage(@RequestParam int page, @RequestParam int length,
                                         @RequestParam String name, @RequestParam int useType) {
        return new HttpResult(serverGroupService.queryServerGroupPage(page, length, name, useType));
    }

    /**
     * 按服务器组名称查询
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/query/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getServerGroup(
            @RequestParam String name) {
        return new HttpResult(serverGroupService.queryServerGroupByName(name));
    }


    /**
     * 查询服务器组的分页数据
     *
     * @param page
     * @param length
     * @param name
     * @param useType
     * @return
     */
    @RequestMapping(value = "/project/query/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getProjectServerGroupPage(@RequestParam int page, @RequestParam int length,
                                                @RequestParam String name, @RequestParam int useType) {
        return new HttpResult(serverGroupService.queryProjectServerGroupPage(page, length, name, useType));
    }


    /**
     * 堡垒机服务器组分页数据查询
     *
     * @param name
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/keybox/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getBoxServerGroupPage(@RequestParam String name,
                                            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(serverGroupService.queryKeyboxServerGroupPage(name, page, length));
    }


    /**
     * 更新服务器组信息
     *
     * @param serverGroupDO
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult updateServerGroup(@RequestBody ServerGroupDO serverGroupDO) {
        return new HttpResult(serverGroupService.updateServerGroupInfo(serverGroupDO));
    }

    /**
     * 删除指定的服务器组信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delServerGroup(@RequestParam long id) {
        BusinessWrapper<Boolean> wrapper = serverGroupService.delServerGroupInfo(id);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 服务器组绑定指定的ip组
     *
     * @param serverGroupId
     * @param ipGroupId
     * @param ipType
     * @return
     */
    @RequestMapping(value = "/ipgroup/add", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult addIPGroup(@RequestParam long serverGroupId, @RequestParam long ipGroupId, @RequestParam int ipType) {
        BusinessWrapper<Boolean> wrapper = serverGroupService.addIPGroup(serverGroupId, ipGroupId, ipType);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 服务器组解绑指定的ip组
     *
     * @param serverGroupId
     * @param ipGroupId
     * @return
     */
    @RequestMapping(value = "/ipgroup/del", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult delIPGroup(@RequestParam long serverGroupId, @RequestParam long ipGroupId) {
        BusinessWrapper<Boolean> wrapper = serverGroupService.delIPGroup(serverGroupId, ipGroupId);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 查询指定条件的服务器组属性组数据
     *
     * @param groupId
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/propertygroup/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryServerGroupPropertyGroupPage(@RequestParam long groupId, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(configService.getGroupPropertyPageByGroupId(groupId, page, length));
    }

    /**
     * 生成指定服务器组&属性组的属性配置文件
     *
     * @param serverGroupId
     * @param propertyGroupId
     * @return
     */
    @RequestMapping(value = "/propertygroup/create", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult createServerGroupPropertyGroup(@RequestParam long serverGroupId, @RequestParam long propertyGroupId) {
        BusinessWrapper<String> wrapper = configService.createServerPropertyFile(serverGroupId, propertyGroupId);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 预览指定服务器组&属性组的属性配置文件
     *
     * @param serverGroupId
     * @param propertyGroupId
     * @return
     */
    @RequestMapping(value = "/propertygroup/preview", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult previewServerGroupPropertyGroup(@RequestParam long serverGroupId, @RequestParam long propertyGroupId) {
        String fileContent = configService.previewServerPropertyFile(serverGroupId, propertyGroupId);

        return new HttpResult(fileContent);
    }

    /**
     * 加载指定服务器组&属性组的本地属性配置文件
     *
     * @param serverGroupId
     * @param propertyGroupId
     * @return
     */
    @RequestMapping(value = "/propertygroup/launch", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult launchServerGroupPropertyGroup(@RequestParam long serverGroupId, @RequestParam long propertyGroupId) {
        BusinessWrapper<String> wrapper = configService.launchServerPropertyFile(serverGroupId, propertyGroupId);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 查询指定服务器组的服务器集合
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/servers", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryServerList(@RequestParam long groupId) {
        return new HttpResult(serverService.getServersByGroupId(groupId));
    }


    /**
     * 查询服务器组使用属性的分页数据
     *
     * @param typeName
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/useType/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getServerGroupUseTypePage(@RequestParam String typeName, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(serverGroupService.queryServerGroupUseTypePage(typeName, page, length));
    }


    /**
     * 查询服务器组使用属性列表
     * @return
     */
    @RequestMapping(value = "/useType/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getServerGroupUseTypePage() {
        return new HttpResult(serverGroupService.queryServerGroupUseType());
    }


    @RequestMapping(value = "/useType/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult getServerGroupUseTypePage(@RequestBody ServerGroupUseTypeDO serverGroupUseTypeDO) {
        return new HttpResult(serverGroupService.saveServerGroupUseType(serverGroupUseTypeDO));
    }

    @RequestMapping(value = "/useType/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult getServerGroupUseTypePage(@RequestParam long id) {
        return new HttpResult(serverGroupService.delServerGroupUseType(id));
    }

}
