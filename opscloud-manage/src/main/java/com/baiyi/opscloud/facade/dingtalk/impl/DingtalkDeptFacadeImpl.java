package com.baiyi.opscloud.facade.dingtalk.impl;

import com.baiyi.opscloud.builder.dingtalk.OcDingtalkDeptBuilder;
import com.baiyi.opscloud.decorator.dingtalk.DingtalkDeptDecorator;
import com.baiyi.opscloud.decorator.dingtalk.DingtalkUserDecorator;
import com.baiyi.opscloud.dingtalk.DingtalkDeptApi;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkDept;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.baiyi.opscloud.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.opscloud.facade.dingtalk.DingtalkDeptFacade;
import com.baiyi.opscloud.service.dingtalk.OcDingtalkDeptService;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 3:03 下午
 * @Since 1.0
 */
@Slf4j
@Component("DingtalkFacade")
public class DingtalkDeptFacadeImpl implements DingtalkDeptFacade {

    private final static Long DEPT_ROOT_ID = 1L;

    @Resource
    private DingtalkDeptApi dingtalkDeptApi;

    @Resource
    private OcDingtalkDeptService ocDingtalkDeptService;

    @Resource
    private DingtalkDeptDecorator dingtalkDeptDecorator;

    @Resource
    private DingtalkUserDecorator dingtalkUserDecorator;

    @Override
    public BusinessWrapper<Boolean> syncDept(String uid) {
        DingtalkParam.QueryByDeptId param = new DingtalkParam.QueryByDeptId();
        param.setUid(uid);
        param.setDeptId(DEPT_ROOT_ID);
        syncDingtalkDept(param);
        syncDept(param);
        dingtalkDeptDecorator.evictPreview(uid);
        dingtalkUserDecorator.clearMap();
        return BusinessWrapper.SUCCESS;
    }

    // 递归同步dept
    public void syncDept(DingtalkParam.QueryByDeptId param) {
        List<Long> deptIdList = dingtalkDeptApi.getDeptIdList(param);
        if (CollectionUtils.isEmpty(deptIdList))
            return;
        deptIdList.forEach(deptId -> {
            DingtalkParam.QueryByDeptId newParam = new DingtalkParam.QueryByDeptId();
            newParam.setUid(param.getUid());
            newParam.setDeptId(deptId);
            syncDingtalkDept(newParam);
            syncDept(newParam);
        });
    }

    public void syncDingtalkDept(DingtalkParam.QueryByDeptId param) {
        HashMap<Long, OcDingtalkDept> map = getDingtalkDeptMap(param.getDeptId(), param.getUid());
        List<OapiV2DepartmentListsubResponse.DeptBaseResponse> deptBaseResponseList = dingtalkDeptApi.getDeptList(param);
        deptBaseResponseList.forEach(deptBaseResponse -> {
            saveDingtalkDept(deptBaseResponse, param.getUid());
            map.remove(deptBaseResponse.getDeptId());
        });
        delDingtalkDeptByMap(map);
    }

    private HashMap<Long, OcDingtalkDept> getDingtalkDeptMap(Long deptId, String uid) {
        List<OcDingtalkDept> ocDingtalkDeptList = ocDingtalkDeptService.queryOcDingtalkDeptByParentId(deptId, uid);
        HashMap<Long, OcDingtalkDept> map = Maps.newHashMap();
        ocDingtalkDeptList.forEach(dept -> map.put(dept.getDeptId(), dept));
        return map;
    }

    private void saveDingtalkDept(OapiV2DepartmentListsubResponse.DeptBaseResponse deptBaseResponse, String uid) {
        OcDingtalkDept ocDingtalkDept = ocDingtalkDeptService.queryOcDingtalkDeptByDeptId(deptBaseResponse.getDeptId(), uid);
        OcDingtalkDept newOcDingtalkDept = OcDingtalkDeptBuilder.build(deptBaseResponse, uid);
        if (ocDingtalkDept == null) {
            try {
                ocDingtalkDeptService.addOcDingtalkDept(newOcDingtalkDept);
            } catch (Exception e) {
                log.error("新增dingtalk dept失败", e);
            }
        } else {
            newOcDingtalkDept.setId(ocDingtalkDept.getId());
            ocDingtalkDeptService.updateOcDingtalkDept(newOcDingtalkDept);
        }
    }

    private void delDingtalkDeptByMap(HashMap<Long, OcDingtalkDept> map) {
        map.forEach((key, value) -> ocDingtalkDeptService.deleteOcDingtalkDept(value.getId()));
    }

    @Override
    public BusinessWrapper<DingtalkVO.DeptTree> queryDingtalkDeptTree(String uid) {
        DingtalkVO.DeptTree tree = dingtalkDeptDecorator.decoratorTreeVO(uid);
        return new BusinessWrapper<>(tree);
    }

    @Override
    public BusinessWrapper<DingtalkVO.DeptTree> refreshDingtalkDeptTree(String uid) {
        dingtalkDeptDecorator.evictPreview(uid);
        DingtalkVO.DeptTree tree = dingtalkDeptDecorator.decoratorTreeVO(uid);
        return new BusinessWrapper<>(tree);
    }

    @Override
    public BusinessWrapper<List<OcDingtalkDept>> queryDingtalkRootDept() {
        List<OcDingtalkDept> dingtalkDeptList = ocDingtalkDeptService.queryDingtalkRootDept();
        return new BusinessWrapper<>(dingtalkDeptList);
    }
}
