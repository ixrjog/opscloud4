package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.GitlabAccessLevelConstants;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.delegate.GitlabProjectDelegate;
import com.baiyi.opscloud.workorder.delegate.GitlabUserDelegate;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetExtendedBaseTicketProcessor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
public class GitlabProjectTicketProcessor extends AbstractDsAssetExtendedBaseTicketProcessor<DatasourceInstanceAsset, GitlabConfig> {

    @Resource
    private GitlabProjectDelegate gitlabProjectDelegate;

    @Resource
    private GitlabUserDelegate gitlabUserDelegate;

    @Resource
    private InstanceHelper instanceHelper;

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, DatasourceInstanceAsset entry) throws TicketProcessException {
        GitlabConfig.Gitlab config = getDsConfig(ticketEntry, GitlabConfig.class).getGitlab();
        WorkOrderTicket ticket = getTicketById(ticketEntry.getWorkOrderTicketId());
        String username = ticket.getUsername();
        String role = ticketEntry.getRole();
        // 预检查用户
        User gitLabUser = preCheckUser(config, username);

        Optional<GitlabAccessLevelConstants> optionalGitlabAccessLevelConstants = Arrays.stream(GitlabAccessLevelConstants.values())
                .filter(e -> e.getRole().equalsIgnoreCase(role))
                .findFirst();

        if (!optionalGitlabAccessLevelConstants.isPresent())
            throw new TicketProcessException("角色名称错误: role={}", role);

        AccessLevel accessLevel = AccessLevel.forValue(optionalGitlabAccessLevelConstants.get().getAccessValue());
        List<Member> projectMembers = gitlabProjectDelegate.getProjectMembers(config, Long.valueOf(entry.getAssetId()));

        Optional<Member> optionalProjectMember = projectMembers.stream().filter(e -> e.getId().equals(gitLabUser.getId())).findFirst();
        if (optionalProjectMember.isPresent()) {
            if (!accessLevel.value.equals(optionalProjectMember.get().getAccessLevel().value)) {
                log.info("更新用户项目角色: userId={}, projectId={}, accessLevel={}", gitLabUser.getId(), entry.getAssetId(), accessLevel.name());
                gitlabProjectDelegate.updateProjectMember(config,
                        Long.valueOf(entry.getAssetId()),
                        gitLabUser.getId(),
                        accessLevel);
            }
        } else {
            log.info("新增用户项目角色: userId={}, projectId={}, accessLevel={}", gitLabUser.getId(), entry.getAssetId(), accessLevel.name());
            gitlabProjectDelegate.addProjectMember(config,
                    Long.valueOf(entry.getAssetId()),
                    gitLabUser.getId(),
                    accessLevel);
        }
    }

    private User preCheckUser(GitlabConfig.Gitlab config, String username) {
        List<User> gitlabUsers = gitlabUserDelegate.findUser(config, username);
        Optional<User> optionalGitlabUser = gitlabUsers.stream().filter(e -> e.getUsername().equals(username)).findFirst();
        return optionalGitlabUser.orElseGet(() -> gitlabUserDelegate.createGitlabUser(config, username));
    }

    @Override
    protected void pullAsset(WorkOrderTicketEntry ticketEntry, DatasourceInstanceAsset entry) {
        DsAssetParam.PullAsset pullAsset = DsAssetParam.PullAsset.builder()
                .instanceId(instanceHelper.getInstanceByUuid(ticketEntry.getInstanceUuid()).getId())
                .assetType(getAssetType())
                .build();
        dsInstanceFacade.pullAsset(pullAsset);
    }

    @Override
    public void verifyHandle(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
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
        if (!OrderTicketPhaseCodeConstants.NEW.name().equals(ticket.getTicketPhase()))
            throw new TicketProcessException("工单进度不是新建，无法更新配置条目！");
        String role = ticketEntry.getRole();
        if (Arrays.stream(GitlabAccessLevelConstants.values()).noneMatch(e -> e.getRole().equalsIgnoreCase(role))) {
            throw new TicketProcessException("修改角色错误，不支持该名称！");
        }
        WorkOrderTicketEntry preTicketEntry = ticketEntryService.getById(ticketEntry.getId());
        preTicketEntry.setRole(role);
        updateTicketEntry(preTicketEntry);
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.GITLAB_PROJECT.name();
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


