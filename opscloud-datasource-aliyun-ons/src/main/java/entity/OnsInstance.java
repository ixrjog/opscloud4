package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/12/14 4:32 PM
 * @Version 1.0
 */
public class OnsInstance {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class InstanceBaseInfo {
        private String instanceId;
        private Integer instanceStatus;
        private Long releaseTime;
        private Integer instanceType;
        private String instanceName;
        private Boolean independentNaming;
        private String remark;
        private Integer topicCapacity;
        private Long maxTps;

        private Endpoints endpoints;

        private String regionId;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Endpoints {
        private String tcpEndpoint;
        private String httpInternetEndpoint;
        private String httpInternetSecureEndpoint;
        private String httpInternalEndpoint;
    }


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
