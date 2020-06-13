package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsAdminuser;
import com.baiyi.opscloud.mapper.jumpserver.AssetsAdminuserMapper;
import com.baiyi.opscloud.service.jumpserver.AssetsAdminuserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/13 3:45 下午
 * @Version 1.0
 */
@Service
public class AssetsAdminuserServiceImpl implements AssetsAdminuserService {

    @Resource
    private AssetsAdminuserMapper assetsAdminuserMapper;

    @Override
    public List<AssetsAdminuser> queryAllAssetsAdminuser() {
        return assetsAdminuserMapper.selectAll();
    }
}
