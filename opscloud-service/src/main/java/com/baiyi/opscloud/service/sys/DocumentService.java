package com.baiyi.opscloud.service.sys;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Document;
import com.baiyi.opscloud.domain.param.sys.DocumentParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:33 上午
 * @Version 1.0
 */
public interface DocumentService {

    Document getByKey(String key);

    List<Document> queryByMountZone(String mountZone);

    DataTable<Document> queryPageByParam(DocumentParam.DocumentPageQuery pageQuery);

    void add(Document document);

    void updateByPrimaryKeySelective(Document document);

    void deleteById(int id);

}