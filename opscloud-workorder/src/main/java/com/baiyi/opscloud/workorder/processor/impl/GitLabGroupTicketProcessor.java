package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.GitLabAccessLevelConstants;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.delegate.GitLabGroupDelegate;
import com.baiyi.opscloud.workorder.delegate.GitLabUserDelegate;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetExtendedBaseTicketProcessor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.User;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/6/27 09:49
 * @Version 1.0
 */
@Slf4j
@Component
public class GitLabGroupTicketProcessor extends AbstractDsAssetExtendedBaseTicketProcessor<DatasourceInstanceAsset, GitLabConfig> {

    @Resource
    private GitLabGroupDelegate gitlabGroupDelegate;

    @Resource
    private GitLabUserDelegate gitlabUserDelegate;

    @Resource
    private InstanceHelper instanceHelper;

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, DatasourceInstanceAsset entry) throws TicketProcessException {
        GitLabConfig.GitLab config = getDsConfig(ticketEntry, GitLabConfig.class).getGitlab();
        WorkOrderTicket ticket = getTicketById(ticketEntry.getWorkOrderTicketId());
        String username = ticket.getUsername();
        String role = ticketEntry.getRole();

        User gitLabUser = getOrCreateUser(config, username);
        GitLabAccessLevelConstants gitlabAccessLevel = Arrays.stream(GitLabAccessLevelConstants.values()).filter(e -> e.getRole().equalsIgnoreCase(role)).findFirst()
                .orElseThrow(() -> new TicketProcessException("GitLab角色名称错误: role={}", role));
        AccessLevel accessLevel = AccessLevel.forValue(gitlabAccessLevel.getAccessValue());
        List<Member> groupMembers = gitlabGroupDelegate.getMembers(config, Integer.parseInt(entry.getAssetId()));
        Optional<Member> optionalGitlabGroupMember = groupMembers.stream().filter(e -> e.getId().equals(gitLabUser.getId())).findFirst();
        if (optionalGitlabGroupMember.isPresent()) {
            // 用户已经拥有相同的角色
            if (accessLevel.value.equals(optionalGitlabGroupMember.get().getAccessLevel().value)) {
                log.info("用户已经拥有群组角色，无需更新: userId={}, accessLevel={}", gitLabUser.getId(), accessLevel.name());
            } else {
                log.info("更新用户群组角色: userId={}, groupId={}, accessLevel={}", gitLabUser.getId(), entry.getAssetId(), accessLevel.name());
                gitlabGroupDelegate.updateMember(config,
                        Long.valueOf(entry.getAssetId()),
                        gitLabUser.getId(),
                        accessLevel);
            }
        } else {
            log.info("新增用户群组角色: userId={}, groupId={}, accessLevel={}", gitLabUser.getId(), entry.getAssetId(), accessLevel.name());
            gitlabGroupDelegate.addMember(config,
                    Long.valueOf(entry.getAssetId()),
                    gitLabUser.getId(),
                    accessLevel);
        }
    }

    private User getOrCreateUser(GitLabConfig.GitLab config, String username) {
        List<User> gitlabUsers = gitlabUserDelegate.findUsers(config, username);
        Optional<User> optionalGitlabUser = gitlabUsers.stream().filter(e -> e.getUsername().equals(username)).findFirst();
        return optionalGitlabUser.orElseGet(() -> gitlabUserDelegate.createUser(config, username));
    }

    @Override
    protected void pullAsset(WorkOrderTicketEntry ticketEntry, DatasourceInstanceAsset entry) {
        DsAssetParam.PullAsset pullAsset = DsAssetParam.PullAsset.builder().instanceId(instanceHelper.getInstanceByUuid(ticketEntry.getInstanceUuid()).getId()).assetType(getAssetType()).build();
        dsInstanceFacade.pullAsset(pullAsset);
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        DatasourceInstanceAsset entry = this.toEntry(ticketEntry.getContent());
        DatasourceInstanceAsset asset = getAsset(entry);
        verifyEntry(asset);
    }

    @Override
    public void update(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        updateHandle(ticketEntry);
    }

    /**
     * 更新工单条目配置(修改角色)
     *
     * @param ticketEntry
     */
    private void updateHandle(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        WorkOrderTicket ticket = ticketService.getById(ticketEntry.getWorkOrderTicketId());
        if (!OrderTicketPhaseCodeConstants.NEW.name().equals(ticket.getTicketPhase())) {
            throw new TicketProcessException("工单进度不是新建，无法更新配置条目！");
        }
        String role = ticketEntry.getRole();
        if (Arrays.stream(GitLabAccessLevelConstants.values()).noneMatch(e -> e.getRole().equalsIgnoreCase(role))) {
            throw new TicketProcessException("修改角色错误，不支持该名称！");
        }
        WorkOrderTicketEntry preTicketEntry = ticketEntryService.getById(ticketEntry.getId());
        preTicketEntry.setRole(role);
        updateTicketEntry(preTicketEntry);
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.GITLAB_GROUP.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.name();
    }

    @Override
    protected Class<DatasourceInstanceAsset> getEntryClassT() {
        return DatasourceInstanceAsset.class;
    }

}