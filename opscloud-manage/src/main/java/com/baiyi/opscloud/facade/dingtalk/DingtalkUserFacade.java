package com.baiyi.opscloud.facade.dingtalk;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.baiyi.opscloud.domain.vo.dingtalk.DingtalkVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/15 11:46 上午
 * @Since 1.0
 */
public interface DingtalkUserFacade {

    DataTable<DingtalkVO.User> queryDingtalkUserPage(DingtalkParam.UserPageQuery pageQuery);

    BusinessWrapper<Boolean> syncUser();

    BusinessWrapper<Boolean> bindOcUser(DingtalkParam.BindOcUser param);

    BusinessWrapper<Boolean> updateUser(DingtalkParam.GetUser param);

    BusinessWrapper<List<DingtalkVO.User>> queryDingtalkUserByDingtalkDeptId(Integer ocDingtalkDeptId);
}
