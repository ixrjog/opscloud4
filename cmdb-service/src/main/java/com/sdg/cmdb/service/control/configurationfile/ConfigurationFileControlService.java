package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.dao.cmdb.ConfigDao;
import com.sdg.cmdb.domain.config.ConfigFileDO;
import com.sdg.cmdb.domain.config.ConfigFileGroupDO;
import com.sdg.cmdb.util.IOUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2016/12/15.
 */
@Service
public class ConfigurationFileControlService {

    public final static String nameAnsible = "ansible";

    public final static String nameInterfaceCI = "interfaceCI";

    public final static String nameTomcatInstallConfig = "tomcatInstallConfig";

    public final static String nameGetway = "getway";

    public final static String nameShadowsocks = "shadowsocks";

    public final static String nameNginxUpstream = "nginxUpstream";

    public final static String nameNginxLocationManage = "nginxLocationManage";

    public final static String nameNginxLocationSupplier = "nginxLocationSupplier";

    public final static String nameNginxLocationKa = "nginxLocationKa";

    public final static String nameNginxLocation = "nginxLocation";

    public final static String nameIptablesDubbo = "iptablesDubbo";

    public final static String nameDnsmasq = "dnsmasq";


    @Resource
    private AnsibleHostService ansibleHostService;

    @Resource
    private InterfaceCIService interfaceCIService;

    @Resource
    private TomcatInstallConfigService tomcatInstallConfigService;

    @Resource
    private GetwayService getwayService;

    @Resource
    private ShadowsocksService shadowsocksService;

    @Resource
    private NginxUpstreamService nginxUpstreamService;

    @Resource
    private NginxLocationManageService nginxLocationManageService;

    @Resource
    private NginxLocationKaService nginxLocationKaService;

    @Resource
    private NginxLocationSupplierService nginxLocationSupplierService;

    @Resource
    private NginxLocationService nginxLocationService;

    @Resource
    private IptablsZookeeperService iptablsZookeeperService;

    @Resource
    private ConfigDao configDao;

    @Resource
    private DnsmasqService dnsmasqService;


    public static final String GROUP_ANSIBLE = "filegroup_ansible";

    public static final String GROUP_GETWAY = "filegroup_getway";


    public String build(ConfigFileDO configFileDO) {
        ConfigFileGroupDO configFileGroupDO = configDao.getConfigFileGroupById(configFileDO.getFileGroupId());

        if (configFileGroupDO.getGroupName().equalsIgnoreCase(GROUP_ANSIBLE))
            return ansibleHostService.acqHostsCfgByUseType(configFileDO.getUseType());

        if (configFileGroupDO.getGroupName().equalsIgnoreCase(GROUP_GETWAY))
            return getwayService.acqHostConfig();


        return "";
    }

    /**
     * 按名称生成配置文件
     *
     * @param name
     * @param type
     */
    public String build(String name, int type) {
        switch (name) {
            case nameAnsible:
                // return ansibleService.acqConfig(type);
                return "";
            case nameInterfaceCI:
                return interfaceCIService.acqConfig();
            case nameTomcatInstallConfig:
                return tomcatInstallConfigService.acqConfig();
            case nameGetway:
                return "";
            case nameShadowsocks:
                return shadowsocksService.acqConfig();
            case nameNginxUpstream:
                return nginxUpstreamService.acqConfig(type);
            case nameNginxLocationManage:
                return nginxLocationManageService.acqConfig(type);
            case nameNginxLocationSupplier:
                return nginxLocationSupplierService.acqConfig(type);
            case nameNginxLocation:
                return nginxLocationService.acqConfig(type);
            case nameIptablesDubbo:
                return iptablsZookeeperService.acqConfig(type);
            case nameDnsmasq:
                return dnsmasqService.acqConfig(type);
        }
        return new String();
    }

    /**
     * 写配置文件
     */


}
