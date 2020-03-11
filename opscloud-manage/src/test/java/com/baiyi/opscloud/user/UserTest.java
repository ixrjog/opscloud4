package com.baiyi.opscloud.user;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.service.user.OcUserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/11 9:30 上午
 * @Version 1.0
 */
public class UserTest extends BaseUnit {
    @Resource
    private OcUserService ocUserService;

    @Test
    void updateUsersUuid() {
        UserParam.PageQuery pageQuery = new UserParam.PageQuery();
        pageQuery.setUsername("");
        pageQuery.setLength(10000);
        pageQuery.setPage(1);
        DataTable<OcUser> table = ocUserService.queryOcUserByParam(pageQuery);
        System.err.println(JSON.toJSONString(table));
        for(OcUser ocUser : table.getData()){
            ocUser.setUuid(UUIDUtils.getUUID());
            ocUserService.updateOcUser(ocUser);
        }

    }


}
