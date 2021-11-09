package com.baiyi.opscloud.opscloud;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroupType;
import com.baiyi.opscloud.opscloud.provider.OcServerGroupProvider;
import com.baiyi.opscloud.opscloud.vo.OcServerGroupTypeVO;
import com.baiyi.opscloud.opscloud.vo.OcServerGroupVO;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerGroupTypeService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2021/6/24 9:14 上午
 * @Version 1.0
 */
public class OcServerGroupTest extends BaseUnit {

    @Resource
    private OcServerGroupProvider ocServerGroupProvider;

    @Resource
    private ServerGroupTypeService serverGroupTypeService;

    @Resource
    private ServerGroupService serverGroupService;

    @Test
    void syncServerGroupType() {
        try {
            DataTable<OcServerGroupTypeVO.ServerGroupType> table = ocServerGroupProvider.queryServerGroupTypes();
            for (OcServerGroupTypeVO.ServerGroupType datum : table.getData()) {
                if (serverGroupTypeService.getByName(datum.getName()) != null) continue;
                ServerGroupType type = ServerGroupType.builder()
                        .color(datum.getColor())
                        .name(datum.getName())
                        .comment(datum.getComment())
                        .build();
                serverGroupTypeService.add(type);
            }
        } catch (IOException e) {
        }
    }

    @Test
    void syncServerGroup() {
        try {
            DataTable<OcServerGroupVO.ServerGroup> table = ocServerGroupProvider.queryServerGroups();
            for (OcServerGroupVO.ServerGroup datum : table.getData()) {
                if (serverGroupService.getByName(datum.getName()) != null) continue;
                ServerGroupType type = serverGroupTypeService.getByName(datum.getServerGroupType().getName());
                ServerGroup serverGroup = ServerGroup.builder()
                        .name(datum.getName())
                        .allowOrder(datum.getInWorkorder() == 1)
                        .comment(datum.getComment())
                        .serverGroupTypeId(type.getId())
                        .build();
                serverGroupService.add(serverGroup);
            }
        } catch (IOException e) {
        }
    }
}
