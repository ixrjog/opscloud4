package com.baiyi.opscloud.common.constants;

/**
 * @Author 修远
 * @Date 2023/7/5 4:32 PM
 * @Since 1.0
 */
public class SerDeployConstants {

    public interface SubTaskResult {
        String SUCCESS = "SUCCESS";
        String CALL_FAIL = "CALL_FAIL";
        String VALID_FAIL = "VALID_FAIL";
    }

    public interface SubTaskStatus {
        String CREATE = "CREATE";
        String RELOADING = "RELOADING";
        String FINISH = "FINISH";
    }

}