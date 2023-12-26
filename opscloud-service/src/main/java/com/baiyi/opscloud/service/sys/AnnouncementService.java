package com.baiyi.opscloud.service.sys;

import com.baiyi.opscloud.domain.generator.opscloud.SysAnnouncement;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/28 17:11
 * @Version 1.0
 */
public interface AnnouncementService {

    List<SysAnnouncement> queryByKind(int kind);

}