package com.baiyi.opscloud.domain.vo.dingtalk;

import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkDept;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.account.AccountVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/15 11:48 上午
 * @Since 1.0
 */
public class DingtalkVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class User extends AccountVO.Account {
        private OcUser ocUser;
        private Map<String, List<OcDingtalkDept>> deptMap;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class DeptTree implements Serializable {
        private static final long serialVersionUID = -9038281869057533834L;
        private String name;
        private List<DingtalkVO.ChildDeptTree> children;
    }

    @Data
    @Builder
    @ApiModel
    public static class ChildDeptTree implements Serializable {
        private static final long serialVersionUID = 3808915294119836396L;
        private Integer id;
        private String name;
        private List<DingtalkVO.ChildDeptTree> children;
    }
}
