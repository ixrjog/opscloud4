package com.baiyi.opscloud.service.datasource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AliyunLog;
import com.baiyi.opscloud.domain.param.datasource.AliyunLogParam;

/**
 * @Author baiyi
 * @Date 2021/9/16 5:44 下午
 * @Version 1.0
 */
public interface AliyunLogService {

    DataTable<AliyunLog> queryAliyunLogByParam(AliyunLogParam.AliyunLogPageQuery pageQuery);

    void add(AliyunLog aliyunLog);

    void update(AliyunLog aliyunLog);

    void deleteById(Integer id);

    AliyunLog getById(Integer id);

}