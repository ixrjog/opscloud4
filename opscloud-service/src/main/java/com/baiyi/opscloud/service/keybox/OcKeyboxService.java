package com.baiyi.opscloud.service.keybox;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKeybox;
import com.baiyi.opscloud.domain.param.keybox.KeyboxParam;

/**
 * @Author baiyi
 * @Date 2020/5/3 10:30 上午
 * @Version 1.0
 */
public interface OcKeyboxService {

    OcKeybox queryOcKeyboxBySystemUser(String systemUser);

    OcKeybox queryOcKeyboxById(int id);

    void updateOcKeybox(OcKeybox ocKeybox);

    void addOcKeybox(OcKeybox ocKeybox);

    void deleteOcKeyboxById(int id);

    DataTable<OcKeybox> queryOcKeyboxByParam(KeyboxParam.PageQuery pageQuery);
}
