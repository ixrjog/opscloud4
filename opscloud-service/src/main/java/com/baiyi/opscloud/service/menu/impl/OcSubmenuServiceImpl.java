package com.baiyi.opscloud.service.menu.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcSubmenu;
import com.baiyi.opscloud.mapper.opscloud.OcSubmenuMapper;
import com.baiyi.opscloud.service.menu.OcSubmenuService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/29 4:52 下午
 * @Since 1.0
 */

@Service
public class OcSubmenuServiceImpl implements OcSubmenuService {

    @Resource
    private OcSubmenuMapper ocSubmenuMapper;

    @Override
    public void addOcSubmenu(OcSubmenu ocSubmenu) {
        ocSubmenuMapper.insert(ocSubmenu);
    }

    @Override
    public void updateOcSubmenu(OcSubmenu ocSubmenu) {
        ocSubmenuMapper.updateByPrimaryKey(ocSubmenu);
    }

    @Override
    public void delOcSubmenu(int id) {
        ocSubmenuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcSubmenu queryOcSubmenu(int id) {
        return ocSubmenuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OcSubmenu> queryOcSubmenuByMenuId(Integer menuId) {
        Example example = new Example(OcSubmenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuId", menuId);
        example.setOrderByClause("submenu_order");
        return ocSubmenuMapper.selectByExample(example);
    }

    @Override
    public List<OcSubmenu> queryOcSubmenuByIdList(List<Integer> idList) {
        return ocSubmenuMapper.queryOcSubmenuByIdList(idList);
    }
}
