package com.baiyi.opscloud.gitlab;

import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import org.gitlab.api.models.GitlabUser;

/**
 * @Author baiyi
 * @Date 2020/1/14 4:16 下午
 * @Version 1.0
 */
public interface GitlabUserCenter {

    GitlabUser createUser(OcUser ocUser, String userDN);

    boolean pushKey(OcUser ocUser, OcAccount ocAccount, UserCredentialVO.UserCredential credential);

}
