package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsSystemuser;
import com.baiyi.opscloud.mapper.jumpserver.AssetsSystemuserMapper;
import com.baiyi.opscloud.service.jumpserver.AssetsSystemuserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/13 3:38 下午
 * @Version 1.0
 */
@Service
public class AssetsSystemuserServiceImpl implements AssetsSystemuserService {

    @Resource
    private AssetsSystemuserMapper assetsSystemuserMapper;

    @Override
    public List<AssetsSystemuser> queryAllAssetsSystemuser() {
        return assetsSystemuserMapper.selectAll();
    }

}
