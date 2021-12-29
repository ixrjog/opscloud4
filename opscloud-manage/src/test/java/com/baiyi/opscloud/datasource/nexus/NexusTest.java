package com.baiyi.opscloud.datasource.nexus;

import com.baiyi.opscloud.datasource.nexus.base.BaseNexusTest;
import com.baiyi.opscloud.datasource.nexus.entity.NexusAsset;
import com.baiyi.opscloud.datasource.nexus.entity.NexusRepository;
import com.baiyi.opscloud.datasource.nexus.drive.NexusAssetDrive;
import com.baiyi.opscloud.datasource.nexus.drive.NexusRepositoryDrive;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/5 6:12 下午
 * @Version 1.0
 */
public class NexusTest extends BaseNexusTest {

    @Resource
    private NexusRepositoryDrive nexusRepositoryHandler;

    @Resource
    private NexusAssetDrive nexusAssetHandler;

    @Test
    void listRepositoriesTest() {
        List<NexusRepository.Repository> repositories = nexusRepositoryHandler.list(getConfig().getNexus());
        repositories.forEach(e ->
                System.err.println(e)
        );
    }

    @Test
    void listAssetsTest() {
        NexusAsset.Assets assets = nexusAssetHandler.list(getConfig().getNexus(), "maven-snapshots", "");
        assets.getItems().forEach(e->
                System.err.println(e)
        );
    }

}
