package com.baiyi.opscloud.service.sys;

import com.baiyi.opscloud.domain.generator.opscloud.Nav;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/6/30 7:03 PM
 * @Since 1.0
 */
public interface NavService {

    void add(Nav nav);

    void update(Nav nav);

    void del(Integer id);

    Nav getById(Integer id);

    List<Nav> listAll();

    List<Nav> listActive();

}