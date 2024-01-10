package com.baiyi.opscloud.service.sys.impl;

import com.baiyi.opscloud.domain.generator.opscloud.MenuChild;
import com.baiyi.opscloud.mapper.MenuChildMapper;
import com.baiyi.opscloud.service.sys.MenuChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/1 5:10 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class MenuChildServiceImpl implements MenuChildService {

    private final MenuChildMapper menuChildMapper;

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

    @Override
    public List<MenuChild> listByIdList(List<Integer> idList) {
        Example example = new Example(MenuChild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", idList);
        return menuChildMapper.selectByExample(example);
    }

}