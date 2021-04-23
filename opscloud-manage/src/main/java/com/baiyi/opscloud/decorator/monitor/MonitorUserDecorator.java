package com.baiyi.opscloud.decorator.monitor;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.vo.monitor.MonitorVO;
import com.baiyi.opscloud.zabbix.entry.ZabbixMedia;
import com.baiyi.opscloud.zabbix.entry.ZabbixUsergroup;
import com.baiyi.opscloud.zabbix.server.ZabbixUserServer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.account.impl.ZabbixAccount.ZABBIX_DEFAULT_USERGROUP;

/**
 * @Author baiyi
 * @Date 2020/12/9 11:31 上午
 * @Version 1.0
 */
@Component("MonitorUserDecorator")
public class MonitorUserDecorator {

    @Resource
    private ZabbixUserServer zabbixUserServer;

    public MonitorVO.User decorator(MonitorVO.User user) {
        // 用户组
        BusinessWrapper usrgrpsWrapper = zabbixUserServer.getUserUsrgrps(user.getUsername());
        if (usrgrpsWrapper.isSuccess()) {
            List<ZabbixUsergroup> usergroups = (List<ZabbixUsergroup>) usrgrpsWrapper.getBody();
            user.setUsrgrps(convertUsrgrps(usergroups));
        }
        // 媒体
        BusinessWrapper mediasWrapper = zabbixUserServer.getUserMedias(user.getUsername());
        if (mediasWrapper.isSuccess()) {
            List<ZabbixMedia> medias = (List<ZabbixMedia>) mediasWrapper.getBody();
            user.setMedias(convertMedias(medias));
        }

        return user;
    }

    private List<MonitorVO.Usrgrp> convertUsrgrps(List<ZabbixUsergroup> usergroups) {
        return BeanCopierUtils.copyListProperties(usergroups.stream().filter(e ->
                !e.getName().equals(ZABBIX_DEFAULT_USERGROUP)
        ).collect(Collectors.toList()), MonitorVO.Usrgrp.class);
    }

    private List<MonitorVO.Media> convertMedias(List<ZabbixMedia> medias) {
        return medias.stream().map(e -> {
            MonitorVO.Media media = new MonitorVO.Media();
            media.setMediaid(Integer.valueOf(e.getMediaid()));
            media.setMediatypeid(Integer.valueOf(e.getMediatypeid()));
            media.setActive("0".equals(e.getActive()));
            switch (media.getMediatypeid()) {
                case 1:
                    media.setSendto(e.getSendto().get(0).asText());
                    break;
                case 3:
                    media.setSendto(e.getSendto().asText());
                    break;
                default:
            }
            return media;
        }).collect(Collectors.toList());
    }
}
