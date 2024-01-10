package com.baiyi.opscloud.service.sys.impl;

import com.baiyi.opscloud.domain.generator.opscloud.SysAnnouncement;
import com.baiyi.opscloud.mapper.SysAnnouncementMapper;
import com.baiyi.opscloud.service.sys.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/28 17:11
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final SysAnnouncementMapper announcementMapper;

    @Override
    public List<SysAnnouncement> queryByKind(int kind) {
        Example example = new Example(SysAnnouncement.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("kind", kind)
                .andEqualTo("isActive", true);
        return announcementMapper.selectByExample(example);
    }

}