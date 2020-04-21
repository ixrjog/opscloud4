package com.baiyi.opscloud.org;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.vo.org.DepartmentTreeVO;
import com.baiyi.opscloud.facade.OrgFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/20 7:49 下午
 * @Version 1.0
 */
public class OrgTest extends BaseUnit {

    @Resource
    private OrgFacade orgFacade;

    @Test
    void aTest(){
        DepartmentTreeVO.DepartmentTree  deptTree =  orgFacade.queryDepartmentTree();
        System.err.println(JSON.toJSONString(deptTree));
    }
}
