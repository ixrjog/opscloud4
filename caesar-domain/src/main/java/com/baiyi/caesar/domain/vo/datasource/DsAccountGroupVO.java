package com.baiyi.caesar.domain.vo.datasource;

import com.baiyi.caesar.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/15 11:18 上午
 * @Version 1.0
 */
public class DsAccountGroupVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Group extends BaseVO {

        List<DsAccountVO.Account> accounts;

        private Integer id;

        private String accountUid;

        private String accountId;

        private Integer accountType;

        private String name;

        private String displayName;

        private Boolean isActive;

        private String comment;
    }
}
