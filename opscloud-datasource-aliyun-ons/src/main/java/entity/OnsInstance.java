package entity;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/12/14 4:32 PM
 * @Version 1.0
 */
public class OnsInstance {

    @Data
    public static class Instance {
        private String instanceId;
        private Integer instanceStatus;
        private Long releaseTime;
        private Integer instanceType;
        private String instanceName;
        private Boolean independentNaming;

        private String regionId;
    }
}
