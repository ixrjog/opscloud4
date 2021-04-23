package com.baiyi.opscloud.service.announcement;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnnouncement;
import com.baiyi.opscloud.domain.param.announcement.AnnouncementParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/22 4:05 下午
 * @Since 1.0
 */
public interface OcAnnouncementService {

    void addOcAnnouncement(OcAnnouncement ocAnnouncement);

    void updateOcAnnouncement(OcAnnouncement OcAnnouncement);

    void delOcAnnouncement(int id);

    List<OcAnnouncement> queryValidOcAnnouncement();

    DataTable<OcAnnouncement> queryOcAnnouncementPage(AnnouncementParam.PageQuery pageQuery);
}
