package com.baiyi.opscloud.vmware.vcsa.instance;

import com.baiyi.opscloud.common.cloud.BaseCloudServerInstance;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.HostConfigInfo;
import com.vmware.vim25.HostHardwareInfo;
import com.vmware.vim25.HostListSummary;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/15 1:37 下午
 * @Version 1.0
 */
@Data
@Builder
public class ESXiInstance implements BaseCloudServerInstance {
    private HostHardwareInfo hostHardwareInfo;
    private HostListSummary hostSummary;
    private List<DatastoreSummary> datastoreSummaryList;
    private HostConfigInfo hostConfigInfo;
}
