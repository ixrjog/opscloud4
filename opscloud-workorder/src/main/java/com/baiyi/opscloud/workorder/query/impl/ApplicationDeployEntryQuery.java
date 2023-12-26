package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.common.util.time.AgoUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.ApplicationDeployEntry;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import com.google.common.base.Joiner;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/5/10 16:20
 * @Version 1.0
 */
@Component
public class ApplicationDeployEntryQuery extends BaseTicketEntryQuery<ApplicationDeployEntry.LeoBuildVersion> {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private LeoBuildService leoBuildService;

    @Resource
    private EnvService envService;

    @Override
    protected List<ApplicationDeployEntry.LeoBuildVersion> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        Application application = applicationService.getByName(entryQuery.getQueryName());
        Env env = envService.getByEnvName(Global.ENV_PROD);
        if (application == null) {
            return Collections.emptyList();
        }
        LeoJobParam.JobBuildPageQuery pageQuery = LeoJobParam.JobBuildPageQuery.builder()
                .applicationId(application.getId())
                .envType(env.getEnvType())
                .isActive(true)
                .page(1)
                .length(entryQuery.getLength())
                .build();
        DataTable<LeoBuild> dataTable = leoBuildService.queryBuildPage(pageQuery);

        return dataTable.getData().stream().map(e -> {
                    ApplicationDeployEntry.LeoBuildVersion entry = BeanCopierUtil.copyProperties(e, ApplicationDeployEntry.LeoBuildVersion.class);
                    entry.setApplication(application);
                    return entry;
                }
        ).collect(Collectors.toList());
    }

    @Override
    protected WorkOrderTicketVO.Entry<ApplicationDeployEntry.LeoBuildVersion> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery,
            ApplicationDeployEntry.LeoBuildVersion entry) {
        String ago = AgoUtil.format(entry.getEndTime());
        String buildInfo = StringFormatter.format("#{}构建", entry.getBuildNumber());
        String comment = Joiner.on(" ").join(ago, buildInfo);
        return WorkOrderTicketVO.Entry.<ApplicationDeployEntry.LeoBuildVersion>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getVersionName())
                .entryKey(entry.getId().toString())
                .businessType(BusinessTypeEnum.LEO_BUILD.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .entry(entry)
                .comment(comment)
                .build();
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APPLICATION_DEPLOY.name();
    }

}