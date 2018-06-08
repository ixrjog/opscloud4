package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.zabbix.ZabbixTemplateDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liangjian on 2017/1/13.
 */
@Component
public interface ZabbixServerDao {


    List<ZabbixTemplateDO> getTemplatePage(
            @Param("templateName") String templateName,
            @Param("enabled") int enabled,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);


    long getTemplateSize(
            @Param("templateName") String templateName,
            @Param("enabled") int enabled);

    ZabbixTemplateDO getTemplateByName(@Param("templateName") String templateName);

    ZabbixTemplateDO getTemplate(@Param("id") long id);

    int delTemplate(@Param("id") long id);

    int updateTemplate(ZabbixTemplateDO zabbixTemplateDO);

    int addTemplate(ZabbixTemplateDO zabbixTemplateDO);


}
