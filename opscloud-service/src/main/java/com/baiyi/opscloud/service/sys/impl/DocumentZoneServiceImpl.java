package com.baiyi.opscloud.service.sys.impl;

import com.baiyi.opscloud.domain.generator.opscloud.DocumentZone;
import com.baiyi.opscloud.mapper.opscloud.DocumentZoneMapper;
import com.baiyi.opscloud.service.sys.DocumentZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author baiyi
 * @Date 2023/2/8 10:12
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class DocumentZoneServiceImpl implements DocumentZoneService {

    private final DocumentZoneMapper documentZoneMapper;

    @Override
    public DocumentZone getByMountZone(String mountZone) {
        Example example = new Example(DocumentZone.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mountZone", mountZone)
                .andEqualTo("isActive", true);
        return documentZoneMapper.selectOneByExample(example);
    }

}
