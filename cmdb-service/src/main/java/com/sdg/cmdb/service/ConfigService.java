package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.config.*;
import com.sdg.cmdb.domain.configCenter.ConfigCenterDO;
import com.sdg.cmdb.domain.server.CreateEcsVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerVO;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxxiao on 2016/10/20.
 */
public interface ConfigService {

    /**
     * 获取合适条件分页属性数据
     *
     * @param proName
     * @param groupId
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ConfigPropertyVO>> getConfigPropertyPage(String proName, long groupId, int page, int length);

    /**
     * 新增 or 更新属性数据
     *
     * @param propertyDO
     * @return
     */
    BusinessWrapper<Boolean> saveConfigProperty(ConfigPropertyDO propertyDO);

    /**
     * 删除指定id的属性数据
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> delConfigProperty(long id);

    /**
     * 获取合适条件分页属性组数据
     *
     * @param groupName
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ConfigPropertyGroupDO>> getConfigPropertyGroupPage(String groupName, int page, int length);

    /**
     * 保存 or 更新属性组数据
     *
     * @param groupDO
     * @return
     */
    BusinessWrapper<Boolean> saveConfigPropertyGroup(ConfigPropertyGroupDO groupDO);

    /**
     * 删除指定id的属性组数据
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> delConfigPropertyGroup(long id);

    /**
     * 获取合适条件分页服务器组属性组数据
     *
     * @param groupId
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ServerGroupPropertiesVO>> getGroupPropertyPageByGroupId(long groupId, int page, int length);

    /**
     * 获取合适条件分页服务器组属性组数据
     *
     * @param groupId
     * @param serverId
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ServerGroupPropertiesVO>> getGroupPropertyPageByServerId(long groupId, long serverId, int page, int length);

    /**
     * 新增 or 更新服务器组属性组数据
     *
     * @param groupPropertiesVO
     * @return
     */
    BusinessWrapper<Boolean> saveServerPropertyGroup(ServerGroupPropertiesVO groupPropertiesVO);

    /**
     * 删除指定服务器组&属性组数据
     *
     * @param serverGroupPropertiesDO
     * @return
     */
    BusinessWrapper<Boolean> delServerPropertyGroup(ServerGroupPropertiesDO serverGroupPropertiesDO);

    /**
     * 查询指定服务器组id的属性组
     *
     * @param groupId
     * @return
     */
    List<ConfigPropertyGroupDO> getPropertyGroupByGroupId(long groupId);

    /**
     * 生成指定服务器组&属性组的配置文件
     *
     * @param serverGroupId
     * @param propertyGroupId
     * @return
     */
    BusinessWrapper<String> createServerPropertyFile(long serverGroupId, long propertyGroupId);

    /**
     * 预览指定服务器组&属性组的配置文件
     *
     * @param serverGroupId
     * @param propertyGroupId
     * @return
     */
    String previewServerPropertyFile(long serverGroupId, long propertyGroupId);

    /**
     * 加载指定服务器组&属性组的本地属性配置文件
     *
     * @param serverGroupId
     * @param propertyGroupId
     * @return
     */
    BusinessWrapper<String> launchServerPropertyFile(long serverGroupId, long propertyGroupId);


    /**
     * 创建getway_host.conf文件
     */
    void buildGetwayHost();

    /**
     * 获取文件组
     *
     * @param groupName
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ConfigFileGroupDO>> getConfigFileGroupPage(String groupName, int page, int length);

    /**
     * 保存 or 更新文件组信息
     *
     * @param configFileGroupDO
     * @return
     */
    BusinessWrapper<Boolean> saveConfigFileGroup(ConfigFileGroupDO configFileGroupDO);

    /**
     * 删除指定id的文件组信息
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> delConfigFileGroup(long id);

    /**
     * 获取文件
     *
     * @param configFileDO
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ConfigFileVO>> getConfigFilePage(ConfigFileDO configFileDO, int page, int length);


    BusinessWrapper<Boolean> saveFileCopy(ConfigFileCopyVO configFileCopyVO);

    TableVO<List<ConfigFileCopyDoScriptVO>> getFileCopyScriptPage(String groupName ,int page, int length);

    BusinessWrapper<Boolean>  saveFileCopyScript(ConfigFileCopyDoScriptDO configFileCopyDoScriptDO);



    List<ConfigFileCopyVO> queryFileCopy(String groupName);

    BusinessWrapper<Boolean> delFileCopy(long id);

    /**
     * 保存 or 更新文件信息
     *
     * @param configFileDO
     * @return
     */
    BusinessWrapper<Boolean> saveConfigFile(ConfigFileDO configFileDO);

    /**
     * 删除指定id的文件信息
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> delConfigFile(long id);

    /**
     * 创建 or 更新指定id的文件
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> createConfigFile(long id);


    /**
     * 查询不重复的文件路径
     *
     * @param fileGroupid
     * @return
     */
    List<ConfigFileDO> queryFilePath(long fileGroupid);


    /**
     * 查询Getway配置的用户目录
     * @return
     */
    String getGetwayPath();

    /**
     * 创建 or 更新指定名称的文件
     *
     * @param fileName
     * @return
     */
    boolean createConfigFileByName(String fileName);

    /**
     * 创建并更新指定名称的配置文件(废弃)
     *
     * @param fileName
     * @return
     */
    void createAndInvokeConfigFile(String fileName, int envType);


    /**
     * 用户相关的配置文件自动同步和执行script
     */
    void invokeUserConfig();

    /**
     * 执行命令
     *
     * @param id
     * @return
     */
    BusinessWrapper<String> invokeConfigFileCmd(long id);


    /**
     * 预览本地内容
     *
     * @param id
     * @return
     */
    BusinessWrapper<String> launchConfigFile(long id);


    /**
     * 获取服务器组的属性
     *
     * @param serverGroupDO
     * @param key
     * @return
     */
    String acqConfigByServerGroupAndKey(ServerGroupDO serverGroupDO, String key);


    /**
     * 保存服务器组的属性
     *
     * @param serverGroupDO
     * @param key
     * @param value
     * @return
     */
    boolean saveConfigServerGroupValue(ServerGroupDO serverGroupDO, String key, String value);

    /**
     * 获取服务器属性
     *
     * @param serverDO
     * @param key
     * @return
     */


    String acqConfigByServerAndKey(ServerDO serverDO, String key);

    /**
     * 新增服务器的配置文件变更
     */
    void invokeServerConfig(ServerVO serverVO);

    /**
     * 新增服务器的配置文件变更
     *
     * @param serverGroupId
     * @param envType
     */
    void invokeServerConfig(long serverGroupId, int envType);

    /**
     * 删除服务器的配置文件变更
     */
    void invokeDelServerConfig(long serverGroupId, int envType);

    /**
     * 新增配置项的配置文件变更
     */
    void invokeConfig(long configPropertyGroupId, long serverGroupId, boolean isAddConfig);

    /**
     * 保存Getway主机配置文件
     * @param file
     * @return
     */
    boolean saveGetwayHostFileConfigFile(String file);

    /**
     * 获取ansible所有主机列表文件
     * @return
     */
    String getAnsibleHostsAllPath();


}
