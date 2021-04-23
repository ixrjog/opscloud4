package com.baiyi.opscloud.service.dingtalk;

import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalk;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/16 3:27 下午
 * @Since 1.0
 */
public interface OcDingtalkService {

    OcDingtalk queryOcDingtalkByKey(String dingtalkKey);

}
