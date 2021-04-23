package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAnnouncement;
import com.baiyi.opscloud.domain.param.announcement.AnnouncementParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAnnouncementMapper extends Mapper<OcAnnouncement> {

    List<OcAnnouncement> queryOcAnnouncementPage(AnnouncementParam.PageQuery pageQuery);
}