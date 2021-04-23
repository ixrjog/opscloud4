package com.baiyi.opscloud.service.aliyun.log;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunLog;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogParam;

/**
 * @Author baiyi
 * @Date 2020/6/13 3:34 下午
 * @Version 1.0
 */
public interface OcAliyunLogService {

    DataTable<OcAliyunLog> queryOcAliyunLogByParam(AliyunLogParam.AliyunLogPageQuery pageQuery);

    void addOcAliyunLog(OcAliyunLog ocAliyunLog);

    void updateOcAliyunLog(OcAliyunLog ocAliyunLog);

    OcAliyunLog queryOcAliyunLogById(int id);
}
