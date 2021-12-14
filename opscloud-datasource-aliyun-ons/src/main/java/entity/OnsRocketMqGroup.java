package entity;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/12/14 7:06 PM
 * @Version 1.0
 */
public class OnsRocketMqGroup {

    // SubscribeInfoDo
    @Data
    public static class Group {
        private String owner;
        private String groupId;
        private Long updateTime;
        private String remark;
        private String instanceId;
        private Boolean independentNaming;
        private Long createTime;
        private String groupType;

        private String regionId;
    }
}
