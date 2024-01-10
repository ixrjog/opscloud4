package com.baiyi.opscloud.service.sys;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DocumentZone;
import com.baiyi.opscloud.domain.param.sys.DocumentParam;

/**
 * @Author baiyi
 * @Date 2023/2/8 10:12
 * @Version 1.0
 */
public interface DocumentZoneService {

    DocumentZone getByMountZone(String mountZone);

    DataTable<DocumentZone> queryPageByParam(DocumentParam.DocumentZonePageQuery pageQuery);

    void updateByPrimaryKeySelective(DocumentZone documentZone);

}