package com.sdg.cmdb.service.impl;



import com.sdg.cmdb.dao.cmdb.*;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ansibleTask.PlaybookLogDO;
import com.sdg.cmdb.domain.ansibleTask.PlaybookLogVO;
import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.config.*;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.service.configurationProcessor.AnsibleFileProcessorService;
import com.sdg.cmdb.service.configurationProcessor.NginxFileProcessorService;
import com.sdg.cmdb.service.control.configurationfile.ConfigurationFileControlService;

import com.sdg.cmdb.template.tomcat.TomcatSetenv;
import com.sdg.cmdb.util.*;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;


@Service
public class ConfigServiceImpl implements ConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);

    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");

    static public final int PLAYBOOK_DO_TYPE_SYSTEM = 0;

    static public final int PLAYBOOK_DO_TYPE_USER = 1;

    public static final String CONFIG_FILE_SHADOWSOCKS = "shadowsocks.json";

    public static final String ANSIBLE_HOSTS_ALL = "ansible_hosts_all";

    static public final String FILEGROUP_SS = "filegroup_ss";

    @Value("#{cmdb['ansible.scripts.path']}")
    private String ansibleScriptsPath;


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
    private AnsibleTaskService ansibleTaskService;

    @Resource
    private AnsibleTaskDao ansibleTaskDao;

    @Resource
    private ConfigServerGroupService configServerGroupService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnsibleFileProcessorService ansibleFileProcessorService;

    @Autowired
    private NginxFileProcessorService nginxFileProcessorService;


    // 新增
    public final boolean ADD_CONFIG = true;

    // 删除
    public final boolean DEL_CONFIG = false;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;


    @Resource
    private ConfigCenterService configCenterService;


//    private HashMap<String, String> publicConfigMap;
//
//    private HashMap<String, String> acqPublicConfigMap() {
//        if (publicConfigMap != null) return publicConfigMap;
//        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.PUBLIC.getItemKey());
//    }


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

//    @Override
//    public BusinessWrapper<String> createServerPropertyFile(long serverGroupId, long propertyGroupId) {
//        HashMap<String, String> configMap = acqPublicConfigMap();
//        String deployPath = configMap.get(PublicItemEnum.DEPLOY_PATH.getItemKey());
//        String tomcatConfigName = configMap.get(PublicItemEnum.TOMCAT_CONFIG_NAME.getItemKey());
//
//
//        TomcatSetenv tomcatSetenv = buildTomcatSetenv(serverGroupId, propertyGroupId);
//        String fileContent = tomcatSetenv.toBody();
//
//        try {
//            IOUtils.writeFile(fileContent, tomcatSetenv.getPath(deployPath, tomcatConfigName));
//            return new BusinessWrapper<>(fileContent);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return new BusinessWrapper<>(ErrorCode.serverFailure);
//        }
//    }

    public static final String GROUP_NAME_TOMCAT = "Tomcat";
    public static final String GROUP_NAME_NGINX = "Nginx";
    public static final String GROUP_NAME_ANSIBLE = "Ansible";


    @Override
    public List<PreviewConfig> previewServerPropertyFile(long serverGroupId, long propertyGroupId) {
        ConfigPropertyGroupDO configPropertyGroupDO = configDao.getConfigPropertyGroupById(propertyGroupId);
        List<PreviewConfig> configList = new ArrayList<>();
        // TODO GROUP_NAME_TOMCAT
        if (configPropertyGroupDO.getGroupName().equals(GROUP_NAME_TOMCAT)) {
            TomcatSetenv tomcatSetenv = buildTomcatSetenv(serverGroupId, propertyGroupId);
            String config = tomcatSetenv.toBody();
            configList.add(new PreviewConfig("Tomcat配置文件", config));
            return configList;
        }

        // TODO GROUP_NAME_NGINX
        if (configPropertyGroupDO.getGroupName().equals(GROUP_NAME_NGINX)) {
           // List<ServerDO> serverList = serverDao.acqServersByGroupId(serverGroupId);
            for (ServerDO.EnvTypeEnum e : ServerDO.EnvTypeEnum.values()) {
                List<ServerDO> serverList =   serverDao.getServersByGroupIdAndEnvType(serverGroupId,e.getCode());
                if(serverList.size() == 0)
                    continue;
                String locationConfig = nginxFileProcessorService.buildLocation(new ServerGroupDO(serverGroupId), e.getCode());
                if (!StringUtils.isEmpty(locationConfig))
                    configList.add(new PreviewConfig("NginxLoction配置", locationConfig, e.getCode()));
                String upstreamConfig = nginxFileProcessorService.buildUpstream(new ServerGroupDO(serverGroupId), serverList, e.getCode());
                if (!StringUtils.isEmpty(upstreamConfig))
                    configList.add(new PreviewConfig("NginxUpstream配置", upstreamConfig, e.getCode()));
            }
            return configList;
        }

        // TODO GROUP_NAME_ANSIBLE
        if (configPropertyGroupDO.getGroupName().equals(GROUP_NAME_ANSIBLE)) {
            ansibleFileProcessorService.preview(serverGroupId, configList);
            return configList;
        }

        return configList;
    }

//    @Override
//    public BusinessWrapper<String> launchServerPropertyFile(long serverGroupId, long propertyGroupId) {
//
//        HashMap<String, String> configMap = acqPublicConfigMap();
//        String deployPath = configMap.get(PublicItemEnum.DEPLOY_PATH.getItemKey());
//        String tomcatConfigName = configMap.get(PublicItemEnum.TOMCAT_CONFIG_NAME.getItemKey());
//
//        TomcatSetenv tomcatSetenv = buildTomcatSetenv(serverGroupId, propertyGroupId);
//
//        try {
//            String fileContent = IOUtils.readFile(tomcatSetenv.getPath(deployPath, tomcatConfigName));
//            return new BusinessWrapper<>(fileContent);
//        } catch (Exception e) {
//            return new BusinessWrapper<>(ErrorCode.serverFailure);
//        }
//    }

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
    public List<ConfigFileDO> getConfigFile() {
        return configDao.getConfigFile();
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
        // 触发用户变更代码
        try {
            ConfigFileGroupDO groupDO = configDao.getConfigFileGroupByName(FILEGROUP_SS);
            if (groupDO == null) return;
            List<ConfigFileDO> files = configDao.getConfigFileByGroupAndFileNameAndEnvType(groupDO.getId(), CONFIG_FILE_SHADOWSOCKS, ServerDO.EnvTypeEnum.prod.getCode());
            for (ConfigFileDO file : files) {
                createConfigFile(file.getId());
                List<ConfigFilePlaybookDO> playbooks = configDao.queryConfigFilePlaybookByFileId(file.getId());
                for (ConfigFilePlaybookDO playbook : playbooks)
                    doPlaybook(playbook.getId(), PLAYBOOK_DO_TYPE_SYSTEM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        // TODO nginx缓存清理
        invokeNginxConfig(new ServerGroupDO(serverGroupId), envType);
        // nginxFileProcessorService.delCache(new ServerGroupDO(serverGroupId), envType);
        // TODO ansible主机配置缓存清理
        invokeAnsibleConfig(new ServerGroupDO(serverGroupId));
    }


    @Override
    public void invokeDelServerConfig(long serverGroupId, int envType) {
        // TODO nginx缓存清理
        invokeNginxConfig(new ServerGroupDO(serverGroupId), envType);
        // nginxFileProcessorService.delCache(new ServerGroupDO(serverGroupId), envType);
        // TODO ansible主机配置缓存清理
        invokeAnsibleConfig(new ServerGroupDO(serverGroupId));
    }

    /**
     * 变更nginx
     */
    private void invokeNginxConfig(ServerGroupDO serverGroupDO, int envType) {
        nginxFileProcessorService.delCache(serverGroupDO, envType);
    }

    private void invokeNginxConfig(ServerGroupDO serverGroupDO) {
        nginxFileProcessorService.delCache(serverGroupDO);
    }

    /**
     * 变更nginx
     */
    private void invokeAnsibleConfig(ServerGroupDO serverGroupDO) {
        ansibleFileProcessorService.delCache(serverGroupDO);
        ConfigFileGroupDO configFileGroupDO = configDao.getConfigFileGroupByName(ConfigurationFileControlService.GROUP_ANSIBLE);
        List<ConfigFileDO> ansibleConfigList = configDao.queryConfigFileByGroupId(configFileGroupDO.getId());
        for (ConfigFileDO andibleConfig : ansibleConfigList) {
            createConfigFile(andibleConfig.getId());
        }
    }


    /**
     * 新增配置项变更配置文件
     */
    public void invokeConfig(long configPropertyGroupId, long serverGroupId, boolean isAddConfig) {
        // if (!checkInvokeEnv()) return;

        if (configPropertyGroupId == 0) return;
        ConfigPropertyGroupDO configPropertyGroupDO = configDao.getConfigPropertyGroupById(configPropertyGroupId);
        try {
            String cfgGroupName = configPropertyGroupDO.getGroupName();

            // TODO Nginx配置文件 (清理缓存)
            if (cfgGroupName.equalsIgnoreCase("Nginx"))
                invokeNginxConfig(new ServerGroupDO(serverGroupId));

            // TODO Ansible配置文件 (清理缓存)
            if (cfgGroupName.equalsIgnoreCase("Ansible"))
                invokeAnsibleConfig(new ServerGroupDO(serverGroupId));

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

    @Override
    public BusinessWrapper<Boolean> saveFilePlaybook(ConfigFilePlaybookDO configFilePlaybookDO) {
        try {
            if (configFilePlaybookDO.getId() == 0) {
                configDao.addConfigFilePlaybook(configFilePlaybookDO);
            } else {
                configDao.updateConfigFilePlaybook(configFilePlaybookDO);
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<Boolean>(false);

    }

    @Override
    public List<ConfigFilePlaybookVO> getFilePlaybookPage() {
        List<ConfigFilePlaybookDO> playbooks = configDao.getConfigFilePlaybookPage();
        List<ConfigFilePlaybookVO> list = new ArrayList<>();
        for (ConfigFilePlaybookDO playbook : playbooks) {
            ConfigFilePlaybookVO vo = getPlaybookVO(playbook);
            list.add(vo);
        }
        return list;
    }

    private ConfigFilePlaybookVO getPlaybookVO(ConfigFilePlaybookDO configFilePlaybookDO) {
        ConfigFilePlaybookVO vo = new ConfigFilePlaybookVO(configFilePlaybookDO);
        List<HostPattern> hostPattern = serverGroupService.getHostPattern(configFilePlaybookDO.getServerGroupId());
        vo.setGroupHostPattern(hostPattern);
        ConfigFileDO configFileDO = configDao.getConfigFileById(configFilePlaybookDO.getFileId());
        vo.setConfigFileDO(configFileDO);
        TaskScriptDO taskScriptDO = ansibleTaskDao.getTaskScript(configFilePlaybookDO.getPlaybookId());
        vo.setTaskScriptDO(taskScriptDO);
        return vo;
    }

    @Override
    public BusinessWrapper<Boolean> delFilePlaybook(long id) {
        try {
            configDao.delConfigFilePlaybook(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<Boolean>(false);
    }


    /**
     * @param id
     * @param doType 0 系统 1 人工
     * @return
     */
    @Override
    public PlaybookLogVO doPlaybook(long id, int doType) {
        ConfigFilePlaybookDO playbookDO = configDao.getConfigFilePlaybook(id);
        ConfigFilePlaybookVO vo = getPlaybookVO(playbookDO);
        String playbookPath = ansibleTaskService.getPlaybookPath(vo.getTaskScriptDO());
        String extraVars = "hosts=" + vo.getHostPattern() + " src=" + vo.getSrc() + " dest=" + vo.getDest();
        PlaybookLogDO pl;
        if (doType == 0) {
            pl = new PlaybookLogDO(vo);
        } else {
            UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
            pl = new PlaybookLogDO(vo, userDO);
        }
        ansibleTaskDao.addPlaybookLog(pl);
        schedulerManager.registerJob(() -> {
            ansibleTaskService.playbook(true, vo.getHostPattern(), playbookPath, extraVars, pl);
        });
        return new PlaybookLogVO(pl);
    }


    @Override
    public PlaybookLogVO getPlaybookLog(long logId) {
        PlaybookLogDO pl = ansibleTaskDao.getPlaybookLog(logId);
        return getPlaybookLog(pl);
    }

    @Override
    public PlaybookLogVO getPlaybookLog(PlaybookLogDO playbookLogDO) {
        PlaybookLogVO playbookLogVO = new PlaybookLogVO(playbookLogDO);
        if (playbookLogDO.isComplete()) {
            try {
                String playbookLog = IOUtils.readFile(playbookLogDO.getLogPath());
                playbookLogVO.setPlaybookLog(playbookLog);
            } catch (Exception e) {
                // 日志文件不存在
            }
            if (playbookLogDO.getDoType() == 1)
                playbookLogVO.setUserDO(userDao.getUserById(playbookLogDO.getUserId()));
            if (!StringUtils.isEmpty(playbookLogDO.getGmtModify())) {
                playbookLogVO.setViewTime(TimeViewUtils.format(playbookLogDO.getGmtModify()));
            } else {
                playbookLogVO.setViewTime(TimeViewUtils.format(playbookLogDO.getGmtCreate()));
            }
        }
        return playbookLogVO;
    }


    @Override
    public TableVO<List<PlaybookLogVO>> getPlaybookLogPage(String playbookName, String username, int page, int length) {
        long size = ansibleTaskDao.getPlaybookLogSize(playbookName, username);
        List<PlaybookLogDO> playbookList = ansibleTaskDao.queryPlaybookLogPage(playbookName, username, page * length, length);
        List<PlaybookLogVO> voList = new ArrayList<>();
        for (PlaybookLogDO playbookLog : playbookList)
            voList.add(getPlaybookLog(playbookLog));
        return new TableVO<>(size, voList);
    }


    @Override
    public BusinessWrapper<Boolean> delPlaybookLog(long id) {
        try {
            PlaybookLogDO playbookLogDO = ansibleTaskDao.getPlaybookLog(id);
            if (playbookLogDO == null)
                return new BusinessWrapper<Boolean>(false);
            IOUtils.delFile(playbookLogDO.getLogPath());
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }

        ansibleTaskDao.delPlaybookLog(id);
        return new BusinessWrapper<Boolean>(true);
    }
}
