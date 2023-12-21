package com.baiyi.opscloud.facade.sys;

import com.baiyi.opscloud.domain.vo.sys.AnnouncementVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/28 17:21
 * @Version 1.0
 */
public interface AnnouncementFacade {

    List<AnnouncementVO.Announcement> getAnnouncementByKind(int kind);

}