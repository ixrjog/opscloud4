package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.domain.generator.opscloud.OcAuthMenu;
import com.baiyi.opscloud.domain.vo.auth.menu.MenuVO;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * @Author baiyi
 * @Date 2020/4/23 9:59 上午
 * @Version 1.0
 */
public class MenuUtils {

    public static ArrayList<MenuVO> buildMenu(OcAuthMenu ocAuthMenu) {
        return new GsonBuilder().create().fromJson(ocAuthMenu.getMenu(), new TypeToken<ArrayList<MenuVO>>() {
        }.getType());
    }
}
