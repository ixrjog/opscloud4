package com.baiyi.opscloud.service.menu.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcMenu;
import com.baiyi.opscloud.mapper.opscloud.OcMenuMapper;
import com.baiyi.opscloud.service.menu.OcMenuService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/29 4:51 下午
 * @Since 1.0
 */

@Service
public class OcMenuServiceImpl implements OcMenuService {

    @Resource
    private OcMenuMapper ocMenuMapper;

    @Override
    public void addOcMenu(OcMenu ocMenu) {
        ocMenuMapper.insert(ocMenu);
    }

    @Override
    public void updateOcMenu(OcMenu ocMenu) {
        ocMenuMapper.updateByPrimaryKey(ocMenu);
    }

    @Override
    public void delOcMenu(int id) {
        ocMenuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcMenu queryOcMenu(int id) {
        return ocMenuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OcMenu> queryOcMenuAll() {
        Example example = new Example(OcMenu.class);
        example.setOrderByClause("menu_order");
        return ocMenuMapper.selectByExample(example);
    }
}
