package com.baiyi.opscloud.service.sys;

import com.baiyi.opscloud.domain.generator.opscloud.Document;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:33 上午
 * @Version 1.0
 */
public interface DocumentService {

    Document getByKey(String key);
}
