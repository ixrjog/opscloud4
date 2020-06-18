package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.bo.UserDocumentBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcDocument;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserDocument;

/**
 * @Author baiyi
 * @Date 2020/5/13 2:28 下午
 * @Version 1.0
 */
public class UserDocumentBuilder {

    public static OcUserDocument build(OcUser ocUser, OcDocument ocDocument) {
        UserDocumentBO userDocumentBO =  UserDocumentBO.builder()
                .userId(ocUser.getId())
                .username(ocUser.getUsername())
                .comment(ocDocument.getComment())
                .docTitle("用户写字板")
                .docContent(ocDocument.getDocContent())
                .build();
        return covert(userDocumentBO);
    }

    private static OcUserDocument covert(UserDocumentBO userDocumentBO) {
        return BeanCopierUtils.copyProperties(userDocumentBO, OcUserDocument.class);
    }
}
