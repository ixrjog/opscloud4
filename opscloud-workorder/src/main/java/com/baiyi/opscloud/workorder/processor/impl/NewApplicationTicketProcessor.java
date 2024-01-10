package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.util.ValidationUtil;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.NewApplicationEntry;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.base.BaseTicketProcessor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/6/5 15:09
 * @Version 1.0
 */
@Slf4j
@Component
public class NewApplicationTicketProcessor extends BaseTicketProcessor<NewApplicationEntry.NewApplication> {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private TagService tagService;

    @Resource
    private BusinessTagService businessTagService;

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Resource
    private UserPermissionService userPermissionService;

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.NEW_APPLICATION.name();
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, NewApplicationEntry.NewApplication entry) throws TicketProcessException {
        final String applicationName = ticketEntry.getName();
        if (applicationService.getByName(applicationName) != null) {
            throw new TicketProcessException("创建应用失败: 应用名称 {} 已存在！", applicationName);
        }
        Application application = Application.builder()
                .name(applicationName)
                .applicationKey(applicationName.toUpperCase())
                .applicationType(0)
                .isActive(true)
                .comment(ticketEntry.getComment())
                .build();
        applicationService.add(application);

        User user = queryCreateUser(ticketEntry);
        // 后处理
        postProcess(application, entry, user);
    }

    private void postProcess(Application application, NewApplicationEntry.NewApplication entry, User user) {
        // 打应用级别标签
        Tag tag = tagService.getByTagKey(entry.getLevelTag());
        if (tag != null) {
            try {
                BusinessTag businessTag = BusinessTag.builder()
                        .tagId(tag.getId())
                        .businessType(BusinessTypeEnum.APPLICATION.getType())
                        .businessId(application.getId())
                        .build();
                businessTagService.add(businessTag);
            } catch (Exception e) {
                log.debug("绑定应用标签错误: {}", e.getMessage());
            }
        }

        // 绑定应用资源(GitLab项目)
        ApplicationResource applicationResource = ApplicationResource.builder()
                .applicationId(application.getId())
                .name(entry.getAssetKey())
                .resourceType(entry.getAssetType())
                .virtualResource(false)
                .businessType(BusinessTypeEnum.ASSET.getType())
                .businessId(entry.getId())
                .comment(entry.getDescription())
                .build();
        try {
            applicationResourceService.add(applicationResource);
        } catch (Exception e) {
            log.debug("绑定应用资源错误: {}", e.getMessage());
        }

        // 用户授权
        if (user != null) {
            UserPermission userPermission = UserPermission.builder()
                    .userId(user.getId())
                    .businessType(BusinessTypeEnum.APPLICATION.getType())
                    .businessId(application.getId())
                    .build();
            try {
                userPermissionService.add(userPermission);
            } catch (Exception e) {
                log.debug("用户授权应用错误: {}", e.getMessage());
            }
        }

    }

    @Override
    protected Class<NewApplicationEntry.NewApplication> getEntryClassT() {
        return NewApplicationEntry.NewApplication.class;
    }

    @Override
    protected void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        if (!ValidationUtil.isApplication(ticketEntry.getName())) {
            throw new TicketVerifyException("校验工单条目失败: 应用名称不合规！");
        }
        if (applicationService.getByName(ticketEntry.getName()) != null) {
            throw new TicketVerifyException("校验工单条目失败: 应用名称 {} 已存在！", ticketEntry.getName());
        }
        if (StringUtils.isBlank(ticketEntry.getComment())) {
            throw new TicketVerifyException("校验工单条目失败: 必须填写应用描述！");
        }
        NewApplicationEntry.NewApplication entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isBlank(entry.getLevelTag())) {
            throw new TicketVerifyException("校验工单条目失败: 必须指定应用级别标签！");
        }
    }

}