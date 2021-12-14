package entity;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/12/14 7:08 PM
 * @Version 1.0
 */
public class OnsRocketMqTopic {

    /**
     * PublishInfoDo
     */
    @Data
    public static class Topic {
        private String topic;
        private String owner;
        private Integer relation;
        private String relationName;
        private Long createTime;
        private String remark;
        private Integer messageType;
        private String instanceId;
        private Boolean independentNaming;

        private String regionId;
    }

}
