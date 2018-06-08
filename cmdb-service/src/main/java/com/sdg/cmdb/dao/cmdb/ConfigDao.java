package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.config.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 2016/10/20.
 */
@Component
public interface ConfigDao {

    /**
     * 查询指定条件的属性组数目
     *
     * @param groupName
     * @return
     */
    long getConfigGroupSize(@Param("groupName") String groupName);

    /**
     * 查询指定条件的属性组分页数据
     *
     * @param groupName
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ConfigPropertyGroupDO> getConfigGroupPage(
            @Param("groupName") String groupName, @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);


    /**
     * 按名称查询
     *
     * @param groupName
     * @return
     */
    ConfigPropertyGroupDO getConfigGroupByName(@Param("groupName") String groupName);

    /**
     * 新增属性组
     *
     * @param groupDO
     * @return
     */
    int addConfigGroup(ConfigPropertyGroupDO groupDO);

    /**
     * 更新属性组
     *
     * @param groupDO
     * @return
     */
    int updateConfigGroup(ConfigPropertyGroupDO groupDO);

    /**
     * 删除属性组
     *
     * @param id
     * @return
     */
    int delConfigGroup(@Param("id") long id);

    /**
     * 获取合适条件下的属性数目
     *
     * @param proName
     * @param groupId
     * @return
     */
    long getConfigSize(@Param("proName") String proName, @Param("groupId") long groupId);

    /**
     * 获取合适条件下的属性分页数据
     *
     * @param proName
     * @param groupId
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ConfigPropertyDO> getConfigPage(
            @Param("proName") String proName, @Param("groupId") long groupId,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 新增属性
     *
     * @param propertyDO
     * @return
     */
    int addConfig(ConfigPropertyDO propertyDO);

    /**
     * 更新属性
     *
     * @param propertyDO
     * @return
     */
    int updateConfig(ConfigPropertyDO propertyDO);

    /**
     * 删除属性
     *
     * @param id
     * @return
     */
    int delConfig(@Param("id") long id);

    /**
     * 获取指定属性被依赖的服务器组数目
     *
     * @param propertyId
     * @return
     */
    long getServerGroupsByConfigId(@Param("propertyId") long propertyId);

    /**
     * 获取指定属性组id的属性组数据
     *
     * @param id
     * @return
     */
    ConfigPropertyGroupDO getConfigPropertyGroupById(@Param("id") long id);

    /**
     * 获取指定服务器组&属性组的总数
     *
     * @param groupId
     * @return
     */
    long getServerPropertyGroupByGroupIdSize(@Param("groupId") long groupId);

    /**
     * 获取指定服务器组&属性组的分页数据
     *
     * @param groupId
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<Long> getServerPropertyGroupByGroupIdPage(
            @Param("groupId") long groupId, @Param("pageStart") long pageStart,
            @Param("pageLength") int pageLength);

    /**
     * 获取指定服务器组&服务器的属性总数
     *
     * @param groupId
     * @param serverId
     * @return
     */
    long getServerPropertyGroupByServerIdSize(@Param("groupId") long groupId, @Param("serverId") long serverId);

    /**
     * 获取指定服务器组&服务器的属性的分页数据
     *
     * @param groupId
     * @param serverId
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<Long> getServerPropertyGroupByServerIdPage(
            @Param("groupId") long groupId, @Param("serverId") long serverId,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 获取指定条件下的服务器组&属性组的数据
     *
     * @param serverGroupPropertiesDO
     * @return
     */
    List<ServerGroupPropertiesDO> getServerGroupProperty(ServerGroupPropertiesDO serverGroupPropertiesDO);

    /**
     * 删除指定id的服务器组&属性组的数据
     *
     * @param serverGroupPropertiesDO
     * @return
     */
    int delServerPropertyData(ServerGroupPropertiesDO serverGroupPropertiesDO);

    /**
     * 新增服务器组&属性组的数据
     *
     * @param groupPropertiesDO
     * @return
     */
    int addServerPropertyData(ServerGroupPropertiesDO groupPropertiesDO);

    /**
     * 获取指定id的属性
     *
     * @param id
     * @return
     */
    ConfigPropertyDO getConfigPropertyById(@Param("id") long id);

    /**
     * 根据名称获取属性
     *
     * @param proName
     * @return
     */
    ConfigPropertyDO getConfigPropertyByName(@Param("proName") String proName);

    /**
     * 根据名称获取
     *
     * @param groupName
     * @return
     */
    ConfigPropertyGroupDO getConfigPropertyGroupByName(@Param("groupName") String groupName);

    /**
     * 查询指定服务器组id的属性组
     *
     * @param groupId
     * @return
     */
    List<ConfigPropertyGroupDO> getPropertyGroupByGroupId(@Param("groupId") long groupId);

    /**
     * 查询指定id的服务器&属性组的数据
     *
     * @param serverId
     * @param propertyId
     * @return
     */
    ServerGroupPropertiesDO getServerPropertyData(@Param("serverId") long serverId, @Param("propertyId") long propertyId);

    /**
     * 查询指定id的服务器组&属性组的数据
     *
     * @param groupId
     * @param propertyId
     * @return
     */
    ServerGroupPropertiesDO getServerGroupPropertyData(@Param("groupId") long groupId, @Param("propertyId") long propertyId);


    /**
     * 更新服务器组的配置项
     *
     * @param serverGroupPropertiesDO
     * @return
     */
    int updateServerGroupProperty(ServerGroupPropertiesDO serverGroupPropertiesDO);

    /**
     * 查询指定gourpId的所有配置数据
     *
     * @param groupId
     * @return
     */
    List<ServerGroupPropertiesDO> getServerGroupPropertyByGroupId(@Param("groupId") long groupId);


    /**
     * 根据组id获取所有属性条目
     *
     * @param groupId
     * @return
     */
    List<ConfigPropertyDO> getConfigPropertyByGroupId(@Param("groupId") long groupId);

    /**
     * 文件组新增
     *
     * @param configFileGroupDO
     * @return
     */
    int addConfigFileGroup(ConfigFileGroupDO configFileGroupDO);

    /**
     * 更新文件组
     *
     * @param configFileGroupDO
     * @return
     */
    int updateConfigFileGroup(ConfigFileGroupDO configFileGroupDO);

    /**
     * 删除文件组
     *
     * @param id
     * @return
     */
    int delConfigFileGroup(@Param("id") long id);

    /**
     * 查询文件组数目
     *
     * @param groupName
     * @return
     */
    long getConfigFileGroupSize(@Param("groupName") String groupName);

    /**
     * 查询文件组分页数据
     *
     * @param groupName
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ConfigFileGroupDO> getConfigFileGroupPage(
            @Param("groupName") String groupName, @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 获取指定id的文件组
     *
     * @param id
     * @return
     */
    ConfigFileGroupDO getConfigFileGroupById(@Param("id") long id);

    /**
     * 新增文件配置
     *
     * @param configFileDO
     * @return
     */
    int addConfigFile(ConfigFileDO configFileDO);

    /**
     * 更新文件配置
     *
     * @param configFileDO
     * @return
     */
    int updateConfigFile(ConfigFileDO configFileDO);

    /**
     * 删除文件配置
     *
     * @param id
     * @return
     */
    int delConfigFile(@Param("id") long id);

    /**
     * 获取文件数目
     *
     * @param configFileDO
     * @return
     */
    long getConfigFileSize(ConfigFileDO configFileDO);

    /**
     * 获取文件分页数据
     *
     * @param configFileDO
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ConfigFileDO> getConfigFilePage(
            @Param("configFileDO") ConfigFileDO configFileDO,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 获取指定id的文件
     *
     * @param id
     * @return
     */
    ConfigFileDO getConfigFileById(@Param("id") long id);

    /**
     * 获取指定名称的文件
     *
     * @param fileName
     * @return
     */
    List<ConfigFileDO> getConfigFileByFileName(@Param("fileName") String fileName);

    /**
     * 获取指定名称&环境类型的文件
     *
     * @param fileName
     * @return
     */
    List<ConfigFileDO> getConfigFileByFileNameAndEnvType(@Param("fileName") String fileName, @Param("envType") int envType);

    /**
     * 从组获取指定名称&环境类型的文件
     *
     * @param fileName
     * @return
     */
    List<ConfigFileDO> getConfigFileByGroupAndFileNameAndEnvType(@Param("fileGroupId") long fileGroupId, @Param("fileName") String fileName, @Param("envType") int envType);

    /**
     * 查询不重复的文件路径
     * @param fileGroupId
     * @return
     */
    List<ConfigFileDO> queryFilePathByFileGroupId(@Param("fileGroupId") long fileGroupId);

    /**
     * 按名称查询
     *
     * @param groupName
     * @return
     */
    ConfigFileGroupDO getConfigFileGroupByName(@Param("groupName") String groupName);


    List<ConfigFileDO> queryConfigFileByGroupId(@Param("fileGroupId") long fileGroupId);


    int addConfigFileCopy(ConfigFileCopyDO configFileCopyDO);

    int delConfigFileCopy(@Param("id") long id);

    ConfigFileCopyDO getConfigFileCopy(@Param("id") long id);

    int updateConfigFileCopy(ConfigFileCopyDO configFileCopyDO);

    List<ConfigFileCopyDO>   queryConfigFileCopyByGroupId(@Param("groupId") long groupId);

    List<ConfigFileCopyDO>  queryConfigFileCopyByGroupName(@Param("groupName") String groupName);



}