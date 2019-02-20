package com.sdg.cmdb.domain.aliyunMQ;

import com.aliyuncs.ons.model.v20180628.OnsGroupListResponse;
import com.sdg.cmdb.domain.auth.UserDO;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class AliyunMqGroupDO implements Serializable,Comparable<AliyunMqGroupDO> {

    private static final long serialVersionUID = -481399816505176893L;

    private long id;
    private String groupId;
    private String remark;
    private long userId;
    private String displayName;

    // TODO 告警通知
    private boolean notice; // 是否告警通知
    private long totalDiff; // 堆积量阈值
    private long delayTime; // 消费延迟阈值
    private boolean online; // 是否在线
    private boolean rebalanceOK; // 订阅关系是否一致

    private long lastTotalDiff; // 最后采样堆积量
    private long lastDelayTime; // 最后采样消费延迟
    private boolean lastOnline; // 最后采样是否在线
    private boolean lastRebalanceOK; // 最后采样订阅关系是否一致

    private String gmtCreate;
    private String gmtModify;

    /**
     * 按堆积量倒序排序
     * @param aliyunMqGroupDO
     * @return
     */
    @Override
    public int compareTo(AliyunMqGroupDO aliyunMqGroupDO) {
        //自定义比较方法，如果认为此实体本身大则返回1，否则返回-1
        try {
            if (this.lastTotalDiff == aliyunMqGroupDO.getLastTotalDiff())
                return 0;
            if (this.lastTotalDiff > aliyunMqGroupDO.getLastTotalDiff())
                return -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public AliyunMqGroupDO(String groupId, String remark, UserDO userDO) {
        this.groupId = groupId;
        this.remark = remark;
        this.userId = userDO.getId();
        if (StringUtils.isEmpty(userDO.getDisplayName())) {
            this.displayName = userDO.getUsername();
        } else {
            this.displayName = userDO.getUsername() + "<" + userDO.getDisplayName() + ">";
        }
    }

    public AliyunMqGroupDO(OnsGroupListResponse.SubscribeInfoDo subscribeInfoDo) {
        this.groupId = subscribeInfoDo.getGroupId();
        this.remark = subscribeInfoDo.getRemark();
    }

    public AliyunMqGroupDO() {

    }


}
