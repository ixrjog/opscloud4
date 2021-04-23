package com.baiyi.opscloud.decorator.dingtalk;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkDept;
import com.baiyi.opscloud.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.opscloud.service.dingtalk.OcDingtalkDeptService;
import com.google.common.collect.Lists;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/15 2:31 下午
 * @Since 1.0
 */

@Component
public class DingtalkDeptDecorator {

    private static final Long ROOT_DEPT_ID = 1L;

    @Resource
    private OcDingtalkDeptService ocDingtalkDeptService;

    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'ocDingtalkDeptTreeDecorator' + #uid", beforeInvocation = true)
    public void evictPreview(String uid) {
    }

    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'ocDingtalkDeptTreeDecorator' + #uid")
    public DingtalkVO.DeptTree decoratorTreeVO(String uid) {
        DingtalkVO.DeptTree deptTree = new DingtalkVO.DeptTree();
        OcDingtalkDept rootDept = ocDingtalkDeptService.queryOcDingtalkDeptByDeptId(ROOT_DEPT_ID, uid);
        deptTree.setName(rootDept.getDeptName());
        deptTree.setChildren(getChildDeptTree(rootDept, uid));
        return deptTree;
    }

    // 递归计算以入参为根节点的子节点
    private List<DingtalkVO.ChildDeptTree> getChildDeptTree(OcDingtalkDept ocDingtalkDept, String uid) {
        List<OcDingtalkDept> childDeptList = ocDingtalkDeptService.queryOcDingtalkDeptByParentId(ocDingtalkDept.getDeptId(), uid);
        if (CollectionUtils.isEmpty(childDeptList))
            return Collections.emptyList();
        List<DingtalkVO.ChildDeptTree> childDeptTreeList = Lists.newArrayListWithCapacity(childDeptList.size());
        childDeptList.forEach(childDept -> {
            DingtalkVO.ChildDeptTree childDeptTree = DingtalkVO.ChildDeptTree.builder()
                    .id(childDept.getId())
                    .name(childDept.getDeptName())
                    .children(getChildDeptTree(childDept, uid))
                    .build();
            childDeptTreeList.add(childDeptTree);
        });
        return childDeptTreeList;
    }
}
