package com.baiyi.opscloud.facade.ser;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.ser.SerDeployParam;
import com.baiyi.opscloud.domain.vo.ser.SerDeployVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 修远
 * @Date 2023/6/7 11:10 AM
 * @Since 1.0
 */
public interface SerDeployFacade {


    DataTable<SerDeployVO.Task> queryProjectPage(SerDeployParam.TaskPageQuery pageQuery);

    void uploadFile(MultipartFile file, String taskUuid);

}
