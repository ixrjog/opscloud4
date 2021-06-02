package com.baiyi.caesar.packer.sys;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.Menu;
import com.baiyi.caesar.domain.generator.caesar.MenuChild;
import com.baiyi.caesar.service.sys.MenuChildService;
import com.baiyi.caesar.domain.vo.sys.MenuVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/2 10:53 上午
 * @Since 1.0
 */

@Component
public class MenuPacker {

    @Resource
    private MenuChildService menuChildService;

    public List<Menu> toDOList(List<MenuVO.Menu> menuList) {
        return BeanCopierUtil.copyListProperties(menuList, Menu.class);
    }

    public List<MenuChild> toChildDOList(List<MenuVO.MenuChild> menuChildren) {
        return BeanCopierUtil.copyListProperties(menuChildren, MenuChild.class);
    }

    public List<MenuVO.Menu> toVOList(List<Menu> menuList) {
        return BeanCopierUtil.copyListProperties(menuList, MenuVO.Menu.class);
    }

    public List<MenuVO.MenuChild> toChildVOList(List<MenuChild> menuChildList) {
        return BeanCopierUtil.copyListProperties(menuChildList, MenuVO.MenuChild.class);
    }
}

