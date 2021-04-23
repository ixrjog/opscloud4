package com.baiyi.opscloud.service.announcement.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnnouncement;
import com.baiyi.opscloud.domain.param.announcement.AnnouncementParam;
import com.baiyi.opscloud.mapper.opscloud.OcAnnouncementMapper;
import com.baiyi.opscloud.service.announcement.OcAnnouncementService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/22 4:06 下午
 * @Since 1.0
 */

@Service
public class OcAnnouncementServiceImpl implements OcAnnouncementService {

    @Resource
    private OcAnnouncementMapper ocAnnouncementMapper;

    @Override
    public void addOcAnnouncement(OcAnnouncement ocAnnouncement) {
        ocAnnouncementMapper.insert(ocAnnouncement);
    }

    @Override
    public void updateOcAnnouncement(OcAnnouncement OcAnnouncement) {
        ocAnnouncementMapper.updateByPrimaryKey(OcAnnouncement);
    }

    @Override
    public void delOcAnnouncement(int id) {
        ocAnnouncementMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcAnnouncement> queryValidOcAnnouncement() {
        Example example = new Example(OcAnnouncement.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true);
        return ocAnnouncementMapper.selectByExample(example);
    }

    @Override
    public DataTable<OcAnnouncement> queryOcAnnouncementPage(AnnouncementParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAnnouncement> announcementList = ocAnnouncementMapper.queryOcAnnouncementPage(pageQuery);
        return new DataTable<>(announcementList, page.getTotal());
    }
}
