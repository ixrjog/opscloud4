package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.param.keybox.KeyboxParam;
import com.baiyi.opscloud.domain.vo.keybox.KeyboxVO;

/**
 * @Author baiyi
 * @Date 2020/5/9 6:17 下午
 * @Version 1.0
 */
public interface KeyboxFacade {

    SSHKeyCredential getSSHKeyCredential(String systemUser);

    DataTable<KeyboxVO.Keybox> queryKeyboxPage(KeyboxParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addKeybox(KeyboxVO.Keybox keybox);

    BusinessWrapper<Boolean> deleteKeyboxById(int id);
}
