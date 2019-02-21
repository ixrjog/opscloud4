package com.sdg.cmdb.service.configurationProcessor;

import com.sdg.cmdb.dao.cmdb.DnsDao;
import com.sdg.cmdb.domain.dns.DnsmasqDO;
import com.sdg.cmdb.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DnsmasqFileProcessorService extends ConfigurationProcessorAbs {

    @Autowired
    private DnsDao dnsDao;

    @Value("#{cmdb['dns.public.conf']}")
    private String dnsPublicConf;

    @Value("#{cmdb['dns.conf']}")
    private String dnsConf;


    public boolean buildFile(){
        try{
            String confFile = getFile();
            IOUtils.writeFile(confFile,dnsConf);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getFile() {
        String file = getHeadInfo();
        file += "# Dnsmasq全局配置文件 : " + dnsPublicConf + "\n";
        file += IOUtils.readFile(dnsPublicConf) + "\n";
        List<DnsmasqDO> dnsList = dnsDao.queryAllDnsmasq();
        for (DnsmasqDO dns : dnsList)
            file += buildDns(dns);
        return file;
    }

    public String buildDns(DnsmasqDO dns) {
        String result = "";
        if (!StringUtils.isEmpty(dns.getContent()))
            result = "# " + dns.getContent() + "\n";
        result += dns.getDnsItem() + "\n";
        return result;
    }

}
