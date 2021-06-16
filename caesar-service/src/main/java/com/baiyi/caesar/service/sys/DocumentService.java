package com.baiyi.caesar.service.sys;

import com.baiyi.caesar.domain.generator.caesar.Document;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:33 上午
 * @Version 1.0
 */
public interface DocumentService {

    Document getByKey(String key);
}
