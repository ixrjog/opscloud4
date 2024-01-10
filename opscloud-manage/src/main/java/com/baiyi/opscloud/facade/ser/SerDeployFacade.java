package com.baiyi.opscloud.facade.ser;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.ser.SerDeployParam;
import com.baiyi.opscloud.domain.vo.ser.SerDeployVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/6/7 11:10 AM
 * @Since 1.0
 */
public interface SerDeployFacade {

    DataTable<SerDeployVO.Task> querySerDeployTaskPage(SerDeployParam.TaskPageQuery pageQuery);

    SerDeployVO.Task getSerDeployTaskByUuid(SerDeployParam.QueryByUuid param);

    void uploadFile(MultipartFile file, String taskUuid);

    void addSerDeployTask(SerDeployParam.AddTask addTask);

    void updateSerDeployTask(SerDeployParam.UpdateTask updateTask);

    void deleteSerDeployTaskItem(Integer id);

    void addSerDeploySubTask(SerDeployParam.AddSubTask addSubTask);

    void deploySubTask(SerDeployParam.DeploySubTask deploySubTask);

    void deploySubTaskCallback(SerDeployParam.DeploySubTaskCallback callback);

    List<SerDeployVO.SerDetail> queryCurrentSer(SerDeployParam.QueryCurrentSer queryCurrentSer);

}