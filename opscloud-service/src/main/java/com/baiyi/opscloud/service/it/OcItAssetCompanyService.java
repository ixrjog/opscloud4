package com.baiyi.opscloud.service.it;

import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetCompany;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 3:07 下午
 * @Since 1.0
 */
public interface OcItAssetCompanyService {

    OcItAssetCompany queryOcItAssetCompany(Integer id);

    List<OcItAssetCompany> queryOcItAssetCompanyAll();

}
