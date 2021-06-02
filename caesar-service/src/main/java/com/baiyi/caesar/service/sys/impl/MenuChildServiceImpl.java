package com.baiyi.caesar.service.sys.impl;

import com.baiyi.caesar.domain.generator.caesar.MenuChild;
import com.baiyi.caesar.mapper.caesar.MenuChildMapper;
import com.baiyi.caesar.service.sys.MenuChildService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/1 5:10 下午
 * @Version 1.0
 */
@Service
public class MenuChildServiceImpl implements MenuChildService {

    @Resource
    private MenuChildMapper menuChildMapper;

    @Override
    public void add(MenuChild menuChild) {
        menuChildMapper.insert(menuChild);
    }

    @Override
    public void update(MenuChild menuChild) {
        menuChildMapper.updateByPrimaryKey(menuChild);
    }

    @Override
    public void del(Integer id) {
        menuChildMapper.deleteByPrimaryKey(id);
    }

    @Override
    public MenuChild getById(Integer id) {
        return menuChildMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MenuChild> listByMenuId(Integer menuId) {
        Example example = new Example(MenuChild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuId", menuId);
        example.setOrderByClause("seq");
        return menuChildMapper.selectByExample(example);
    }
}
