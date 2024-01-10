package com.baiyi.opscloud.workorder.entry;

import com.baiyi.opscloud.common.util.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2023/10/9 17:57
 * @Version 1.0
 */
public class AwsTransferCreateUserEntry {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransferUser implements Serializable {

        public static final TransferUser NEW = TransferUser.builder().build();

        @Serial
        private static final long serialVersionUID = 7195257696732256773L;

        private String userName;

        private String sshPublicKey;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

    }

}