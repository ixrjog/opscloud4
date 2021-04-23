package com.baiyi.opscloud.facade.announcement.impl;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnnouncement;
import com.baiyi.opscloud.domain.param.announcement.AnnouncementParam;
import com.baiyi.opscloud.facade.announcement.AnnouncementFacade;
import com.baiyi.opscloud.service.announcement.OcAnnouncementService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/25 11:45 上午
 * @Since 1.0
 */

@Component("AnnouncementFacade")
public class AnnouncementFacadeImpl implements AnnouncementFacade {

    @Resource
    private OcAnnouncementService ocAnnouncementService;

    @Override
    public BusinessWrapper<Boolean> saveAnnouncement(OcAnnouncement ocAnnouncement) {
        if (ocAnnouncement.getId() == null) {
            ocAnnouncementService.addOcAnnouncement(ocAnnouncement);
        } else {
            ocAnnouncementService.updateOcAnnouncement(ocAnnouncement);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> delAnnouncement(int id) {
        ocAnnouncementService.delOcAnnouncement(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<List<OcAnnouncement>> queryValidOcAnnouncement() {
        List<OcAnnouncement> announcementList = ocAnnouncementService.queryValidOcAnnouncement();
        return new BusinessWrapper<>(announcementList);
    }

    @Override
    public DataTable<OcAnnouncement> queryOcAnnouncementPage(AnnouncementParam.PageQuery pageQuery) {
        return ocAnnouncementService.queryOcAnnouncementPage(pageQuery);
    }
}
