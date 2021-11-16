package com.baiyi.opscloud.datasource.nexus;

import com.baiyi.opscloud.datasource.nexus.base.BaseNexusTest;
import com.baiyi.opscloud.nexus.entry.NexusAsset;
import com.baiyi.opscloud.nexus.entry.NexusRepository;
import com.baiyi.opscloud.nexus.datasource.NexusAssetDatasource;
import com.baiyi.opscloud.nexus.datasource.NexusRepositoryDatasource;
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
    private NexusRepositoryDatasource nexusRepositoryHandler;

    @Resource
    private NexusAssetDatasource nexusAssetHandler;

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
