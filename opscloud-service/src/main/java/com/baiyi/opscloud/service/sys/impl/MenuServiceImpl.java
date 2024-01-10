package com.baiyi.opscloud.service.sys.impl;

import com.baiyi.opscloud.domain.generator.opscloud.Menu;
import com.baiyi.opscloud.mapper.MenuMapper;
import com.baiyi.opscloud.service.sys.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/1 5:09 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;

    @Override
    public void add(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void update(Menu menu) {
        menuMapper.updateByPrimaryKey(menu);
    }

    @Override
    public void del(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Menu getById(Integer id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Menu> queryAllBySeq() {
        Example example = new Example(Menu.class);
        example.setOrderByClause("seq");
        return menuMapper.selectByExample(example);
    }

    @Override
    public List<Menu> listAll() {
        return menuMapper.selectAll();
    }

}