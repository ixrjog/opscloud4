package com.baiyi.opscloud.domain.base;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2023/5/17 16:50
 * @Version 1.0
 */
public class BaseProjectResource {

    public interface IProjectResource extends BaseBusiness.IBusiness {

        Integer getProjectId();

    }

    @Data
    @Builder
    public static class SimpleProjectResource implements IProjectResource {

        private Integer projectId;

        private Integer businessType;

        private Integer businessId;

    }

}