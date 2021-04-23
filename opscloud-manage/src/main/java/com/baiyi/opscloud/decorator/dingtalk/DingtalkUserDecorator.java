package com.baiyi.opscloud.decorator.dingtalk;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.dingtalk.DingtalkUserApi;
import com.baiyi.opscloud.dingtalk.bo.DingtalkUserBO;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkDept;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.opscloud.service.dingtalk.OcDingtalkDeptService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Lists;
import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/15 4:09 下午
 * @Since 1.0
 */

@Component
public class DingtalkUserDecorator {

    private static final Long ROOT_DEPT_ID = 1L;

    private static Map<String, List<OcDingtalkDept>> deptMap = Maps.newHashMap();

    @Resource
    private OcUserService ocUserService;

    @Resource
    private DingtalkUserApi dingtalkUserApi;

    @Resource
    private OcDingtalkDeptService ocDingtalkDeptService;

    private DingtalkVO.User decoratorVO(OcAccount ocAccount) {
        DingtalkVO.User user = BeanCopierUtils.copyProperties(ocAccount, DingtalkVO.User.class);
        if (ocAccount.getUserId() != null) {
            OcUser ocUser = ocUserService.queryOcUserById(ocAccount.getUserId());
            if (ocUser != null)
                user.setOcUser(ocUser);
        }
        DingtalkUserBO dingtalkUserBO = dingtalkUserApi.getUserDetail(ocAccount.getUsername(), ocAccount.getAccountUid());
        if (!CollectionUtils.isEmpty(dingtalkUserBO.getDeptIdList())) {
            Map<String, List<OcDingtalkDept>> map = Maps.newHashMapWithExpectedSize(dingtalkUserBO.getDeptIdList().size());
            dingtalkUserBO.getDeptIdList().forEach(deptId -> {
                OcDingtalkDept dept = ocDingtalkDeptService.queryOcDingtalkDeptByDeptId(deptId, ocAccount.getAccountUid());
                if (!deptMap.containsKey(dept.getDeptName()))
                    deptMap.put(dept.getDeptName(), getDeptList(dept));
                map.put(dept.getDeptName(), deptMap.get(dept.getDeptName()));
            });
            user.setDeptMap(map);
        }
        return user;
    }

    public List<DingtalkVO.User> decoratorVOList(List<OcAccount> ocAccountList) {
        List<DingtalkVO.User> userList = Lists.newArrayListWithCapacity(ocAccountList.size());
        ocAccountList.forEach(ocAccount -> userList.add(decoratorVO(ocAccount)));
        return userList;
    }

    private Stack<OcDingtalkDept> getDeptList(OcDingtalkDept ocDingtalkDept) {
        Stack<OcDingtalkDept> stack = new Stack<>();
        OcDingtalkDept dept = ocDingtalkDept;
        while (dept != null) {
            stack.push(dept);
            dept = getParentDept(dept);
        }
        Collections.reverse(stack);
        return stack;
    }

    private OcDingtalkDept getParentDept(OcDingtalkDept ocDingtalkDept) {
        if (ocDingtalkDept.getParentId().equals(ROOT_DEPT_ID)) {
            return null;
        }
        return ocDingtalkDeptService.queryOcDingtalkDeptByDeptId(ocDingtalkDept.getParentId(), ocDingtalkDept.getDingtalkUid());
    }

    public void clearMap() {
        deptMap.clear();
    }
}
