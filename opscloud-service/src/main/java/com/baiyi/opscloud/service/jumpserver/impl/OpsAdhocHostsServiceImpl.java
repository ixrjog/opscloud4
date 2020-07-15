package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.OpsAdhocHosts;
import com.baiyi.opscloud.mapper.jumpserver.OpsAdhocHostsMapper;
import com.baiyi.opscloud.service.jumpserver.OpsAdhocHostsService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/24 1:41 下午
 * @Version 1.0
 */
@Service
public class OpsAdhocHostsServiceImpl implements OpsAdhocHostsService {

    @Resource
    private OpsAdhocHostsMapper opsAdhocHostsMapper;

    @Override
    public void deleteOpsAdhocHostsById(int id) {
        opsAdhocHostsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OpsAdhocHosts> queryOpsAdhocHostsByAssetId(String assetId) {
        Example example = new Example(OpsAdhocHosts.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetId);
        return opsAdhocHostsMapper.selectByExample(example);
    }

}
