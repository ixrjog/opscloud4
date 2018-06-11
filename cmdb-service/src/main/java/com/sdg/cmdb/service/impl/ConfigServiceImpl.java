package com.sdg.cmdb.service.impl;


import com.sdg.cmdb.dao.cmdb.AnsibleTaskDao;
import com.sdg.cmdb.dao.cmdb.ConfigDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import com.sdg.cmdb.domain.config.*;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.GetwayItemEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.PublicItemEnum;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerGroupUseTypeDO;
import com.sdg.cmdb.domain.server.ServerVO;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.service.control.configurationfile.ConfigurationFileControlService;
import com.sdg.cmdb.service.control.configurationfile.IptablsZookeeperService;
import com.sdg.cmdb.template.tomcat.TomcatSetenv;
import com.sdg.cmdb.util.CmdUtils;
import com.sdg.cmdb.util.IOUtils;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * Created by zxxiao on 2016/10/20.
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);

    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");

    public static final String CONFIG_FILE_SHADOWSOCKS = "shadowsocks.json";

    public static final String CONFIG_FILE_TOMCAT_RES = "tomcat_res_install_config.conf";

    public static final String ANSIBLE_HOSTS_ALL = "ansible_hosts_all";


    @Resource
    private SchedulerManager schedulerManager;

    @Resource
    private ConfigDao configDao;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ConfigurationFileControlService configurationFileControlService;

    @Resource
    private IptablsZookeeperService iptablsZookeeperService;

    @Resource
    private AnsibleTaskService ansibleTaskService;

    @Resource
    private AnsibleTaskDao ansibleTaskDao;

    @Resource
    private ConfigServerGroupService configServerGroupService;

    // 新增
    public final boolean ADD_CONFIG = true;

    // 删除
    public final boolean DEL_CONFIG = false;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;


    @Resource
    private ConfigCenterService configCenterService;


    private HashMap<String, String> getwayConfigMap;

    private HashMap<String, String> publicConfigMap;

    private HashMap<String, String> acqGetwayConfigMap() {
        if (getwayConfigMap != null) return getwayConfigMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.GETWAY.getItemKey());
    }

    private HashMap<String, String> acqPublicConfigMap() {
        if (publicConfigMap != null) return publicConfigMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.PUBLIC.getItemKey());
    }


    @Override
    public TableVO<List<ConfigPropertyVO>> getConfigPropertyPage(String proName, long groupId, int page, int length) {
        long size = configDao.getConfigSize(proName, groupId);
        List<ConfigPropertyDO> propertyDOList = configDao.getConfigPage(proName, groupId, page * length, length);

        List<ConfigPropertyVO> propertyVOList = new ArrayList<>();
        for (ConfigPropertyDO propertyDO : propertyDOList) {
            ConfigPropertyGroupDO groupDO = configDao.getConfigPropertyGroupById(propertyDO.getGroupId());
            ConfigPropertyVO propertyVO = new ConfigPropertyVO(propertyDO, groupDO);

            propertyVOList.add(propertyVO);
        }
        return new TableVO<>(size, propertyVOList);
    }

    @Override
    public BusinessWrapper<Boolean> saveConfigProperty(ConfigPropertyDO propertyDO) {
        try {
            if (propertyDO.getId() == 0) {
                configDao.addConfig(propertyDO);
            } else {
                configDao.updateConfig(propertyDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delConfigProperty(long id) {
        try {
            if (configDao.getServerGroupsByConfigId(id) == 0) {
                configDao.delConfig(id);
                return new BusinessWrapper<>(true);
            } else {
                return new BusinessWrapper<>(ErrorCode.configPropertyHasServer);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public TableVO<List<ConfigPropertyGroupDO>> getConfigPropertyGroupPage(String groupName, int page, int length) {
        long size = configDao.getConfigGroupSize(groupName);
        List<ConfigPropertyGroupDO> list = configDao.getConfigGroupPage(groupName, page * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public BusinessWrapper<Boolean> saveConfigPropertyGroup(ConfigPropertyGroupDO groupDO) {
        try {
            if (groupDO.getId() == 0) {
                configDao.addConfigGroup(groupDO);
            } else {
                configDao.updateConfigGroup(groupDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delConfigPropertyGroup(long id) {
        try {
            if (configDao.getConfigSize(null, id) == 0) {
                configDao.delConfigGroup(id);
                return new BusinessWrapper<>(true);
            } else {
                return new BusinessWrapper<>(ErrorCode.configPropertyGroupHasChildProperty);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public TableVO<List<ServerGroupPropertiesVO>> getGroupPropertyPageByGroupId(long groupId, int page, int length) {
        checkServerGroupProperty(groupId);
        long size = configDao.getServerPropertyGroupByGroupIdSize(groupId);
        List<Long> groupList = configDao.getServerPropertyGroupByGroupIdPage(groupId, page * length, length);

        List<ServerGroupPropertiesVO> propertiesVOList = new ArrayList<>();
        for (long tmpId : groupList) {
            ServerGroupPropertiesDO queryPropertyDO = new ServerGroupPropertiesDO();
            queryPropertyDO.setGroupId(tmpId);
            List<ServerGroupPropertiesDO> propertiesDOList = configDao.getServerGroupProperty(queryPropertyDO);

            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(tmpId);

            Map<Long, List<ConfigPropertyDO>> groupPropertyMap = new HashMap<>();
            for (ServerGroupPropertiesDO groupPropertiesDO : propertiesDOList) {
                ConfigPropertyDO propertyDO = configDao.getConfigPropertyById(groupPropertiesDO.getPropertyId());
                propertyDO.setProValue(groupPropertiesDO.getPropertyValue());

                List<ConfigPropertyDO> propertyDOList = groupPropertyMap.get(groupPropertiesDO.getPropertyGroupId());
                if (groupPropertyMap.containsKey(groupPropertiesDO.getPropertyGroupId())) {
                    propertyDOList.add(propertyDO);
                } else {
                    propertyDOList = new ArrayList<>();
                    propertyDOList.add(propertyDO);

                    groupPropertyMap.put(groupPropertiesDO.getPropertyGroupId(), propertyDOList);
                }
            }

            Map<Long, ConfigPropertyGroupDO> propertyGroupDOMap = new HashMap<>();
            for (Map.Entry<Long, List<ConfigPropertyDO>> entry : groupPropertyMap.entrySet()) {
                long key = entry.getKey();

                ConfigPropertyGroupDO propertyGroupDO = configDao.getConfigPropertyGroupById(key);

                propertyGroupDOMap.put(key, propertyGroupDO);
            }

            ServerGroupPropertiesVO propertiesVO = new ServerGroupPropertiesVO(serverGroupDO, propertyGroupDOMap, groupPropertyMap);
            propertiesVOList.add(propertiesVO);
        }

        return new TableVO<>(size, propertiesVOList);
    }

    @Override
    public TableVO<List<ServerGroupPropertiesVO>> getGroupPropertyPageByServerId(long groupId, long serverId, int page, int length) {
        long size = configDao.getServerPropertyGroupByServerIdSize(groupId, serverId);
        List<Long> serverList = configDao.getServerPropertyGroupByServerIdPage(groupId, serverId, page * length, length);
        List<ServerGroupPropertiesVO> propertiesVOList = new ArrayList<>();
        for (long tmpId : serverList) {
            ServerGroupPropertiesDO queryPropertyDO = new ServerGroupPropertiesDO();
            queryPropertyDO.setServerId(tmpId);
            List<ServerGroupPropertiesDO> propertiesDOList = configDao.getServerGroupProperty(queryPropertyDO);
            Map<Long, List<ConfigPropertyDO>> groupPropertyMap = new HashMap<>();

            for (ServerGroupPropertiesDO groupPropertiesDO : propertiesDOList) {
                ConfigPropertyDO propertyDO = configDao.getConfigPropertyById(groupPropertiesDO.getPropertyId());
                propertyDO.setProValue(groupPropertiesDO.getPropertyValue());

                List<ConfigPropertyDO> propertyDOList = groupPropertyMap.get(groupPropertiesDO.getPropertyGroupId());
                if (groupPropertyMap.containsKey(groupPropertiesDO.getPropertyGroupId())) {
                    propertyDOList.add(propertyDO);
                } else {
                    propertyDOList = new ArrayList<>();
                    propertyDOList.add(propertyDO);

                    groupPropertyMap.put(groupPropertiesDO.getPropertyGroupId(), propertyDOList);
                }
            }

            Map<Long, ConfigPropertyGroupDO> propertyGroupDOMap = new HashMap<>();
            for (Map.Entry<Long, List<ConfigPropertyDO>> entry : groupPropertyMap.entrySet()) {
                long key = entry.getKey();

                ConfigPropertyGroupDO propertyGroupDO = configDao.getConfigPropertyGroupById(key);

                propertyGroupDOMap.put(key, propertyGroupDO);
            }
            ServerDO serverDO = serverDao.getServerInfoById(tmpId);
            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(serverDO.getServerGroupId());
            ServerGroupPropertiesVO propertiesVO = new ServerGroupPropertiesVO(serverDO, propertyGroupDOMap, groupPropertyMap);
            propertiesVO.setServerGroupDO(serverGroupDO);

            propertiesVOList.add(propertiesVO);
        }
        return new TableVO<>(size, propertiesVOList);
    }

    public void checkServerGroupProperty(long groupId) {
        if (groupId <= 0) return;
        List<Long> propertyGroupIds = new ArrayList<Long>();
        List<ServerGroupPropertiesDO> list = configDao.getServerGroupPropertyByGroupId(groupId);
        Map<Long, List<ConfigPropertyDO>> ConfigPropertyDOMap = new HashMap<>();
        for (ServerGroupPropertiesDO serverGroupPropertiesDO : list) {
            if (ConfigPropertyDOMap.containsKey(serverGroupPropertiesDO.getPropertyGroupId())) continue;
            propertyGroupIds.add(serverGroupPropertiesDO.getPropertyGroupId());


            List<ConfigPropertyDO> configPropertyDOList = configDao.getConfigPropertyByGroupId(serverGroupPropertiesDO.getPropertyGroupId());

            ConfigPropertyDOMap.put(serverGroupPropertiesDO.getPropertyGroupId(), configPropertyDOList);
        }

        for (long propertyGroupId : propertyGroupIds) {

            List<ConfigPropertyDO> configPropertyDOList = ConfigPropertyDOMap.get(propertyGroupId);

            for (ConfigPropertyDO configPropertyDO : configPropertyDOList) {

                ServerGroupPropertiesDO serverGroupPropertiesDO = configDao.getServerGroupPropertyData(groupId, configPropertyDO.getId());

                if (serverGroupPropertiesDO == null) {
                    ServerGroupPropertiesDO groupPropertiesDO = new ServerGroupPropertiesDO();
                    groupPropertiesDO.setGroupId(groupId);
                    groupPropertiesDO.setPropertyId(configPropertyDO.getId());
                    groupPropertiesDO.setPropertyValue(configPropertyDO.getProValue());
                    groupPropertiesDO.setPropertyGroupId(configPropertyDO.getGroupId());
                    configDao.addServerPropertyData(groupPropertiesDO);

                }
            }
        }

    }


    @Override
    public BusinessWrapper<Boolean> saveServerPropertyGroup(ServerGroupPropertiesVO groupPropertiesVO) {
        return transactionTemplate.execute(status -> {
            try {
                //删掉旧的
                ServerGroupPropertiesDO serverGroupPropertiesDO = new ServerGroupPropertiesDO();
                serverGroupPropertiesDO.setServerId(groupPropertiesVO.getServerDO() == null ? 0 : groupPropertiesVO.getServerDO().getId());
                serverGroupPropertiesDO.setGroupId(groupPropertiesVO.getServerGroupDO() == null ? 0 : groupPropertiesVO.getServerGroupDO().getId());
                serverGroupPropertiesDO.setPropertyGroupId(groupPropertiesVO.getPropertyGroupDO().getId());
                configDao.delServerPropertyData(serverGroupPropertiesDO);

                //直接插入新的
                for (ConfigPropertyDO propertyDO : groupPropertiesVO.getPropertyDOList()) {
                    ServerGroupPropertiesDO groupPropertiesDO = new ServerGroupPropertiesDO();
                    groupPropertiesDO.setGroupId(groupPropertiesVO.getServerGroupDO() == null ? 0 : groupPropertiesVO.getServerGroupDO().getId());
                    groupPropertiesDO.setServerId(groupPropertiesVO.getServerDO() == null ? 0 : groupPropertiesVO.getServerDO().getId());
                    groupPropertiesDO.setPropertyGroupId(groupPropertiesVO.getPropertyGroupDO().getId());
                    groupPropertiesDO.setPropertyId(propertyDO.getId());
                    groupPropertiesDO.setPropertyValue(propertyDO.getProValue());

                    configDao.addServerPropertyData(groupPropertiesDO);
                }
                // 新增服务器组属性变更配置并执行命令
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                status.setRollbackOnly();
                return new BusinessWrapper<>(ErrorCode.serverFailure);
            }

            try {
                invokeConfig(groupPropertiesVO.getPropertyGroupDO().getId(), groupPropertiesVO.getServerGroupDO().getId(), ADD_CONFIG);
            } catch (Exception e) {
            }

            return new BusinessWrapper<>(true);
        });
    }

    @Override
    public BusinessWrapper<Boolean> delServerPropertyGroup(ServerGroupPropertiesDO serverGroupPropertiesDO) {
        try {
            configDao.delServerPropertyData(serverGroupPropertiesDO);
            // 按服务器删除属性需要获取serverGroupId

            if (serverGroupPropertiesDO.getGroupId() == 0 && serverGroupPropertiesDO.getServerId() != 0) {
                ServerDO serverDO = serverDao.getServerInfoById(serverGroupPropertiesDO.getServerId());
                serverGroupPropertiesDO.setGroupId(serverDO.getServerGroupId());
            }
            invokeConfig(serverGroupPropertiesDO.getPropertyGroupId(), serverGroupPropertiesDO.getGroupId(), DEL_CONFIG);

            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public List<ConfigPropertyGroupDO> getPropertyGroupByGroupId(long groupId) {
        return configDao.getPropertyGroupByGroupId(groupId);
    }

    @Override
    public BusinessWrapper<String> createServerPropertyFile(long serverGroupId, long propertyGroupId) {
        HashMap<String, String> configMap = acqPublicConfigMap();
        String deployPath = configMap.get(PublicItemEnum.DEPLOY_PATH.getItemKey());
        String tomcatConfigName = configMap.get(PublicItemEnum.TOMCAT_CONFIG_NAME.getItemKey());


        TomcatSetenv tomcatSetenv = buildTomcatSetenv(serverGroupId, propertyGroupId);
        String fileContent = tomcatSetenv.toBody();

        try {
            IOUtils.writeFile(fileContent, tomcatSetenv.getPath(deployPath, tomcatConfigName));
            return new BusinessWrapper<>(fileContent);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public String previewServerPropertyFile(long serverGroupId, long propertyGroupId) {
        TomcatSetenv tomcatSetenv = buildTomcatSetenv(serverGroupId, propertyGroupId);
        String fileContent = tomcatSetenv.toBody();

        return fileContent;
    }

    @Override
    public BusinessWrapper<String> launchServerPropertyFile(long serverGroupId, long propertyGroupId) {

        HashMap<String, String> configMap = acqPublicConfigMap();
        String deployPath = configMap.get(PublicItemEnum.DEPLOY_PATH.getItemKey());
        String tomcatConfigName = configMap.get(PublicItemEnum.TOMCAT_CONFIG_NAME.getItemKey());

        TomcatSetenv tomcatSetenv = buildTomcatSetenv(serverGroupId, propertyGroupId);

        try {
            String fileContent = IOUtils.readFile(tomcatSetenv.getPath(deployPath, tomcatConfigName));
            return new BusinessWrapper<>(fileContent);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    private TomcatSetenv buildTomcatSetenv(long serverGroupId, long propertyGroupId) {
        ServerGroupPropertiesDO queryPropertyDO = new ServerGroupPropertiesDO();
        queryPropertyDO.setGroupId(serverGroupId);
        queryPropertyDO.setPropertyGroupId(propertyGroupId);

        List<ServerGroupPropertiesDO> list = configDao.getServerGroupProperty(queryPropertyDO);

        List<ConfigPropertyDO> configPropertyVOList = new ArrayList<>();
        for (ServerGroupPropertiesDO propertiesDO : list) {
            ConfigPropertyDO propertyDO = configDao.getConfigPropertyById(propertiesDO.getPropertyId());
            propertyDO.setProValue(propertiesDO.getPropertyValue());

            configPropertyVOList.add(propertyDO);
        }
        TomcatSetenv tomcatSetenv = TomcatSetenv.builder(configPropertyVOList);

        return tomcatSetenv;
    }

    @Override
    public void buildGetwayHost() {

        HashMap<String, String> configMap = acqGetwayConfigMap();
        String configFile = configMap.get(GetwayItemEnum.GETWAY_HOST_CONF_FILE.getItemKey());
        //System.err.println(configFile);
        String body = configurationFileControlService.build(ConfigurationFileControlService.nameGetway, 0);
        IOUtils.writeFile(body, configFile);
    }

    @Override
    public TableVO<List<ConfigFileGroupDO>> getConfigFileGroupPage(String groupName, int page, int length) {
        long size = configDao.getConfigFileGroupSize(groupName);
        List<ConfigFileGroupDO> list = configDao.getConfigFileGroupPage(groupName, page * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public BusinessWrapper<Boolean> saveConfigFileGroup(ConfigFileGroupDO configFileGroupDO) {
        try {
            if (configFileGroupDO.getId() == 0) {
                configDao.addConfigFileGroup(configFileGroupDO);
            } else {
                configDao.updateConfigFileGroup(configFileGroupDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delConfigFileGroup(long id) {
        try {
            ConfigFileDO configFileDO = new ConfigFileDO();
            configFileDO.setFileGroupId(id);
            if (configDao.getConfigFileSize(configFileDO) == 0) {
                configDao.delConfigFileGroup(id);
                return new BusinessWrapper<>(true);
            } else {
                return new BusinessWrapper<>(ErrorCode.configFileGroupHasUsed);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public TableVO<List<ConfigFileVO>> getConfigFilePage(ConfigFileDO configFileDO, int page, int length) {
        long size = configDao.getConfigFileSize(configFileDO);
        List<ConfigFileDO> fileDOList = configDao.getConfigFilePage(configFileDO, page * length, length);

        List<ConfigFileVO> fileVOList = new ArrayList<>();
        for (ConfigFileDO fileDO : fileDOList) {
            ConfigFileGroupDO fileGroupDO = configDao.getConfigFileGroupById(fileDO.getFileGroupId());
            ConfigFileVO fileVO = new ConfigFileVO(fileDO, fileGroupDO);
            ServerGroupUseTypeDO useType = serverGroupDao.getServerGroupUseTypeByUseType(fileDO.getUseType());
            fileVO.setUseTypeDO(useType);
            fileVOList.add(fileVO);
        }
        return new TableVO<>(size, fileVOList);
    }

    @Override
    public TableVO<List<ConfigFileCopyDoScriptVO>> getFileCopyScriptPage(String groupName, int page, int length) {
        long size = configDao.getConfigFileCopyDoScriptSize(groupName);
        List<ConfigFileCopyDoScriptDO> list = configDao.getConfigFileCopyDoScriptPage(groupName, page * length, length);
        List<ConfigFileCopyDoScriptVO> voList = new ArrayList<>();
        for (ConfigFileCopyDoScriptDO doScriptDO : list) {
            TaskScriptDO script = ansibleTaskDao.getTaskScript(doScriptDO.getTaskScriptId());
            ConfigFileCopyDO copyDO = configDao.getConfigFileCopy(doScriptDO.getCopyId());

            ServerDO serverDO = serverDao.getServerInfoById(copyDO.getServerId());
            serverDO.setServerName(serverDO.acqServerName());
            ConfigFileCopyDoScriptVO doScriptVO = new ConfigFileCopyDoScriptVO(doScriptDO, serverDO, script);
            voList.add(doScriptVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public BusinessWrapper<Boolean> saveFileCopy(ConfigFileCopyVO configFileCopyVO) {
        try {
            if (configFileCopyVO.getId() == 0) {
                configDao.addConfigFileCopy(new ConfigFileCopyDO(configFileCopyVO));
            } else {
                configDao.updateConfigFileCopy(new ConfigFileCopyDO(configFileCopyVO));
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> saveFileCopyScript(ConfigFileCopyDoScriptDO configFileCopyDoScriptDO) {
        try {
            if (configFileCopyDoScriptDO.getId() == 0) {
                configDao.addConfigFileCopyDoScript(configFileCopyDoScriptDO);
            } else {
                configDao.updateConfigFileCopyDoScript(configFileCopyDoScriptDO);
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delFileCopy(long id) {
        try {
            configDao.delConfigFileCopy(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }


    @Override
    public List<ConfigFileCopyVO> queryFileCopy(String groupName) {
        List<ConfigFileCopyDO> list = configDao.queryConfigFileCopyByGroupName(groupName);
        List<ConfigFileCopyVO> voList = new ArrayList<>();
        for (ConfigFileCopyDO configFileCopyDO : list) {
            ConfigFileCopyVO copyVO = new ConfigFileCopyVO(configFileCopyDO);
            copyVO.setConfigFileGroupDO(configDao.getConfigFileGroupById(copyVO.getGroupId()));
            ServerDO serverDO = serverDao.getServerInfoById(copyVO.getServerId());
            serverDO.setServerName(serverDO.acqServerName());
            ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());
            copyVO.setServerGroupDO(serverGroupDO);
            copyVO.setServerDO(serverDO);
            voList.add(copyVO);
        }
        return voList;
    }

    @Override
    public BusinessWrapper<Boolean> saveConfigFile(ConfigFileDO configFileDO) {
        try {
            if (configFileDO.getId() == 0) {
                configDao.addConfigFile(configFileDO);
            } else {
                configDao.updateConfigFile(configFileDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delConfigFile(long id) {
        try {
            configDao.delConfigFile(id);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> createConfigFile(long id) {
        ConfigFileDO configFileDO = configDao.getConfigFileById(id);

        if (configFileDO == null) {
            return new BusinessWrapper<>(ErrorCode.configFileNotExist);
        }
        // 必须是系统配置文件
        if (configFileDO.getFileType() == ConfigFileDO.systemConfigFile) {
            coreLogger.info(SessionUtils.getUsername() + " createConfigFile for:" + id);
            String value = configurationFileControlService.build(configFileDO);
            IOUtils.writeFile(value, configFileDO.getFilePath() + "/" + configFileDO.getFileName());
        } else {
            coreLogger.info(SessionUtils.getUsername() + " createConfigFile for:" + id);
            IOUtils.writeFile(configFileDO.getFileContent(), configFileDO.getFilePath() + "/" + configFileDO.getFileName());
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public List<ConfigFileDO> queryFilePath(long fileGroupid) {
        return configDao.queryFilePathByFileGroupId(fileGroupid);
    }


    @Override
    public String getGetwayPath() {
        HashMap<String, String> configMap = acqGetwayConfigMap();
        String getway_user_conf_path = configMap.get(GetwayItemEnum.GETWAY_USER_CONF_PATH.getItemKey());
        File tempFile = new File(getway_user_conf_path.trim());
        String fileName = tempFile.getName();
        return getway_user_conf_path.replace(fileName, "");

    }

    @Override
    public boolean createConfigFileByName(String fileName) {
        List<ConfigFileDO> fileList = configDao.getConfigFileByFileName(fileName);
        if (fileList == null || fileList.size() == 0) return false;
        schedulerManager.registerJob(() -> {
            for (ConfigFileDO configFileDO : fileList) {
                createConfigFile(configFileDO.getId());
            }
        });
        return true;
    }


    @Override
    public void createAndInvokeConfigFile(String fileName, int envType) {
        schedulerManager.registerJob(() -> {
            List<ConfigFileDO> files = configDao.getConfigFileByFileNameAndEnvType(fileName, envType);
            boolean result;
            for (ConfigFileDO configFileDO : files) {
                createConfigFile(configFileDO.getId());
                invokeConfigFileCmd(configFileDO.getId());
            }
        });
    }

    @Override
    public void invokeUserConfig() {
        // List<ConfigFileCopyDO> list = configDao.queryConfigFileCopyByGroupName(ConfigurationFileControlService.GROUP_SHADOWSOCKS);
       // ansibleTaskService.doScriptByCopyByGroup(ConfigurationFileControlService.GROUP_SHADOWSOCKS);
        ansibleTaskService.doFileCopyByFileGroupName(ConfigurationFileControlService.GROUP_SHADOWSOCKS);

    }

    /**
     * 增加新服务器的配置文件变更
     */
    @Override
    public void invokeServerConfig(ServerVO serverVO) {
        if (!checkInvokeEnv()) return;
        invokeServerConfig(serverVO.getServerGroupDO().getId(), serverVO.getEnvType());
    }

    public void invokeServerConfig(long serverGroupId, int envType) {
        //if (!checkInvokeEnv()) return;
        // 终端堡垒机全局配置
        List<ConfigFileDO> getwayConfigFileDOList = configDao.getConfigFileByFileName("getway_host.conf");
        schedulerManager.registerJob(() -> {
            for (ConfigFileDO configFileDO : getwayConfigFileDOList)
                createConfigFile(configFileDO.getId());
        });

        //// TODO ansible主机文件
        ConfigFileGroupDO configFileGroupDO = configDao.getConfigFileGroupByName(ConfigurationFileControlService.GROUP_ANSIBLE);
        List<ConfigFileDO> ansibleConfigList = configDao.queryConfigFileByGroupId(configFileGroupDO.getId());
        for (ConfigFileDO andibleConfig : ansibleConfigList) {
            createConfigFile(andibleConfig.getId());
        }
        // 远程配置文件同步
        ansibleTaskService.doFileCopyByFileGroupName(ConfigurationFileControlService.GROUP_ANSIBLE);

        // 变更nginx配置 & 变更dubbo白名单
        //ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverGroupId);
        //if (serverGroupDO == null) return;
        //if (serverGroupDO.getUseType() != ServerGroupDO.UseTypeEnum.webservice.getCode()) return;
        //变更nginx配置
        //invokeNginxConfig(serverGroupDO, envType);
        //变更dubbo白名单
        //invokeDubboIptablesConfig(serverGroupDO, envType);
    }


    @Override
    public void invokeDelServerConfig(long serverGroupId, int envType) {
        //if (!checkInvokeEnv()) return;

        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverGroupId);
        //变更nginx
        if (serverGroupDO.getUseType() == ServerGroupDO.UseTypeEnum.webservice.getCode()) {
            invokeNginxConfig(serverGroupDO, envType);
            invokeDubboIptablesConfig(serverGroupDO, envType);
        }
        //变更主机列表相关配置
        invokeServerConfig(serverGroupId, envType);
    }

    /**
     * 变更nginx
     */
    private void invokeNginxConfig(ServerGroupDO serverGroupDO, int envType) {
        List<ConfigFileDO> nginxConfigList = new ArrayList<ConfigFileDO>();
        // 检查是否变更nginx配置
        if (configServerGroupService.isbuildLocation(serverGroupDO)) {
            ConfigFileGroupDO configFileGroupDO = configDao.getConfigFileGroupByName("web-www");
            if (configFileGroupDO == null) return;
            nginxConfigList.addAll(configDao.getConfigFileByGroupAndFileNameAndEnvType(configFileGroupDO.getId(), "upstream_default.conf", envType));
            nginxConfigList.addAll(configDao.getConfigFileByGroupAndFileNameAndEnvType(configFileGroupDO.getId(), "upstream_back.default.conf", envType));
            nginxConfigList.addAll(configDao.getConfigFileByGroupAndFileNameAndEnvType(configFileGroupDO.getId(), "location_default.conf", envType));
        }
        // 检查是否变更nginx(manage)配置
        List<ConfigFileDO> nginxManageConfigList = new ArrayList<ConfigFileDO>();
        if (configServerGroupService.isBuildManageLocation(serverGroupDO)) {
            //ConfigPropertyGroupDO configPropertyGroupDO = configDao.getConfigGroupByName("web-manage");
            //if (configPropertyGroupDO == null) return;
            nginxManageConfigList.addAll(configDao.getConfigFileByFileNameAndEnvType("upstream_default.conf", envType));
            nginxManageConfigList.addAll(configDao.getConfigFileByFileNameAndEnvType("upstream_back.default.conf", envType));
            nginxManageConfigList.addAll(configDao.getConfigFileByFileNameAndEnvType("location_default.conf", envType));
        }

        schedulerManager.registerJob(() -> {
            for (ConfigFileDO configFileDO : nginxConfigList) {
                createConfigFile(configFileDO.getId());
            }
            for (ConfigFileDO configFileDO : nginxManageConfigList) {
                createConfigFile(configFileDO.getId());
            }
            if (nginxConfigList.size() != 0)
                invokeConfigFileCmd(nginxConfigList.get(0).getId());

            // prod环境需要分开执行
            if (envType == ServerDO.EnvTypeEnum.prod.getCode())
                if (nginxManageConfigList.size() != 0)
                    invokeConfigFileCmd(nginxManageConfigList.get(0).getId());
        });
    }

    /**
     * 变更dubbo白名单
     */
    private void invokeDubboIptablesConfig(ServerGroupDO serverGroupDO, int envType) {
        if (iptablsZookeeperService.isbuildIptablesDubbo(serverGroupDO)) {
            ConfigFileGroupDO configFileGroupDO = configDao.getConfigFileGroupByName("iptables-dubbo");
            if (configFileGroupDO == null) return;
            List<ConfigFileDO> configList = configDao.getConfigFileByGroupAndFileNameAndEnvType(configFileGroupDO.getId(), "iptables", envType);
            schedulerManager.registerJob(() -> {
                for (ConfigFileDO configFileDO : configList)
                    createConfigFile(configFileDO.getId());
                for (ConfigFileDO configFileDO : configList)
                    invokeConfigFileCmd(configFileDO.getId());
            });
        }
    }

    /**
     * 新增配置项变更配置文件
     */
    public void invokeConfig(long configPropertyGroupId, long serverGroupId, boolean isAddConfig) {

        if (!checkInvokeEnv()) return;

        if (configPropertyGroupId == 0) return;
        ConfigPropertyGroupDO configPropertyGroupDO = configDao.getConfigPropertyGroupById(configPropertyGroupId);
        try {
            String cfgGroupName = configPropertyGroupDO.getGroupName();
            // Tomcat配置文件 & Tomcat安装资源
            if (cfgGroupName.equalsIgnoreCase("Tomcat")) {
                schedulerManager.registerJob(() -> {
                    // 生成tomcatSetenv配置
                    if (isAddConfig)
                        createServerPropertyFile(serverGroupId, configPropertyGroupId);
                    List<ConfigFileDO> configFileDOList = configDao.getConfigFileByFileName("tomcat_res_install_config.conf");
                    for (ConfigFileDO configFileDO : configFileDOList)
                        createConfigFile(configFileDO.getId());
                    // 生成持续集成部署目录
                    ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverGroupId);
                    String projectName = configServerGroupService.queryProjectName(serverGroupDO);

                    HashMap<String, String> configMap = acqPublicConfigMap();
                    String iptables_webservice_path = configMap.get(PublicItemEnum.IPTABLES_WEBSERVICE_PATH.getItemKey());

                    IOUtils.writeFile("# CMDB CREATE", iptables_webservice_path + "/" + projectName + "/iptables");
                });
            }
            // InterfaceCI配置文件
            if (cfgGroupName.equalsIgnoreCase("InterfaceCI")) {
                schedulerManager.registerJob(() -> {
                    //创建 持续集成接口配置
                    List<ConfigFileDO> configFileDOList = configDao.getConfigFileByFileName("interface_ci.conf");
                    for (ConfigFileDO configFileDO : configFileDOList)
                        createConfigFile(configFileDO.getId());
                    // 生成持续集成部署目录
                    ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverGroupId);
                    String projectName = configServerGroupService.queryProjectName(serverGroupDO);
                });
            }

            // Getway配置文件
            if (cfgGroupName.equalsIgnoreCase("Getway")) {
                schedulerManager.registerJob(() -> {
                    List<ConfigFileDO> configFileDOList = configDao.getConfigFileByFileName("getway_host.conf");
                    for (ConfigFileDO configFileDO : configFileDOList)
                        createConfigFile(configFileDO.getId());
                });
            }

            // IptablesDubbo配置文件
            if (cfgGroupName.equalsIgnoreCase("IptablesDubbo")) {
                schedulerManager.registerJob(() -> {
                    List<ConfigFileDO> configFileDOList = configDao.getConfigFileByFileName("iptables");
                    for (ConfigFileDO configFileDO : configFileDOList)
                        createConfigFile(configFileDO.getId());
                });
            }

            // Nginx配置文件
            if (cfgGroupName.equalsIgnoreCase("Nginx")) {
                // 暂时不自动配置
            }


        } catch (Exception e) {
            logger.error("新增配置项变更配置文件错误");
        }
    }

    @Override
    public BusinessWrapper<String> invokeConfigFileCmd(long id) {
        ConfigFileDO configFileDO = configDao.getConfigFileById(id);
        if (configFileDO == null) {
            return new BusinessWrapper<>(ErrorCode.configFileNotExist);
        }
        coreLogger.info(SessionUtils.getUsername() + " invokeConfigFileCmd for:" + id);
        coreLogger.info("invokeConfigFileCmd:" + configFileDO.getInvokeCmd());
        String invokeStr = CmdUtils.runCmd(configFileDO.getInvokeCmd());
        return new BusinessWrapper<>(invokeStr);
    }


    @Override
    public BusinessWrapper<String> launchConfigFile(long id) {
        ConfigFileDO configFileDO = configDao.getConfigFileById(id);
        if (configFileDO == null) {
            return new BusinessWrapper<>(ErrorCode.configFileNotExist);
        }
        try {
            String value = IOUtils.readFile(configFileDO.getFilePath() + "/" + configFileDO.getFileName());
            return new BusinessWrapper<>(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure.getCode(), "文件不存在!");
        }
    }

    @Override
    public String acqConfigByServerGroupAndKey(ServerGroupDO serverGroupDO, String key) {
        ConfigPropertyDO confPropertyDO = configDao.getConfigPropertyByName(key);
        if (confPropertyDO == null) return null;
        ServerGroupPropertiesDO serverGroupPropertiesDO = configDao.getServerGroupPropertyData(serverGroupDO.getId(), confPropertyDO.getId());
        if (serverGroupPropertiesDO == null) return confPropertyDO.getProValue();
        if (serverGroupPropertiesDO.getPropertyValue() != null && !serverGroupPropertiesDO.getPropertyValue().isEmpty()) {
            return serverGroupPropertiesDO.getPropertyValue();
        }
        return confPropertyDO.getProValue();
    }

    @Override
    public boolean saveConfigServerGroupValue(ServerGroupDO serverGroupDO, String key, String value) {
        ConfigPropertyDO confPropertyDO = configDao.getConfigPropertyByName(key);
        if (confPropertyDO == null) return false;
        ServerGroupPropertiesDO serverGroupPropertiesDO = configDao.getServerGroupPropertyData(serverGroupDO.getId(), confPropertyDO.getId());
        if (serverGroupPropertiesDO == null) return false;

        serverGroupPropertiesDO.setPropertyValue(value);
        try {
            configDao.updateServerGroupProperty(serverGroupPropertiesDO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String acqConfigByServerAndKey(ServerDO serverDO, String key) {
        ConfigPropertyDO confPropertyDO = configDao.getConfigPropertyByName(key);
        if (confPropertyDO == null) return null;
        ServerGroupPropertiesDO serverGroupPropertiesDO = configDao.getServerPropertyData(serverDO.getId(), confPropertyDO.getId());
        if (serverGroupPropertiesDO == null) return confPropertyDO.getProValue();
        if (serverGroupPropertiesDO.getPropertyValue() != null && !serverGroupPropertiesDO.getPropertyValue().isEmpty()) {
            return serverGroupPropertiesDO.getPropertyValue();
        }
        return confPropertyDO.getProValue();
    }

    @Override
    public boolean saveGetwayHostFileConfigFile(String filePath) {
        ConfigFileGroupDO fileGroupDO = configDao.getConfigFileGroupByName(ConfigurationFileControlService.GROUP_GETWAY);
        if (fileGroupDO == null) return false;
        List<ConfigFileDO> list = configDao.queryConfigFileByGroupId(fileGroupDO.getId());
        try {
            for (ConfigFileDO configFileDO : list) {
                File tempFile = new File(filePath.trim());
                String fileName = tempFile.getName();
                String path = filePath.replace(fileName, "");
                configFileDO.setFileName(fileName);
                configFileDO.setFilePath(path);
                configDao.updateConfigFile(configFileDO);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getAnsibleHostsAllPath() {
        //configDao.getConfigFileByFileName("ansible_hosts_all");
        ConfigFileGroupDO fileGroupDO = configDao.getConfigFileGroupByName(ConfigurationFileControlService.GROUP_ANSIBLE);
        if (fileGroupDO == null) return "";
        List<ConfigFileDO> list = configDao.queryConfigFileByGroupId(fileGroupDO.getId());
        for (ConfigFileDO fileDO : list) {
            if (fileDO.getFileName().equals(ANSIBLE_HOSTS_ALL)) {
                return fileDO.getFilePath() + fileDO.getFileName();
            }
        }
        return "";
    }


    // 判断环境是否支持变更配置
    private boolean checkInvokeEnv() {
        // online
        if (ConfigCenterServiceImpl.DEFAULT_ENV.equalsIgnoreCase(invokeEnv))
            return true;
        // daily
        if (invokeEnv.equalsIgnoreCase("daily"))
            return true;
        return false;
    }


}
