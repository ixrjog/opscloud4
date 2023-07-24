package com.baiyi.opscloud.facade.sys;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.param.sys.DocumentParam;
import com.baiyi.opscloud.domain.vo.sys.DocumentVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2023/2/8 13:06
 * @Version 1.0
 */
class DocumentFacadeTest extends BaseUnit {

    @Resource
    private DocumentFacade documentFacade;

    @Test
    public void test() {
        SessionHolder.setUsername("baiyi");

        DocumentParam.DocumentZoneQuery query = DocumentParam.DocumentZoneQuery.builder()
                .mountZone("HOME")
                .build();

        DocumentVO.DocZone docZone = documentFacade.getDocZone(query);

        print(docZone);

    }

}