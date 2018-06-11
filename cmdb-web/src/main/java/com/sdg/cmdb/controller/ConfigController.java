package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.config.*;
import com.sdg.cmdb.domain.configCenter.ConfigCenterDO;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxxiao on 2016/10/20.
 */
@Controller
@RequestMapping("config")
public class ConfigController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @Resource
    private ConfigService configService;

    @Resource
    private ConfigCenterService configCenterService;

    /**
     * 属性分页数据
     *
     * @param proName
     * @param groupId
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/property", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getPropertyPage(@RequestParam String proName, @RequestParam long groupId,
                                      @RequestParam int page, @RequestParam int length) {
        return new HttpResult(configService.getConfigPropertyPage(proName, groupId, page, length));
    }

    /**
     * 新增 or 更新属性
     *
     * @param propertyDO
     * @return
     */
    @RequestMapping(value = "/property/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult savePropertyItem(@RequestBody ConfigPropertyDO propertyDO) {
        BusinessWrapper<Boolean> wrapper = configService.saveConfigProperty(propertyDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除指定的属性
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/property/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delPropertyItem(@RequestParam long id) {
        BusinessWrapper<Boolean> wrapper = configService.delConfigProperty(id);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 属性组分页数据
     *
     * @param groupName
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/property/group", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getPropertyGroupPage(@RequestParam String groupName, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(configService.getConfigPropertyGroupPage(groupName, page, length));
    }

    /**
     * 新增 or 更新属性组
     *
     * @param groupDO
     * @return
     */
    @RequestMapping(value = "/property/group/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult savePropertyGroup(@RequestBody ConfigPropertyGroupDO groupDO) {
        BusinessWrapper<Boolean> wrapper = configService.saveConfigPropertyGroup(groupDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除指定的属性组
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/property/group/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delPropertyGroupItem(@RequestParam long id) {
        BusinessWrapper<Boolean> wrapper = configService.delConfigPropertyGroup(id);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 新增 or 更新属性组数据
     *
     * @param groupPropertiesVO
     * @return
     */
    @RequestMapping(value = "/propertygroup/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult savePropertyGroup(@RequestBody ServerGroupPropertiesVO groupPropertiesVO) {
        BusinessWrapper<Boolean> wrapper = configService.saveServerPropertyGroup(groupPropertiesVO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除属性组数据
     *
     * @param serverId
     * @param groupId
     * @param propertyGroupId
     * @return
     */
    @RequestMapping(value = "/propertygroup/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delPropertyGroup(@RequestParam long serverId, @RequestParam long groupId, @RequestParam long propertyGroupId) {
        ServerGroupPropertiesDO serverGroupPropertiesDO = new ServerGroupPropertiesDO();
        serverGroupPropertiesDO.setServerId(serverId);
        serverGroupPropertiesDO.setGroupId(groupId);
        serverGroupPropertiesDO.setPropertyGroupId(propertyGroupId);

        BusinessWrapper<Boolean> wrapper = configService.delServerPropertyGroup(serverGroupPropertiesDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 获取文件组分页数据
     *
     * @param groupName
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/fileGroup/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getFileGroup(@RequestParam String groupName, @RequestParam int page, @RequestParam int length) {
        TableVO<List<ConfigFileGroupDO>> tableVO = configService.getConfigFileGroupPage(groupName, page, length);
        return new HttpResult(tableVO);
    }

    /**
     * 保存 or 更新文件组信息
     *
     * @param configFileGroupDO
     * @return
     */
    @RequestMapping(value = "/fileGroup/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveFileGroup(@RequestBody ConfigFileGroupDO configFileGroupDO) {
        BusinessWrapper<Boolean> wrapper = configService.saveConfigFileGroup(configFileGroupDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除指定id的文件组信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/fileGroup/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delFileGroup(@RequestParam long id) {
        BusinessWrapper<Boolean> wrapper = configService.delConfigFileGroup(id);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }


    /**
     * 保存服务器同步配置
     *
     * @param configFileCopyVO
     * @return
     */
    @RequestMapping(value = "/fileCopy/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveFileCopy(@RequestBody ConfigFileCopyVO configFileCopyVO) {
        return new HttpResult(configService.saveFileCopy(configFileCopyVO));
    }


    /**
     * 保存服务器同步后远程执行Script配置
     *
     * @param configFileCopyDoScriptDO
     * @return
     */
    @RequestMapping(value = "/fileCopy/script/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveFileCopyScript(@RequestBody ConfigFileCopyDoScriptDO configFileCopyDoScriptDO) {
        return new HttpResult(configService.saveFileCopyScript(configFileCopyDoScriptDO));
    }

    /**
     * 保存服务器同步后远程执行Script详情页
     *
     * @param groupName
     * @return
     */
    @RequestMapping(value = "/fileCopy/script/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getFileCopyScriptPage(@RequestParam String groupName, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(configService.getFileCopyScriptPage(groupName, page, length));
    }


    /**
     * 查询服务器同步配置
     *
     * @param groupName
     * @return
     */
    @RequestMapping(value = "/fileCopy/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryFileCopy(@RequestParam String groupName) {
        return new HttpResult(configService.queryFileCopy(groupName));
    }

    /**
     * 删除服务器同步配置
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/fileCopy/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult queryFileCopy(@RequestParam long id) {
        return new HttpResult(configService.delFileCopy(id));
    }

    /**
     * 获取文件分页数据
     *
     * @param configFileDO
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/file/query", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult getFile(@RequestBody ConfigFileDO configFileDO, @RequestParam int page, @RequestParam int length) {
        TableVO<List<ConfigFileVO>> tableVO = configService.getConfigFilePage(configFileDO, page, length);
        return new HttpResult(tableVO);
    }

    /**
     * 保存 or 更新文件组信息
     *
     * @param configFileDO
     * @return
     */
    @RequestMapping(value = "/file/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveFile(@RequestBody ConfigFileDO configFileDO) {
        BusinessWrapper<Boolean> wrapper = configService.saveConfigFile(configFileDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除指定id的文件组信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/file/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delFile(@RequestParam long id) {
        BusinessWrapper<Boolean> wrapper = configService.delConfigFile(id);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 生成本地文件（此接口很重要）
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/file/create", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult createFile(@RequestParam long id) {
        BusinessWrapper<Boolean> wrapper = configService.createConfigFile(id);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 按文件组查询不重复的文件路径
     *
     * @param fileGroupId
     * @return
     */
    @RequestMapping(value = "/file/queryPath", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryFilePath(@RequestParam long fileGroupId) {
        return new HttpResult(configService.queryFilePath(fileGroupId));
    }

    /**
     * 查询Getway配置的用户目录
     *
     * @return
     */
    @RequestMapping(value = "/file/getGetwayPath", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryFilePath() {
        return new HttpResult(configService.getGetwayPath());
    }


    /**
     * 执行命令
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/file/invoke", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult invokeFileCmd(@RequestParam long id) {
        BusinessWrapper<String> wrapper = configService.invokeConfigFileCmd(id);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 预览本地文件
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/file/launch", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult launchConfigFile(@RequestParam long id) {
        BusinessWrapper<String> wrapper = configService.launchConfigFile(id);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }


    /**
     * 配置中心详情页
     *
     * @return
     */
    @RequestMapping(value = "/center/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getConfigCenterPage(@RequestParam String item, @RequestParam String itemGroup, @RequestParam String env, @RequestParam int page, @RequestParam int length) {
        TableVO<List<ConfigCenterDO>> tableVO = configCenterService.getConfigCenterPage(item, itemGroup, env, page, length);
        return new HttpResult(tableVO);
    }


    /**
     * 获取配置中心配置项
     *
     * @return
     */
    @RequestMapping(value = "/center/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getConfigCenterPage(@RequestParam String itemGroup, @RequestParam String env) {
        HashMap<String, ConfigCenterDO> map = configCenterService.getConfigCenterItemGroup(itemGroup, env);
        return new HttpResult(map);
    }


    /**
     * 更新配置中心配置组缓存
     *
     * @return
     */
    @RequestMapping(value = "/center/refreshCache", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult refreshConfigCenter(@RequestParam String itemGroup, @RequestParam String env) {
        BusinessWrapper<Boolean> wrapper = configCenterService.refreshCache(itemGroup);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 新增 or 更新属性
     *
     * @param configCenterDO
     * @return
     */
    @RequestMapping(value = "/center/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveConfigCenterItem(@RequestBody ConfigCenterDO configCenterDO) {
        BusinessWrapper<Boolean> wrapper = configCenterService.saveConfigCenter(configCenterDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除属性
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/center/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delConfigCenterItem(@RequestParam long id) {
        BusinessWrapper<Boolean> wrapper = configCenterService.delConfigCenter(id);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 保存属性
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/center/update", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult delConfigCenterSave(@RequestBody HashMap<String, ConfigCenterDO> map) {
        BusinessWrapper<Boolean> wrapper = configCenterService.updateConfigCenter(map);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

}
