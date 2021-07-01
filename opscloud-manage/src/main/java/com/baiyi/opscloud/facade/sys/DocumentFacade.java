package com.baiyi.opscloud.facade.sys;

import com.baiyi.opscloud.domain.param.sys.DocumentParam;
import com.baiyi.opscloud.domain.vo.sys.DocumentVO;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:29 上午
 * @Version 1.0
 */
public interface DocumentFacade {

    DocumentVO.Doc previewDocument(DocumentParam.DocumentQuery query);

}
