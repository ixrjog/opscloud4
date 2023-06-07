package com.baiyi.opscloud.facade.ser;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 修远
 * @Date 2023/6/7 11:10 AM
 * @Since 1.0
 */
public interface SerDeployFacade {

    void uploadFile(MultipartFile file);

}
