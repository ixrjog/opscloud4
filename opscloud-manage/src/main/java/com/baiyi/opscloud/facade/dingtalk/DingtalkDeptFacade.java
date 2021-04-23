package com.baiyi.opscloud.facade.dingtalk;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkDept;
import com.baiyi.opscloud.domain.vo.dingtalk.DingtalkVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 3:02 下午
 * @Since 1.0
 */
public interface DingtalkDeptFacade {

    BusinessWrapper<Boolean> syncDept(String uid);

    BusinessWrapper<DingtalkVO.DeptTree> queryDingtalkDeptTree(String uid);

    BusinessWrapper<DingtalkVO.DeptTree> refreshDingtalkDeptTree(String uid);

    BusinessWrapper<List<OcDingtalkDept>>  queryDingtalkRootDept();
}
