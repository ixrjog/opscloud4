package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.SysAnnouncement;
import com.baiyi.opscloud.domain.vo.sys.AnnouncementVO;
import com.baiyi.opscloud.facade.sys.AnnouncementFacade;
import com.baiyi.opscloud.service.sys.AnnouncementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/28 17:21
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementFacadeImpl implements AnnouncementFacade {

    private final AnnouncementService announcementService;

    @Override
    public List<AnnouncementVO.Announcement> getAnnouncementByKind(int kind) {
        List<SysAnnouncement> announcements = announcementService.queryByKind(kind);
        return BeanCopierUtil.copyListProperties(announcements, AnnouncementVO.Announcement.class);
    }

}