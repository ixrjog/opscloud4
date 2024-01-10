package com.baiyi.opscloud.workorder.entry;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2023/5/11 13:46
 * @Version 1.0
 */
public class ApplicationDeployEntry {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LeoBuildVersion extends LeoBuild implements Serializable {

        @Serial
        private static final long serialVersionUID = -6877793248500302561L;

        private Application application;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }
    }

}