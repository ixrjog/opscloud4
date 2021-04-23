package com.baiyi.opscloud.dingtalk;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.base.AccountType;
import com.baiyi.opscloud.common.base.DingtalkMsgType;
import com.baiyi.opscloud.common.util.PinYinUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkMsgParam;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.baiyi.opscloud.facade.dingtalk.DingtalkDeptFacade;
import com.baiyi.opscloud.service.user.OcAccountService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 11:13 上午
 * @Since 1.0
 */
public class DingtalkServerTest extends BaseUnit {

    @Resource
    private DingtalkDeptFacade dingtalkFacade;

    @Resource
    private DingtalkMsgApi dingtalkMsgApi;

    @Resource
    private OcAccountService ocAccountService;

    @Resource
    private OcUserService ocUserService;

    @Test
    void syncDeptTest() {
//        dingtalkFacade.syncDept("xincheng");
        String xx = "修远";
        String yy = PinYinUtils.toPinYin(xx);
        System.err.println(yy);
    }

    @Test
    void sendMsgTest() {
        DingtalkParam.SendAsyncMsg param = new DingtalkParam.SendAsyncMsg();
        OcUser ocUser = ocUserService.queryOcUserByUsername("xiuyuan");
        OcAccount ocAccount = ocAccountService.queryOcAccountByUserId(AccountType.DINGTALK.getType(), ocUser.getId());
        Set<String> useridList = Sets.newHashSet(ocAccount.getUsername());
        param.setUseridList(useridList);
        DingtalkMsgParam.LinkMsg link = DingtalkMsgParam.LinkMsg.builder()
                .title("修远的测试消息-标题")
                .text("修远的测试消息-内容4")
                .messageUrl("https://oc.xinc818.com/index.html#/workbench/workorder")
                .picUrl("https://opscloud-res.oss-cn-hangzhou.aliyuncs.com/opscloud/workorder-msg/opscloud-workorder-msg.jpg")
                .build();
        DingtalkMsgParam msg = DingtalkMsgParam.builder()
                .msgtype(DingtalkMsgType.LINK.getType())
                .link(link)
                .build();
        param.setMsg(msg);
        param.setUid(ocAccount.getAccountUid());
        dingtalkMsgApi.sendAsyncMsg(param);
    }

}
