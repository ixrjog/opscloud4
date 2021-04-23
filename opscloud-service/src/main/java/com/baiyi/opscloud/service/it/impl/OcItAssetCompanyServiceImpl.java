package com.baiyi.opscloud.service.it.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetCompany;
import com.baiyi.opscloud.mapper.opscloud.OcItAssetCompanyMapper;
import com.baiyi.opscloud.service.it.OcItAssetCompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 3:08 下午
 * @Since 1.0
 */

@Service
public class OcItAssetCompanyServiceImpl implements OcItAssetCompanyService {

    @Resource
    private OcItAssetCompanyMapper ocItAssetCompanyMapper;

    @Override
    public OcItAssetCompany queryOcItAssetCompany(Integer id) {
        return ocItAssetCompanyMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OcItAssetCompany> queryOcItAssetCompanyAll() {
        return ocItAssetCompanyMapper.selectAll();
    }
}
