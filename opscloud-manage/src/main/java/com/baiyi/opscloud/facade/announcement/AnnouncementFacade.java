package com.baiyi.opscloud.facade.announcement;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnnouncement;
import com.baiyi.opscloud.domain.param.announcement.AnnouncementParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/25 11:44 上午
 * @Since 1.0
 */
public interface AnnouncementFacade {

    BusinessWrapper<Boolean> saveAnnouncement(OcAnnouncement ocAnnouncement);

    BusinessWrapper<Boolean> delAnnouncement(int id);

    BusinessWrapper<List<OcAnnouncement>> queryValidOcAnnouncement();

    DataTable<OcAnnouncement> queryOcAnnouncementPage(AnnouncementParam.PageQuery pageQuery);
}
