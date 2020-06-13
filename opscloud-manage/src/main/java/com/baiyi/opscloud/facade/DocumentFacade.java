package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.vo.document.DocumentVO;

/**
 * @Author baiyi
 * @Date 2020/5/12 5:36 下午
 * @Version 1.0
 */
public interface DocumentFacade {

    DocumentVO.Doc queryDocByKey(String docKey);

    DocumentVO.Doc queryDocById(int id);


    DocumentVO.UserDoc queryUserDocByType(int docType);

    BusinessWrapper<Boolean> saveUserDoc(DocumentVO.UserDoc userDoc);
}
