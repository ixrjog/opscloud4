package com.baiyi.caesar.domain.vo.datasource;

import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.domain.vo.base.BaseVO;
import com.baiyi.caesar.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/18 4:54 下午
 * @Version 1.0
 */
public class DsInstanceVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Instance extends BaseVO implements TagVO.ITags {

        private final int businessType = BusinessTypeEnum.DATASOURCE_INSTANCE.getType();

        private List<TagVO.Tag> tags;

        @Override
        public int getBusinessId() {
            return this.id;
        }

        private List<Instance> children;

        private Integer id;

        private Integer parentId;

        private String instanceName;

        private String uuid;

        private String instanceType;

        private String kind;

        private String version;

        private Boolean isActive;

        private Integer configId;

        private Date createTime;

        private Date updateTime;

        private String comment;

    }
}
