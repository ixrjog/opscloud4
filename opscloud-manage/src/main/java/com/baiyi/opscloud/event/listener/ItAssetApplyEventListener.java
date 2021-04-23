package com.baiyi.opscloud.event.listener;

import com.baiyi.opscloud.common.base.AccountType;
import com.baiyi.opscloud.common.base.DingtalkMsgType;
import com.baiyi.opscloud.common.util.BeetlUtils;
import com.baiyi.opscloud.common.util.ObjectUtils;
import com.baiyi.opscloud.dingtalk.DingtalkMsgApi;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkMsgParam;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;
import com.baiyi.opscloud.service.file.OcFileTemplateService;
import com.baiyi.opscloud.service.it.OcItAssetNameService;
import com.baiyi.opscloud.service.it.OcItAssetService;
import com.baiyi.opscloud.service.user.OcAccountService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/1 3:26 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class ItAssetApplyEventListener {


    private static final String FILE_TEMPLATE_IT_ASSET_APPLY_MSG = "DINGTALK_IT_ASSET_APPLY";
    private static final String DINGTALK_UID = "xincheng";

    @Resource
    private OcAccountService ocAccountService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcItAssetService ocItAssetService;

    @Resource
    private OcItAssetNameService ocItAssetNameService;

    @Resource
    private OcFileTemplateService ocFileTemplateService;

    @Resource
    private DingtalkMsgApi dingtalkMsgApi;

    @Subscribe
    public void sendDingtalkMsg(ItAssetParam.ApplyAsset applyAsset) {
        OcAccount ocAccount = ocAccountService.queryOcAccountByUserId(AccountType.DINGTALK.getType(), applyAsset.getUserId());
        if (ocAccount == null) return;
        OcUser ocUser = ocUserService.queryOcUserById(applyAsset.getUserId());
        if (ocUser == null) return;
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetById(applyAsset.getAssetId());
        OcItAssetName ocItAssetName =ocItAssetNameService.queryOcItAssetNameById(ocItAsset.getAssetNameId());
        ItAssetParam.ApplyAssetDingTalkMsg dingtalkMsg = ItAssetParam.ApplyAssetDingTalkMsg.builder()
                .displayName(ocUser.getDisplayName())
                .assetName(ocItAssetName.getAssetName())
                .build();
        Map<String, Object> contentMap = ObjectUtils.objectToMap(dingtalkMsg);
        OcFileTemplate template = ocFileTemplateService.queryOcFileTemplateByUniqueKey(FILE_TEMPLATE_IT_ASSET_APPLY_MSG, 0);
        try {
            DingtalkMsgParam.MarkdownMsg markdownMsg = DingtalkMsgParam.MarkdownMsg.builder()
                    .title("资产领用通知")
                    .text(BeetlUtils.renderTemplate(template.getContent(), contentMap))
                    .build();
            DingtalkMsgParam msgParam = DingtalkMsgParam.builder()
                    .msgtype(DingtalkMsgType.MARKDOWN.getType())
                    .markdown(markdownMsg)
                    .build();
            DingtalkParam.SendAsyncMsg msg = new DingtalkParam.SendAsyncMsg();
            msg.setUid(DINGTALK_UID);
            msg.setMsg(msgParam);
            msg.setUseridList(Sets.newHashSet(ocAccount.getUsername()));
            dingtalkMsgApi.sendAsyncMsg(msg);
        } catch (IOException e) {
            log.error("生成钉钉发送消息参数失败", e);
        }
    }
}
